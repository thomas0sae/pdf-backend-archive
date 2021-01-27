package com.useless.links;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class WarcPdfJsonGenerator
{
	/*
	*//**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 *//*
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

		File crawledPDFs = new File("crawled" + System.currentTimeMillis() + ".json");
		Writer crawledPDFsJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawledPDFs), "UTF8"));

		File linkedPDFs = new File("linked" + System.currentTimeMillis() + ".json");
		Writer linkedPDFsJson = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linkedPDFs), "UTF8"));

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
						url1 = java.net.URLDecoder.decode(urii.toString(), "UTF-8");
						uri = new URI(urii.toString());
						dmn = uri.getHost();
						String topDomain = InternetDomainName.from(dmn).topPrivateDomain().toString();
						topDmn = topDomain;
					}

					if (r.getHeader().getHeaderValue("WARC-Identified-Payload-Type").equals("application/pdf"))
					{
						PostProcessedPDFRecordForUI crawlRecord = new PostProcessedPDFRecordForUI();
						//crawlRecord.setWebPageUrl(url1);
						crawlRecord.setTopDmn(topDmn);
						processCrawledPDF(wr, crawlRecord, url1);
						String flnme = crawlRecord.getImgFilePath();
						if (flnme == null || flnme.trim().isEmpty())
						{
							flnme = url1.substring(url1.lastIndexOf('/') + 1);
							crawlRecord.setFlNm(flnme);
							crawlRecord.setImgFilePath(formatName(flnme));
						}
						String md5 = (new HexBinaryAdapter()).marshal(md.digest(urii.toString().getBytes()));
						// String documentID = generateString();
						// crawledPDFsJSON.append("{\"index\" :
						// {\"_id\":\""+documentID+"\"}}").append("\r\n");
						crawledPDFsJSON.append("{\"index\" : {\"_id\":\"" + md5 + "\"}}").append("\r\n");
						crawledPDFsJSON.append((new Gson()).toJson(crawlRecord)).append("\r\n");
						crawledPDFsJSON.flush(); 
					} else
					{
						LinkedPDFRecord linkedRecord = new LinkedPDFRecord();
						// linkedRecord.setDmn(dmn);
						linkedRecord.setUrl(url1);
						linkedRecord.setTopDmn(topDmn);
						// Map<String, String> pdfLinksMap = new HashMap<String,
						// String>();
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
								link = java.net.URLDecoder.decode(link.toString(), "UTF-8");
								lnkPdfs.add(link + ":::" + java.net.URLDecoder.decode(link1.text(), "UTF-8"));
							}
						}
						if (lnkPdfs.isEmpty() || lnkPdfs.size() > 50)
						{
							continue;
						}

						linkedRecord.setLnkPdfs(lnkPdfs);
						String md5 = (new HexBinaryAdapter()).marshal(md.digest(urii.toString().getBytes()));
						linkedPDFsJson.append("{\"index\" : {\"_id\":\"" + md5 + "\"}}").append("\r\n");
						linkedPDFsJson.append(new Gson().toJson(linkedRecord)).append("\r\n");
						linkedPDFsJson.flush();
					}
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}
		is.close();
		crawledPDFsJSON.close();
		linkedPDFsJson.close();
	}

	private static void processCrawledPDF(WARCRecord wr, PostProcessedPDFRecordForUI crawlRecord, String url)
			throws HttpException, IOException
	{
		Header[] httpHeaders = LaxHttpParser.parseHeaders(wr, "UTF-8");
		for (Header h : httpHeaders)
		{
			if (h.getName().equals("Content-Disposition"))
			{
				try
				{
					String flnme = null;
					String[] filenamesplit = h.getValue().split("filename=");
					if (filenamesplit.length > 1)
					{
						flnme = filenamesplit[1].replace("\"", "").replaceAll(";", "");
						if (flnme.contains(" filename"))
						{
							flnme = flnme.split(" filename")[0];
						}
					} else
					{
						filenamesplit = h.getValue().split("fileName=");
						if (filenamesplit.length > 1)
						{
							flnme = filenamesplit[1].replace("\"", "").replaceAll(";", "");
							if (flnme.contains(" filename"))
							{
								flnme = flnme.split(" filename")[0];
							}
						}
					}
					crawlRecord.setFlNm(flnme);
					crawlRecord.setImgFilePath(formatName(flnme));
				} catch (Exception e)
				{
					System.out.println(h.getValue());
					System.out.println(e.getMessage());
				}
			} else if (h.getName().equals("Last-Modified"))
			{
				SimpleDateFormat parser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz");
				// Mon, 27 Feb 2017 04:44:18 GMT
				Date date;
				try
				{
					date = parser.parse(h.getValue());

					LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					int year = localDate.getYear();
					crawlRecord.setYr(year + "");
				} catch (ParseException e)
				{
					e.printStackTrace();
				}

			}
		}
		long sizeVal = Long.valueOf(wr.getHeader().getHeaderValue("Content-Length").toString());

		//crawlRecord.setdSz(formatSizeValue(sizeVal));
		crawlRecord.setSz(sizeVal);
		InputStream in = (InputStream) wr;
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null)
		{
			if (sCurrentLine.contains("/Type/Pages/Count"))
			{
				crawlRecord.setPgCnt(Integer.valueOf(sCurrentLine.split("/Count ")[1].split("/")[0]));
				break;
			}
		}
		in.close();
		br.close();

	}

	private static String formatName(String flnme)
	{
		flnme = flnme.replaceAll("[-_]"," ").replaceAll("\\s+", " ");
		int fileNameSize = flnme.length();
		if (fileNameSize > 70)
		{
			flnme = flnme.substring(0, 65) + "...pdf";
		}
		return flnme;
	}

	private static String formatSizeValue(long sizeVal)
	{
		double k = sizeVal / 1024.0;
		double m = sizeVal / 1048576.0;
		double g = sizeVal / 1073741824.0;
		double t = sizeVal / 1102732853248.0;
		DecimalFormat dec = new DecimalFormat("0.00");
		String hrSize = "";
		if (t > 1)
		{
			hrSize = dec.format(t).concat(" TB");
		} else if (g > 1)
		{
			hrSize = dec.format(g).concat(" GB");
		} else if (m > 1)
		{
			hrSize = dec.format(m).concat(" MB");
		} 
		else if (k > 1)
		{
			hrSize = dec.format(k).concat(" KB");
		}
		else 
		{
			hrSize = dec.format(sizeVal).concat(" B");
		}
		return hrSize;
	}
*/
}