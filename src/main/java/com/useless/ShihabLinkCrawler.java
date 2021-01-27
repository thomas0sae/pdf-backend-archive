package com.useless;

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

public class ShihabLinkCrawler
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
		}
		catch (NoSuchAlgorithmException nse)
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

		File surveymonkeyStream1 = new File("surveyMon" + System.currentTimeMillis() + ".csv");
		Writer surveyMonkWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(surveymonkeyStream1), "UTF8"));
 
		File zendeskStream = new File("zendesk" + System.currentTimeMillis() + ".csv");
		Writer zenDeskWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(zendeskStream), "UTF8"));

		File shihabTypeFormLinks = new File("TypeForm" + System.currentTimeMillis() + ".csv");
		Writer shihabTypeFormWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(shihabTypeFormLinks), "UTF8"));

		File shihabSurveyGizmoLinks = new File("SurveyGizmo" + System.currentTimeMillis() + ".csv");
		Writer shihabSurveyGizmoWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(shihabSurveyGizmoLinks), "UTF8"));

		File shihabJunkLinks = new File("Junk_Links" + System.currentTimeMillis() + ".csv");
		Writer shihabJunkLinksWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(shihabJunkLinks), "UTF8"));

		// survicate.com
		// typeform.com
		// surveymonkey.com
		// surveygizmo.com
		// polldaddy.com
		/// qualtrics.com
		// getfeedback.com
		//// formsite.com
		// qualaroo.com
		// pulseinsights.com
		// webengage.com
		// asknicely.com
		// honestlyapp.com
		// responster.com
		// limesurvey.org
		// kwiksurveys.com
		// surveynuts.com
		// surveyplanet.com
		// surveylegend.com
		// clientheartbeat.com
		// mopinion.com
		// questionpro.com
		// confirmit.com
		// checkbox.com
		// fluidsurveys.com
		// sogosurvey.com
		// surveyplanet.com
		// survio.com

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

					if (url1.contains("/hc/en-us"))
					{
						zenDeskWriter.append("/hc/en-us").append("||");
						zenDeskWriter.append(url1).append("||");
						zenDeskWriter.append(topDmn).append("||");
						zenDeskWriter.append("no_parent").append("\r\n");
						zenDeskWriter.flush();
					}
					else if (url1.contains("surveymonkey.com/r/"))
					{
						surveyMonkWriter.append("surveymonkey.com/r/").append("||");
						surveyMonkWriter.append(url1).append("||");
						surveyMonkWriter.append(topDmn).append("||");
						surveyMonkWriter.append("no_parent").append("\r\n");
						surveyMonkWriter.flush();
					}
					else if (url1.contains("typeform.com/to"))
					{
						shihabTypeFormWriter.append("typeform.com/to").append("||");
						shihabTypeFormWriter.append(url1).append("||");
						shihabTypeFormWriter.append(topDmn).append("||");
						shihabTypeFormWriter.append("no_parent").append("\r\n");
						shihabTypeFormWriter.flush();
					}
					else if (url1.contains("survicate.com") || url1.contains("typeform.com")
							|| url1.contains("freshdesk.com")
							|| url1.contains("surveymonkey.com") || url1.contains("surveygizmo.com")
							|| url1.contains("polldaddy.com") || url1.contains("qualtrics.com")
							|| url1.contains("getfeedback.com") || url1.contains("formsite.com")
							|| url1.contains("qualaroo.com") || url1.contains("pulseinsights.com")
							|| url1.contains("webengage.com") || url1.contains("asknicely.com")
							|| url1.contains("honestlyapp.com") || url1.contains("responster.com")
							|| url1.contains("limesurvey.org") || url1.contains("kwiksurveys.com")
							|| url1.contains("surveynuts.com") || url1.contains("surveyplanet.com")
							|| url1.contains("surveylegend.com") || url1.contains("clientheartbeat.com")
							|| url1.contains("mopinion.com") || url1.contains("questionpro.com")
							|| url1.contains("confirmit.com") || url1.contains("checkbox.com")
							|| url1.contains("fluidsurveys.com") || url1.contains("sogosurvey.com")
							|| url1.contains("surveyplanet.com") || url1.contains("survio.com"))
					{
						shihabJunkLinksWriter.append(url1).append("||");
						shihabJunkLinksWriter.append(topDmn).append("||");
						shihabJunkLinksWriter.append("no_parent").append("\r\n");
						shihabJunkLinksWriter.flush();
					}
					// Map<String, String> pdfLinksMap = new HashMap<String,
					// String>();

					byte[] rawData = IOUtils.toByteArray(r, r.available());
					String content = new String(rawData);

					Document doc = Jsoup.parse(content, "UTF-8");
					Elements links = doc.select("a[href]");
					for (Element link1 : links)
					{
						String link = link1.attr("abs:href");

						if (link.contains("/hc/en-us"))
						{
							zenDeskWriter.append("/hc/en-us").append("||");
							zenDeskWriter.append(link).append("||");
							zenDeskWriter.append(topDmn).append("||");
							zenDeskWriter.append(url1).append("\r\n");
							zenDeskWriter.flush();
						}
						else if (link.contains("surveymonkey.com/r/"))
						{
							surveyMonkWriter.append("surveymonkey.com/r/").append("||");
							surveyMonkWriter.append(link).append("||");
							surveyMonkWriter.append(topDmn).append("||");
							surveyMonkWriter.append(url1).append("\r\n");
							surveyMonkWriter.flush();
						}
						else if (link.contains("typeform.com/to"))
						{
							shihabTypeFormWriter.append("typeform.com/to").append("||");
							shihabTypeFormWriter.append(link).append("||");
							shihabTypeFormWriter.append(topDmn).append("||");
							shihabTypeFormWriter.append(url1).append("\r\n");
							shihabTypeFormWriter.flush();
						}
						else if (link.contains("survicate.com") || link.contains("typeform.com")
								|| link.contains("freshdesk.com")
								|| link.contains("surveymonkey.com") || link.contains("surveygizmo.com")
								|| link.contains("polldaddy.com") || link.contains("qualtrics.com")
								|| link.contains("getfeedback.com") || link.contains("formsite.com")
								|| link.contains("qualaroo.com") || link.contains("pulseinsights.com")
								|| link.contains("webengage.com") || link.contains("asknicely.com")
								|| link.contains("honestlyapp.com") || link.contains("responster.com")
								|| link.contains("limesurvey.org") || link.contains("kwiksurveys.com")
								|| link.contains("surveynuts.com") || link.contains("surveyplanet.com")
								|| link.contains("surveylegend.com") || link.contains("clientheartbeat.com")
								|| link.contains("mopinion.com") || link.contains("questionpro.com")
								|| link.contains("confirmit.com") || link.contains("checkbox.com")
								|| link.contains("fluidsurveys.com") || link.contains("sogosurvey.com")
								|| link.contains("survio.com"))
						{
							shihabJunkLinksWriter.append(link).append("||");
							shihabJunkLinksWriter.append(topDmn).append("||");
							shihabJunkLinksWriter.append(url1).append("\r\n");
							shihabJunkLinksWriter.flush();
						}

					}
				}
				catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}
		is.close();
		zenDeskWriter.close();
		shihabJunkLinksWriter.close();
		shihabSurveyGizmoWriter.close();
		shihabTypeFormWriter.close();
		surveyMonkWriter.close();
	}
}