package com.cc.pdfparser.elastic;

public class CrawledJsonFormatParser
{
/*	public static void processJsonFilesInFolder(final File folder) throws IOException
	{
		for (final File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
			{
				processJsonFilesInFolder(fileEntry);
			} 
			else if(fileEntry.getName().startsWith("linked"))
			{
				File linked_formatted_json = new File(fileEntry.getName()+"_formatted.json");
				Writer linkedPDFsJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(linked_formatted_json), "UTF8"));
 
				try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileEntry), "UTF8")))
				{
					String line = "";
					while(( line = br.readLine()) != null)
					{						
						if(line.contains("\"index\" : {\"_id\":"))
						{//{"index" : {"_id":"7CF915339EB5E136D2FCE8B2C14F2719"}}
							linkedPDFsJSON.append(line).append("\r\n");
							linkedPDFsJSON.flush();
						}
						else
						{
							GsonBuilder b = new GsonBuilder(); 
						    Gson gson = b.create();
						    LinkedPDFRecord linkedOrig = gson.fromJson(line, LinkedPDFRecord.class);
						    LinkedPDFRecord linkedToWritePDF = new LinkedPDFRecord();
							linkedToWritePDF.setTopDmn(linkedOrig.getTopDmn()); 
							String url = java.net.URLDecoder.decode(linkedOrig.getUrl(), "UTF-8");
							linkedToWritePDF.setUrl(java.net.URLDecoder.decode(url, "UTF-8"));
							
							Set<String> newLinkPDFs = new HashSet<String>();
							Set<String> lnkPDFs = linkedOrig.getLnkPdfs();
							for(String linkPDF : lnkPDFs)
							{
								String[] urlParts = linkPDF.split(":::");
								String url1 = java.net.URLDecoder.decode(urlParts[0], "UTF-8");
								newLinkPDFs.add(url1 + (((urlParts.length > 1) && !urlParts[1].trim().isEmpty())?":::"+urlParts[1]:""));
							}
							linkedToWritePDF.setLnkPdfs(newLinkPDFs);							
							linkedPDFsJSON.append((new Gson()).toJson(linkedToWritePDF)).append("\r\n");
							linkedPDFsJSON.flush(); 
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				linkedPDFsJSON.close();
			}
			else
			{
				System.out.println(fileEntry.getName());
				File crawled_formatted_json = new File(fileEntry.getName()+"_formatted.json");
				Writer crawledPDFsJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawled_formatted_json), "UTF8"));
 
				try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileEntry), "UTF8")))
				{
					String line = "";
					while(( line = br.readLine()) != null)
					{						
						if(line.contains("\"index\" : {\"_id\":"))
						{//{"index" : {"_id":"7CF915339EB5E136D2FCE8B2C14F2719"}}
							crawledPDFsJSON.append(line).append("\r\n");
							crawledPDFsJSON.flush();
							//continue;
						}
						else
						{
							//{"topDmn":"eyeballbooks.us","url":"http://2xp.eyeballbooks.us/no-baths-at-camp-hardback_mnbx2.pdf","sz":10313,"dSz":"10.07 MB","pgCnt":0,"yr":"2017","flNm":"no-baths-at-camp-hardback_mnbx2.pdf.pdf","dFlNm":"no-baths-at-camp-hardback_mnbx2.pdf.pdf"}
							GsonBuilder b = new GsonBuilder(); 
						    Gson gson = b.create();
						    CrawledPDFRecord crawledOrig = gson.fromJson(line, CrawledPDFRecord.class);
							CrawledPDFRecord crawledToWritePDF = new CrawledPDFRecord();
							crawledToWritePDF.setTopDmn(crawledOrig.getTopDmn()); 
							String url = java.net.URLDecoder.decode(crawledOrig.getWebPageUrl(), "UTF-8");
							crawledToWritePDF.setWebPageUrl(java.net.URLDecoder.decode(url, "UTF-8"));
							
							String fileName = crawledOrig.getFlNm();
							if(fileName == null || fileName.equals(""))
							{
								fileName = url.substring(url.lastIndexOf('/') + 1);
								crawledToWritePDF.setFlNm(fileName);
								crawledToWritePDF.setImgFilePath(formatName(fileName));
							}
							else
							{
								crawledToWritePDF.setImgFilePath(formatName(java.net.URLDecoder.decode(crawledOrig.getFlNm(),
										"UTF-8")));
								crawledToWritePDF.setFlNm(java.net.URLDecoder.decode(crawledOrig.getFlNm(),
										"UTF-8"));
							}
							crawledToWritePDF.setPgCnt(crawledOrig.getPgCnt());
							crawledToWritePDF.setSz(crawledOrig.getSz());
							//crawledToWritePDF.setdSz(formatSizeValue(crawledOrig.getSz()));
							crawledToWritePDF.setYr(crawledOrig.getYr());
							
							crawledPDFsJSON.append((new Gson()).toJson(crawledToWritePDF)).append("\r\n");
							crawledPDFsJSON.flush(); 
						}
					}
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				crawledPDFsJSON.close();
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		if (args == null || args.length < 1)
		{
			System.out.println("Insufficient Arguments! Exiting!! ");
			System.exit(0);
		}
		
		processJsonFilesInFolder(new File(args[0]));
		
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
	
	private static String formatName(String flnme)
	{
		flnme = flnme.replaceAll("[-_]"," ").replaceAll("\\s+", " ");
		int fileNameSize = flnme.length();
		if (fileNameSize > 70)
		{
			flnme = flnme.substring(0, 65) + "...pdf";
		}
		return flnme;
	}*/
}
