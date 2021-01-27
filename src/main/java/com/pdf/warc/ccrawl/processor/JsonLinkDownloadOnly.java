package com.pdf.warc.ccrawl.processor;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class JsonLinkDownloadOnly {
    private static final Pattern pattern1 = Pattern.compile("ISBN\\x20(?=.{13}$)\\d{1,5}([- ])\\d{1,7}\\1\\d{1,6}\\1(\\d|X)$\n", Pattern.CASE_INSENSITIVE);
    private static final Pattern pattern2 = Pattern.compile("^ISBN\\s(?=[-0-9xX ]{13}$)(?:[0-9]+[- ]){3}[0-9]*[xX0-9]$\n", Pattern.CASE_INSENSITIVE);
    private final static Pattern pattern3 = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})\n" +
            "[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)\n" +
            "(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$", Pattern.CASE_INSENSITIVE);
    private static final LanguageDetector detector = new OptimaizeLangDetector().loadModels();

    private static Set<String> topDomainTimeOutList = new HashSet<>();
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
            jsonFileName = "test.json";
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
        System.setProperty("http.agent", "https://pdfdomain.com/bot.html");
        JsonReader jsonReader = readEachJsonLine(args);
        Writer postProcessedPDFRecordForUIJsonWriter = getElasticJsonFileWriter();
        while (jsonReader.hasNext()) {
            PostProcessedPDFRecordForUI postProcessedPDFRecordForUI = new PostProcessedPDFRecordForUI();
            PDFLinkRecordFromCCrawl response = new Gson().fromJson(jsonReader, PDFLinkRecordFromCCrawl.class);
            String fullPDFURL = response.getPdfFullURL();
            postProcessedPDFRecordForUI.setUrl(fullPDFURL);
            postProcessedPDFRecordForUIJsonWriter.append("wget "+fullPDFURL).append("\r\n");
            postProcessedPDFRecordForUIJsonWriter.flush();
        }
        postProcessedPDFRecordForUIJsonWriter.flush();
        postProcessedPDFRecordForUIJsonWriter.close();
        jsonReader.endObject();
        jsonReader.close();
    }
}
