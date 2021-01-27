package com.pdf.warc.ccrawl.processor;

import com.google.common.net.InternetDomainName;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReaderFactory;
import org.archive.io.warc.WARCRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class WarcPdfDomainAndLinksGenerator
{
	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private static MessageDigest md;
	static
	{
		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nse)
		{
			nse.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException
	{
		String fn = "";
		if (args != null && args.length > 0)
		{
			fn = args[0];
		}
		else
		{
			fn = "F:\\everything_pdf\\ccrawl-downloads\\CC-MAIN-20200216182139-20200216212139-00002.warc.gz";
		}
		File fnew = new File(fn);
		File crawledPDFLinks = new File(fnew.getName() + ".json");
		Writer crawledPDFsLinkJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawledPDFLinks), "UTF8"));

	// Set up a local compressed WARC file for reading

	//TODO - Implement a URL download and parse module
		//FileInputStream is = new FileInputStream(args[0]);
		FileInputStream is = new FileInputStream(fn);
		ArchiveReader ar = WARCReaderFactory.get(fn, is, true);

		// Once we have an ArchiveReader, we can work through each of the
		// records it contains
		for (ArchiveRecord r : ar)
		{
			String url1 = "";
			String topDmn = "";
			String dmn = "";
			WARCRecord wr = (WARCRecord) r;
			if (r.getHeader().getHeaderValue("WARC-Type").equals("response"))
			{
				Object urii = r.getHeader().getHeaderValue("WARC-Target-URI");
				URI uri;
				try
				{
					if (urii != null)
					{
						url1 = java.net.URLDecoder.decode(urii.toString(), "UTF-8");
						uri = new URI(urii.toString());
						dmn = uri.getHost();
						String topDomain = InternetDomainName.from(dmn).topPrivateDomain().toString();
						topDmn = topDomain;
					}
					if (r.getHeader().getHeaderValue("WARC-Identified-Payload-Type").equals("application/pdf"))
					{

						PDFLinkRecordFromCCrawl recordFromCCrawl = new PDFLinkRecordFromCCrawl();
						recordFromCCrawl.setWebPageUrl(url1);
						recordFromCCrawl.setPdfFullURL(url1);
						crawledPDFsLinkJSON.append((new Gson()).toJson(recordFromCCrawl)).append("\r\n");
						crawledPDFsLinkJSON.flush();
					}
					else
					{
						PDFLinkRecordFromCCrawl pdfLinkRecordFromCCrawl = new PDFLinkRecordFromCCrawl();
						pdfLinkRecordFromCCrawl.setWebPageUrl(url1);
						Set<String> lnkPdfs = new HashSet<String>();

						byte[] rawData = IOUtils.toByteArray(r, r.available());
						String content = new String(rawData);
						Document doc = Jsoup.parse(content, "UTF-8");
						Elements links = doc.select("a[href]");
						for (Element link1 : links)
						{
							String link = link1.attr("abs:href");
							if (link.contains(".pdf"))
							{
								String linkText = link1.text();
								if(!linkText.contains(".pdf") && !linkText.startsWith("http") && !link.equalsIgnoreCase(linkText)) {
									pdfLinkRecordFromCCrawl.setLinkText(linkText);
								}
								link = java.net.URLDecoder.decode(link.toString(), "UTF-8");
								pdfLinkRecordFromCCrawl.setPdfFullURL(link);
								crawledPDFsLinkJSON.append(new Gson().toJson(pdfLinkRecordFromCCrawl)).append("\r\n");
							}
						}
						crawledPDFsLinkJSON.flush();
					}
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}
		is.close();
		crawledPDFsLinkJSON.close();
	}
}