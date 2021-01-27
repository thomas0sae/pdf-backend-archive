package com.cc.pdfparser.elastic;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Read a PDF file from the internet. Save it locally. If
 *
 *
 *
 *
 * { randomID fileName-fromURL fullURL firstPageText domain pageCount title date
 * size keywords imageName and path
 * 
 * ---------------------- domain - from URL
 * 
 *
 */
public class App
{
	private static void parseGZipAndGetPDFLink(String args[]) throws Exception
	{
		if (args == null || args.length < 1)
		{
			System.out.println("Insufficient Arguments! Exiting!! ");
			System.exit(0);
		}
		File indexLoadFile = new File("cdx-00000" + System.currentTimeMillis() + ".json");
		Writer indexLoadJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexLoadFile), "UTF8"));

		String indexGzipFileName = args[0];
		InputStream gzipStream = new GZIPInputStream(new FileInputStream(indexGzipFileName));
		BufferedReader br = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));

		String line = null;
		while ((line = br.readLine()) != null)
		{
			String documentID = generateString();
			Properties pdfJsonProps = new Properties();
			try
			{
				String lineVal = line.substring(line.lastIndexOf("{"));
				HashMap<String, String> map = new Gson().fromJson(lineVal, new TypeToken<HashMap<String, String>>()
				{
				}.getType());
				String pdfURLFromIndex = map.get("url");

				pdfJsonProps.setProperty("pdfURL", pdfURLFromIndex);

				if (map.get("mime").equals("application/pdf") || map.get("mime-detected").equals("application/pdf"))
				{
					String pdfFileName = null;

					pdfFileName = downloadPDF(pdfURLFromIndex, pdfJsonProps, documentID);
					if (Objects.isNull(pdfFileName))
					{
						// throw new NullPointerException("fileName shouldn't be
						// null");
						continue;
					}
					File localFile = new File(pdfFileName);
					long bytes = localFile.length();
					pdfJsonProps.setProperty("sizByt", bytes + "");
					try (PDDocument pdDoc = PDDocument.load(localFile))
					{
						int count = pdDoc.getNumberOfPages();
						if (count < 5)
						{
							//System.out.println("URL " + pdfURLFromIndex);
							//System.out.println("PageCount " + count);
							pdDoc.close();
							Files.deleteIfExists(Paths.get(pdfFileName));
							continue;
						}
						pdfJsonProps.setProperty("fPgTxt", getDataAsString(pdDoc));
						
						getMetaData(pdDoc, pdfJsonProps);
						saveImage(pdfFileName, pdDoc, pdfJsonProps);
						pdDoc.close();
					} 
					catch (IOException e)
					{
						//System.out.println(e.getMessage());
						e.printStackTrace();
					}
					Files.deleteIfExists(Paths.get(pdfFileName));
					// System.out.println(pdfJsonProps);
					indexLoadJSON.append("{\"index\" : {\"_id\":\"" + documentID + "\"}}").append("\r\n");
					indexLoadJSON.append(new Gson().toJson(pdfJsonProps)).append("\r\n");
					indexLoadJSON.flush();
				}
			} 
			catch (Exception e)
			{
				System.out.println("line............ " + line);
				System.out.println(e.getCause());
				//e.printStackTrace();
			}
		}
		br.close();
		indexLoadJSON.close();
	}

	private static void getMetaData(PDDocument pdDoc, Properties pdfJsonProps)
	{
		PDDocumentInformation info = pdDoc.getDocumentInformation();

/*		System.out.println("Author=" + info.getAuthor());
		System.out.println("Subject=" + info.getSubject());
		System.out.println("Keywords=" + info.getKeywords());
		System.out.println("Creator=" + info.getCreator());
		System.out.println("Producer=" + info.getProducer());
		System.out.println("Creation Date=" + info.getCreationDate());
		System.out.println("Modification Date=" + info.getModificationDate());
		System.out.println("Trapped=" + info.getTrapped());
*/
		String ttl = info.getTitle();
		if (Objects.nonNull(ttl) && !ttl.isEmpty())
		{
			pdfJsonProps.setProperty("ttl", ttl);
		}
		String sbj = info.getSubject();
		if (Objects.nonNull(sbj) && !sbj.isEmpty())
		{
			pdfJsonProps.setProperty("sbj", sbj);
		}
		Calendar cal = info.getCreationDate();
		if (Objects.nonNull(cal))
		{
			int crtYr = cal.get(Calendar.YEAR);
			pdfJsonProps.setProperty("crtYr", crtYr + "");
		}
		int count = pdDoc.getNumberOfPages();
		pdfJsonProps.setProperty("pgCnt", count + "");
	}

	private static void saveImage(String fileName, PDDocument pdDoc, Properties pdfJsonProps) throws IOException
	{
		PDFRenderer pdfRenderer = new PDFRenderer(pdDoc);
		BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 70, ImageType.RGB);
		// suffix in filename will be used as the file format
		String imageFileName = fileName + ".png";
		pdfJsonProps.setProperty("imgN", imageFileName);
		ImageIOUtil.writeImage(bim, imageFileName, 70);
		pdDoc.close();
	}

	private static String getDataAsString(PDDocument pdDoc)
	{
		try
		{
			PDFTextStripper stripper = new PDFTextStripper();
			// stripper.setLineSeparator("\n");
			stripper.setAddMoreFormatting(true);
			stripper.setStartPage(1);
			stripper.setEndPage(1);

			Optional<String> data2 = Optional.of(stripper.getText(pdDoc).trim().replaceAll("\n", " ").replaceAll("\r", " ")
					.replaceAll("[^\\w\\s]", "").replaceAll("_", "").replaceAll("\t", " ").replaceAll(" +", " "));
			String nWords="";
			if (data2.isPresent())
			{
				String textData = data2.get();
				if (!textData.isEmpty())
				{
				    String [] arr = textData.split("\\s+"); 
			        int n = 20; // NUMBER OF WORDS THAT YOU NEED
			        if(arr.length > n)
			        {
			        	// concatenating number of words that you required
				        for(int i=0; i<n  ; i++){
				             nWords = nWords + " " + arr[i] ;         
				        }
			        }
			        else
			        {
			        	nWords = textData;
			        }
			        if(nWords.length() > 200)
			        {
			        	nWords = nWords.substring(0, 200);
			        }
				}
			}
			return nWords;
		} catch (IOException e)
		{
			System.out.println(e.getCause());
			//e.printStackTrace();
			return "";
		}
	}

	/*
	 * public static void extractMetaData(String fileName) throws IOException,
	 * SAXException, TikaException { BodyContentHandler handler = new
	 * BodyContentHandler(1000); AutoDetectParser parser = new
	 * AutoDetectParser(); Metadata metadata = new Metadata(); try (InputStream
	 * stream = App.class.getResourceAsStream(fileName)) { parser.parse(stream,
	 * handler, metadata); System.out.println(handler.toString()); } }
	 */

	public static void main(String[] args) throws Exception
	{
		/*
		 * if (args.length < 1) {
		 * System.out.println("Full File Path has to be specified ");
		 * System.exit(0); }
		 */
		parseGZipAndGetPDFLink(args);

		// String textString = parseToPlainText(pdfFileName);
		// System.out.println(textString);
	}

	private static String downloadPDF(String pdfURL, Properties pdfJsonProps, String documentID) throws Exception
	{
		URL url = new URL(pdfURL);
		String pdfURLFileName = FilenameUtils.getName(url.getPath());
		if (Objects.nonNull(pdfURLFileName) && !pdfURLFileName.isEmpty())
		{
			pdfJsonProps.setProperty("pdfInetN", pdfURLFileName);
		}

		String localPDFName = documentID + ".pdf";
		try (InputStream in = url.openStream())
		{
			Files.copy(in, Paths.get(localPDFName), StandardCopyOption.REPLACE_EXISTING);
		}
		return localPDFName;
	}

	private static String generateString()
	{
		return UUID.randomUUID().toString();
	}

	/*
	 * public static String parseToPlainText(String fileName) throws
	 * IOException, SAXException, TikaException { BodyContentHandler handler =
	 * new BodyContentHandler();
	 * 
	 * AutoDetectParser parser = new AutoDetectParser(); Metadata metadata = new
	 * Metadata(); System.out.println(fileName); try (InputStream stream =
	 * App.class.getResourceAsStream(fileName)) { parser.parse(stream, handler,
	 * metadata); return handler.toString(); } }
	 */
}
