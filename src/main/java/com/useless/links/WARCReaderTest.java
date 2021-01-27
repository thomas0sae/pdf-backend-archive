package com.useless.links;

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
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.httpclient.Header;
import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.archive.io.warc.WARCRecord;
import org.archive.util.LaxHttpParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.net.InternetDomainName;
import com.google.gson.Gson;

/**
 * A raw example of how to process a WARC file using the org.archive.io package.
 * Common Crawl S3 bucket without credentials using JetS3t.
 *
 * @author Stephen Merity (Smerity)
 */
public class WARCReaderTest
{
	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException
	{
		File crawledPDFs = new File("crawled" + System.currentTimeMillis() + ".json");
		Writer crawledPDFsJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawledPDFs), "UTF8"));

		File linkedPDFs = new File("linked" + System.currentTimeMillis() + ".json");
		Writer linkedPDFsJson = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linkedPDFs), "UTF8"));

		// Set up a local compressed WARC file for reading
		String fn = "F:\\everything_pdf\\ccrawl-downloads\\CC-MAIN-20200216182139-20200216212139-00002.warc.gz";
		FileInputStream is = new FileInputStream(fn);
		ArchiveReader ar = WARCReaderFactory.get(fn, is, true);
		MessageDigest md = MessageDigest.getInstance("MD5");
		String md5 = "";
		// Once we have an ArchiveReader, we can work through each of the
		// records it contains 
		for (ArchiveRecord r : ar)
		{
			Map<String, String> pdfDataMap = new HashMap<String, String>();
			WARCRecord wr = (WARCRecord) r;
			if (r.getHeader().getHeaderValue("WARC-Type").equals("response"))
			{
				Object urii = r.getHeader().getHeaderValue("WARC-Target-URI");			
				URI uri;
				try
				{
					if(urii != null)
					{
						pdfDataMap.put("url", urii.toString());
											
						uri = new URI(urii.toString());
						String domain = uri.getHost(); 
						pdfDataMap.put("dmn",domain);
						String topDomain = InternetDomainName.from(domain).topPrivateDomain().toString();
						pdfDataMap.put("topDmn",topDomain);
					}
				} 
				catch (Exception e)
				{
					//e.printStackTrace();
				}
				if (r.getHeader().getHeaderValue("WARC-Identified-Payload-Type").equals("application/pdf"))
				{
					Header[] httpHeaders = LaxHttpParser.parseHeaders(wr, "UTF-8");
					for (Header h : httpHeaders)
					{
						if (h.getName().equals("Content-Disposition"))
						{
							//System.out.println(h.getValue());
							try
							{
								String[] filenamesplit = h.getValue().split("filename=");
								if(filenamesplit.length > 1)
								{	
									pdfDataMap.put("flNm",filenamesplit[1].replace("\"","").replaceAll(";",""));
									//System.out.println(filenamesplit[1].replace("\"","").replaceAll(";",""));
								}
								else 
								{
									filenamesplit = h.getValue().split("fileName=");
									if(filenamesplit.length > 1)
									{
										pdfDataMap.put("flNm",filenamesplit[1].replace("\"","").replaceAll(";",""));
										//System.out.println(filenamesplit[1].replace("\"","").replaceAll(";",""));
									}
								}
							} 
							catch (Exception e)
							{ 
								System.out.println(h.getValue()); 
							}
						}
						else if (h.getName().equals("Last-Modified"))
						{ 
							SimpleDateFormat parser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz");
							//Mon, 27 Feb 2017 04:44:18 GMT
					        Date date;
							try
							{
								date = parser.parse(h.getValue());
								
								LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
								int year  = localDate.getYear();
								pdfDataMap.put("yr", year+"");
								//System.out.println(year);
							} catch (ParseException e)
							{ 
								e.printStackTrace();
							}
							
						}
					}
					pdfDataMap.put("sz", r.getHeader().getHeaderValue("Content-Length").toString());
					
					InputStream in = (InputStream)wr;
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) 
					{
						if(sCurrentLine.contains("/Type/Pages/Count"))
						{
							//System.out.println("sCurrentLine "+sCurrentLine);
							//System.out.println("page count "+sCurrentLine.split("/Count ")[1].split("/")[0]);
							pdfDataMap.put("pgCnt", sCurrentLine.split("/Count ")[1].split("/")[0]);
							break;
						}
					}
					md5 = (new HexBinaryAdapter()).marshal(md.digest(urii.toString().getBytes()));
					//String documentID = generateString();
					//crawledPDFsJSON.append("{\"index\" : {\"_id\":\""+documentID+"\"}}").append("\r\n");
					crawledPDFsJSON.append("{\"index\" : {\"_id\":\""+md5+"\"}}").append("\r\n");
					crawledPDFsJSON.append((new Gson()).toJson(pdfDataMap)).append("\r\n");
					crawledPDFsJSON.flush();
				} 
				else
				{ 
					Map<String, Map<String, String>> htmlParsePdfDataMap = new HashMap<String, Map<String, String>>();
					Map<String, String> pdfLinksMap = new HashMap<String, String>();
					
					byte[] rawData = IOUtils.toByteArray(r, r.available());
 					String content = new String(rawData);
 					
					Document doc = Jsoup.parse(content, "UTF-8");
					Elements links = doc.select("a[href]");
					for (Element link1 : links)
					{
						String link = link1.attr("abs:href");
						if (link.contains(".pdf"))
						{							
							pdfLinksMap.put(link, link1.text());
						}
					}
					if(pdfLinksMap.isEmpty())
					{
						continue;
					}
					htmlParsePdfDataMap.put("lnks", pdfLinksMap);
					htmlParsePdfDataMap.put("meta", pdfDataMap);
					//String documentID = generateString();
					md5 = (new HexBinaryAdapter()).marshal(md.digest(urii.toString().getBytes()));
					linkedPDFsJson.append("{\"index\" : {\"_id\":\""+md5+"\"}}").append("\r\n");
					linkedPDFsJson.append(new Gson().toJson(htmlParsePdfDataMap)).append("\r\n");
					linkedPDFsJson.flush();
				}
			}
		}
		crawledPDFsJSON.close();
		linkedPDFsJson.close();
	}
 
}
 
//System.out.println(r.getHeader().getHeaderFieldKeys());			
/*if (r.getHeader().getHeaderValue("WARC-Type").equals("request"))
{
	
	
	Dont need the request because request directly has the url to pdf
 	Doesnt matter where it comes from
 	If it is some other type also, doesn't care. 
 	Gets redirected to a PDF URL based on the check of the type
 	
 					
	requestWARCRecordID = r.getHeader().getHeaderValue("WARC-Record-ID").toString();
	pdfDataMap.put("pdfType", "requestPDF");
	pdfDataMap.put("WARC-Record-ID", "");
	pdfDataMap.put("", "");
	Header[] httpHeaders1 = LaxHttpParser.parseHeaders(wr, "UTF-8");
	for (Header h : httpHeaders1)
	{
		if (h.getName().equals("Host"))
		{
			//System.out.println("****************************");
			System.out.println("Host --  " + h.getValue()); 
		}
		continue;
	}
}*/