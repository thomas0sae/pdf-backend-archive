package com.sitemap;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SiteMapParserAndDownloader
{
	public static void main(String argv[]) {

	    try {

		File fXmlFile = new File(argv[0]);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
				
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				
		NodeList nList = doc.getElementsByTagName("url");
				
		System.out.println("----------------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
					
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			String loc  = "";
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				loc = eElement.getElementsByTagName("loc").item(0).getTextContent();
				System.out.println("loc : " + loc);
			}
			if(loc != "" && loc.equalsIgnoreCase("http://www.ebook777.com"))
			{
				continue;
			}
			
			String name = loc.substring(loc.indexOf("com/")+4, loc.lastIndexOf("/"));
			System.out.println("name : " + name);
			File fileDir = new File("post-sitemap0"+File.separator+name);
			fileDir.mkdirs();
			saveInternetFileOnDisk(loc, fileDir+File.separator+name+".html");
			
			NodeList nList1 = ((Element)nNode).getElementsByTagName("image:image");
			for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) {

				Node nNode1 = nList1.item(temp1);
						
				System.out.println("\nCurrent Element :" + nNode1.getNodeName());
						
				if (nNode1.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement1 = (Element) nNode1;

					System.out.println("imageloc : " + eElement1.getElementsByTagName("image:loc").item(0).getTextContent());
					File imgfile1 = new File(fileDir+File.separator+name+".jpg");
					saveFileOnDisk(eElement1.getElementsByTagName("image:loc").item(0).getTextContent(), fileDir+File.separator+name+".jpg");
					File txtfile1 = new File(fileDir+File.separator+name+".txt");
					System.out.println("imagecaption : " + eElement1.getElementsByTagName("image:caption").item(0).getTextContent());
					
				}
			}
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	  }
	
	private static void saveFileOnDisk(String url, String fileToSave)
	{
		try
		{

			FileOutputStream fos = new FileOutputStream(new File(fileToSave));
			
			URL obj1 = new URL(url);
			HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
			// optional default is GET
			con1.setRequestMethod("GET");
			con1.setRequestProperty("Origin", "https://google.com");
			con1.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
			con1.setRequestProperty("Cache-Control", "max-age=0");
			con1.setRequestProperty("Accept", "*/*");
			con1.setRequestProperty("connection", "keep-alive");
			con1.setRequestProperty("Host", "https://google.com");
			con1.setRequestProperty("Referer", "https://google.com");
			con1.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			con1.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
			InputStream in = con1.getInputStream();
			System.setProperty("http.agent", "Chrome");
			int length = -1;
			byte[] buffer = new byte[1024];// buffer for portion of data from connection
			while ((length = in.read(buffer)) > -1) {
			    fos.write(buffer, 0, length);
			}
			fos.close();
			in.close(); 
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void saveInternetFileOnDisk(String url, String fileToSave) throws Exception
	{
		File file1 = new File(fileToSave);
		Writer out1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1), "UTF8"));


		URL obj1 = new URL(url);
		HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
		// optional default is GET
		con1.setRequestMethod("GET");
		con1.setRequestProperty("Origin", "https://google.com");
		con1.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		con1.setRequestProperty("Cache-Control", "max-age=0");
		con1.setRequestProperty("Accept", "*/*");
		con1.setRequestProperty("connection", "keep-alive");
		con1.setRequestProperty("Host", "https://google.com");
		con1.setRequestProperty("Referer", "https://google.com");
		con1.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		con1.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
		BufferedReader in1;
		InputStream is1;
		is1 = con1.getInputStream();
		in1 = new BufferedReader(new InputStreamReader(is1));
		String inputLine1;
		StringBuffer response1 = new StringBuffer();
		while ((inputLine1 = in1.readLine()) != null)
		{
			response1.append(inputLine1);
		}
		con1.disconnect();
		out1.append(response1.toString());
		out1.flush();
 
		org.jsoup.nodes.Document doc = Jsoup.parse(response1.toString(), "UTF-8");
		Elements links = doc.select("a[href]");
		String link = "";
		for (org.jsoup.nodes.Element link1 : links)
		{
			link = link1.attr("abs:href");
			if (link.contains(".pdf") && link.contains("05367.com"))
			{
				link = java.net.URLDecoder.decode(link.toString(), "UTF-8");
				saveFileOnDisk(link, "book.pdf");
			}
		}
		System.out.println(" link " + link);
		
	}
	
}
