package com.single.website.crawler;

import com.cc.pdfparser.elastic.PDFTextStripperUtil;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.pdf.image.generator.PDFPageImageGenerator;
import com.pdf.image.generator.imageUtil.Image;
import com.pdf.warc.ccrawl.processor.PDFLinkRecordFromCCrawl;
import com.pdf.warc.ccrawl.processor.PostProcessedPDFRecordForUI;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a copy of JsonLinkReaderAndProcessor with changes in URL replacement
 */
public class BookToLearnReaderAndProcessor {
    private static final Pattern pattern1 = Pattern.compile("ISBN\\x20(?=.{13}$)\\d{1,5}([- ])\\d{1,7}\\1\\d{1,6}\\1(\\d|X)$\n", Pattern.CASE_INSENSITIVE);
    private static final Pattern pattern2 = Pattern.compile("^ISBN\\s(?=[-0-9xX ]{13}$)(?:[0-9]+[- ]){3}[0-9]*[xX0-9]$\n", Pattern.CASE_INSENSITIVE);
    private final static Pattern pattern3 = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})\n" +
            "[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)\n" +
            "(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", Pattern.CASE_INSENSITIVE);
    private static final LanguageDetector detector = new OptimaizeLangDetector().loadModels();
    /**
     * @param args
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private static MessageDigest md;

    static {

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nse) {
            nse.printStackTrace();
        }
    }

    private static JsonReader readEachJsonLine(String[] args) throws IOException {
        String jsonFileName = "";
        if (args != null && args.length > 0) {
            jsonFileName = args[0];
        } else {
            jsonFileName = "dl-booktolearn-com.json";
        }
        JsonReader jsonReader = new JsonReader(new FileReader(jsonFileName));
        //jsonReader.beginObject();
        jsonReader.setLenient(true);
        return jsonReader;
    }

    private static Writer getElasticJsonFileWriter() throws IOException {
        File postProcessedPDFRecordForUIJson = new File("elastic-upload" + System.currentTimeMillis() + ".json");
        Writer postProcessedPDFRecordForUIJsonWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(postProcessedPDFRecordForUIJson), StandardCharsets.UTF_8));
        return postProcessedPDFRecordForUIJsonWriter;
    }

    private static File generatePDFFileName(String md5) {
        StringBuilder pathBaseName = new StringBuilder("downloads").append("/").append(md5, 0, 3).append("/")
                .append(md5).append(".pdf");
        return new File(pathBaseName.toString());
    }

    public static void main(String[] args) throws IOException {
        JsonReader jsonReader = readEachJsonLine(args);
        Writer postProcessedPDFRecordForUIJsonWriter = getElasticJsonFileWriter();
        while (jsonReader.hasNext()) {
            PostProcessedPDFRecordForUI postProcessedPDFRecordForUI = new PostProcessedPDFRecordForUI();
            PDFLinkRecordFromCCrawl response = new Gson().fromJson(jsonReader, PDFLinkRecordFromCCrawl.class);
            postProcessedPDFRecordForUI.setUrl(response.getPdfFullURL());
            String urlToDownload = response.getPdfFullURL().
                    replaceAll("http://dl.booktolearn.com/ebooks2", "http://192.168.99.1:9090/dl.booktolearn.com/ebooks2");
            System.out.println("URL to download "+urlToDownload);
            postProcessedPDFRecordForUI.setFoundUrl(response.getWebPageUrl());
            postProcessedPDFRecordForUI.setFlNm(getFileNameFromUrl(response.getPdfFullURL()));
            if(response.getCategory() != null) {
                postProcessedPDFRecordForUI.setCategory(response.getCategory());
            }
            else
            {
                postProcessedPDFRecordForUI.setCategory("General");
            }
            String md5 = (new HexBinaryAdapter()).marshal(md.digest(response.getPdfFullURL().getBytes())).toLowerCase();
            File createdPDF = generatePDFFileName(md5); //file name created, file is not
            PDDocument pdDoc = null;
            try {
                createdPDF = downloadAndProcessPDF(urlToDownload, createdPDF);
                if (createdPDF.exists()) {
                    pdDoc = PDDocument.load(createdPDF, MemoryUsageSetting.setupTempFileOnly());
                    boolean doesPDFMeetsCriteria = checkIfPDFMeetsCriteria(pdDoc, createdPDF, md5, postProcessedPDFRecordForUI);
                    if (!doesPDFMeetsCriteria) {
                        if (pdDoc != null) {
                            pdDoc.close();
                            pdDoc = null;
                        }
                        deleteFileAndEmptyDIR(createdPDF);
                        continue;
                    }
                    extractPDFInfoIfValidPDF(pdDoc, md5, postProcessedPDFRecordForUI);
                    postProcessedPDFRecordForUIJsonWriter.append("{\"index\" : {\"_id\":\"" + md5 + "\"}}").append("\r\n");
                    postProcessedPDFRecordForUIJsonWriter.append((new Gson()).toJson(postProcessedPDFRecordForUI)).append("\r\n");
                    postProcessedPDFRecordForUIJsonWriter.flush();
                }
            } catch (Exception e) {
                //e.printStackTrace();
                postProcessedPDFRecordForUIJsonWriter.flush();
                if (pdDoc != null) {
                    pdDoc.close();
                    pdDoc = null;
                }
                deleteFileAndEmptyDIR(createdPDF);
                System.out.format("Exception in CreatedPDF %s, so Deleted - %s ", createdPDF.exists(), e.getLocalizedMessage());
                System.out.println();
            }
            finally {
                postProcessedPDFRecordForUIJsonWriter.flush();
                if (pdDoc != null) {
                    pdDoc.close();
                    pdDoc = null;
                }
                deleteFileAndEmptyDIR(createdPDF);
            }
        }
        postProcessedPDFRecordForUIJsonWriter.flush();
        postProcessedPDFRecordForUIJsonWriter.close();
        jsonReader.endObject();
        jsonReader.close();

    }

    private static String getFileNameFromUrl(String pdfFullURL) {
        URL urlObj = null;
        try
        {
            urlObj = new URL(pdfFullURL);
            String urlPath = urlObj.getPath();
            String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
            return fileName;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return pdfFullURL;
    }

     private static File downloadAndProcessPDF(String fileToDownload, File file) throws IOException {
        file.getParentFile().mkdirs();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            URL obj1 = new URL(fileToDownload);
            HttpURLConnection httpURLConnection = (HttpURLConnection) obj1.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setConnectTimeout(100000);
            httpURLConnection.setReadTimeout(100000);
            // optional default is GET
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Origin", "https://google.com");
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            httpURLConnection.setRequestProperty("Cache-Control", "max-age=0");
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "keep-alive");
            httpURLConnection.setRequestProperty("Host", "https://google.com");
            httpURLConnection.setRequestProperty("Referer", "https://google.com");
            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            httpURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("responseCode "+responseCode);
            if(!Objects.equals(HttpURLConnection.HTTP_OK, responseCode))
            {
                throw new IOException("PDF GET not Found - "+fileToDownload);
            }
            InputStream in = httpURLConnection.getInputStream();
            System.setProperty("http.agent", "Chrome");
            byte[] buffer = new byte[8096];// buffer for portion of data from connection
            int bytesread = 0, bytesBuffered = 0;
            while ((bytesread = in.read(buffer)) > -1) {
                fos.write(buffer, 0, bytesread);
                bytesBuffered += bytesread;
                if (bytesBuffered > 1024 * 1024) { //flush after 1MB
                    bytesBuffered = 0;
                    fos.flush();
                }
            }
            in.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    private static boolean checkIfPDFMeetsCriteria(PDDocument pdDoc, File pdfFile, String md5, PostProcessedPDFRecordForUI postProcessedPDFRecordForUI) throws IOException {
        long fileSize = pdfFile.length();
        int pageCount = 0;
        pageCount = pdDoc.getNumberOfPages();
        postProcessedPDFRecordForUI.setPgCnt(pageCount);
        postProcessedPDFRecordForUI.setSz(fileSize);
        if (fileSize < 220004 || pageCount < 5) //TODO  - or if I cannot identify a category
        {
            System.out.println();
            System.out.format("Rejecting %s because it is less than 200 KB. Size is %d bytes ", pdfFile.getPath(), fileSize);
            if (pdDoc != null) {
                pdDoc.close();
                pdDoc = null;
            }
            return false;
        }
        return true;
    }

    private static void extractPDFInfoIfValidPDF(PDDocument pdDoc, String md5, PostProcessedPDFRecordForUI postProcessedPDFRecordForUI) throws IOException {

        try {
            PDDocumentInformation info = pdDoc.getDocumentInformation();
            Set<String> metaDataKeys = info.getMetadataKeys();
            String title = info.getTitle();
            if (title != null && !title.trim().isEmpty()) {
                System.out.println("Title " + title);
                postProcessedPDFRecordForUI.setTitle(title);
            }
            Calendar cal = info.getCreationDate();
            if(cal != null)
            {
                long year = cal.get(Calendar.YEAR);
                if (year < 1000) {
                    postProcessedPDFRecordForUI.setYr(year + 1900L);
                }
                else
                {
                    postProcessedPDFRecordForUI.setYr(year);
                }
            }

            String keywords = info.getKeywords();
            if (keywords != null && !keywords.trim().isEmpty()) {
                System.out.println("keywords " + keywords);
                postProcessedPDFRecordForUI.setKeywords(keywords);
            }
            String subject = info.getSubject();
            if (subject != null && !subject.trim().isEmpty()) {
                System.out.println("subject " + subject);
                postProcessedPDFRecordForUI.setCategory(subject);
            }
            Optional<String> pdfTextFromStartPages = PDFTextStripperUtil.getDataAsString(pdDoc, 1, 4);
            if (pdfTextFromStartPages.isPresent()) {
                String pagesText = pdfTextFromStartPages.get();
                if(pagesText.length() < 500)
                {
                    postProcessedPDFRecordForUI.setDesc(pagesText);
                }
                else {
                    postProcessedPDFRecordForUI.setDesc(pagesText.substring(0, 500));
                }
                Matcher matcher1 = pattern1.matcher(pagesText);
                Matcher matcher2 = pattern2.matcher(pagesText);
                Matcher matcher3 = pattern3.matcher(pagesText);
                String isbnKeyword = postProcessedPDFRecordForUI.getKeywords();
                Set<String> isbnSet = new HashSet<>();
                while (matcher1.find())
                {
                    isbnSet.add(matcher1.group());
                }
                while (matcher2.find()) {
                    isbnSet.add(matcher1.group());
                }
                while (matcher3.find()) {
                    isbnSet.add(matcher1.group());
                }
                for(String isbnEntry: isbnSet)
                {
                    isbnKeyword = isbnKeyword + " | " +isbnEntry;
                }
                postProcessedPDFRecordForUI.setKeywords(isbnKeyword);
                findLanguageOfPdfText(postProcessedPDFRecordForUI, pagesText);
            }
            printImageFromPDFPage(pdDoc, md5, postProcessedPDFRecordForUI);
            if (pdDoc != null) {
                try {
                    pdDoc.close();
                    pdDoc = null;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (pdDoc != null) {
                try {
                    pdDoc.close();
                    pdDoc = null;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void findLanguageOfPdfText(PostProcessedPDFRecordForUI postProcessedPDFRecordForUI, String pagesText) {
        LanguageResult languageResult = detector.detect(pagesText);
        Locale locale = new Locale(languageResult.getLanguage());
        postProcessedPDFRecordForUI.setLanguage(locale.getDisplayLanguage());
    }

    private static void printImageFromPDFPage(PDDocument pdDocument, String md5, PostProcessedPDFRecordForUI postProcessedPDFRecordForUI) throws IOException {

        try {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            pdfRenderer.setSubsamplingAllowed(true);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 120, ImageType.RGB);
            StringBuilder imgPathBaseName = new StringBuilder("pdf-images").append("/").append(md5, 0, 3).append("/").append(md5);
            File imageFilePath = new File(imgPathBaseName.append(".png").toString());
            imageFilePath.getParentFile().mkdirs();
            PDFPageImageGenerator.generateThumbNailImagesFromPDFPage(new Image(bim, com.pdf.image.generator.imageUtil.ImageType.PNG), imageFilePath);
            postProcessedPDFRecordForUI.setImgFlPth(imageFilePath.getPath());
        } catch (IOException e) {
            throw e;
        }
    }

    private static void deleteFileAndEmptyDIR(File file) {
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                try {
                    boolean isdeleted = file.delete();
                    //if (isdeleted)
                     //   System.out.printf("Empty Directory '%s' is successfully deleted", file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                boolean isdeleted = file.delete();
                if (isdeleted) {
                    System.out.printf("File '%s' is successfully deleted ", file.getAbsolutePath());
                    System.out.println();
                    deleteFileAndEmptyDIR(new File(file.getParent()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createImageInsideFolder(File pdfFile) {
    }
}
