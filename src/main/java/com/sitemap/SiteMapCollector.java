package com.sitemap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class SiteMapCollector
{
	public static void main(String[] args) throws Exception
	{

		try
		{
			BufferedReader in1;
			InputStream is1;
			File fileDir;
			Writer out;
			for (int i = 79; i < 350; i++)
			{
				fileDir = new File("sitemap" + i + ".xml");
				System.out.println("Sitemap" + i + " started");
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
				URL obj1 = new URL("https://www.aihitdata.com/sitemap" + i + ".xml");
				HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();
				// optional default is GET
				con1.setRequestMethod("GET");
				con1.setRequestProperty("Origin", "http://subscriber.hoovers.com");
				con1.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
				con1.setRequestProperty("Cache-Control", "max-age=0");
				con1.setRequestProperty("Accept", "*/*");
				con1.setRequestProperty("connection", "keep-alive");
				con1.setRequestProperty("Host", "http://subscriber.hoovers.com");
				con1.setRequestProperty("Referer", "http://beta.hgdata.com/simple_count");
				con1.setRequestProperty("X-Requested-With", "XMLHttpRequest");
				con1.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
				is1 = con1.getInputStream();
				in1 = new BufferedReader(new InputStreamReader(is1));
				String inputLine1;
				StringBuffer response1 = new StringBuffer();
				while ((inputLine1 = in1.readLine()) != null)
				{
					response1.append(inputLine1);
				}
				System.out.println("Sitemap"+i+" Collection is over");
				con1.disconnect();
				out.append(response1.append("\r\n")).toString();
				out.flush();
			}
		}
		catch (Exception e)
		{

		}
	}
}
