package com.cc.pdfparser.elastic;

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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
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

public class CharacterEncodingTest
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
		if (args == null || args.length < 1)
		{
			System.out.println("Insufficient Arguments! Exiting!! ");
			System.exit(0);
		}

		File crawledPDFs = new File("url_test_" + System.currentTimeMillis() + ".json");
		Writer crawledPDFsJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawledPDFs), "UTF8"));

 
		// Set up a local compressed WARC file for reading
		// String fn =
		// "C:\\Users\\easothomas\\Downloads\\CC-MAIN-20171117170336-20171117190336-00000.warc\\CC-MAIN-20171117170336-20171117190336-00000.warc.gz";
		FileInputStream is = new FileInputStream(args[0]);
		ArchiveReader ar = WARCReaderFactory.get(args[0], is, true);

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
						url1 = urii.toString();
						uri = new URI(urii.toString());
						String result = java.net.URLDecoder.decode(url1, "UTF-8");
						crawledPDFsJSON.append(url1).append("\r\n");
						crawledPDFsJSON.append(uri.toString()).append("\r\n");
						crawledPDFsJSON.append(result).append("\r\n");
						crawledPDFsJSON.append("\r\n");
						crawledPDFsJSON.flush();
					}
 
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}
		is.close();
		crawledPDFsJSON.close(); 
	}

	 
}