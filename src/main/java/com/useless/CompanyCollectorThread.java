package com.useless;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

public class CompanyCollectorThread implements Runnable
{
	private List<String> companyToCollect;
	
	
	public CompanyCollectorThread(List<String> companyToCollect)
	{
		this.companyToCollect = companyToCollect;
	}
	@Override
	public void run()
	{
		File fileDir = new File("Cache_"+System.currentTimeMillis()+".csv");
		Writer out = null;
		try
		{
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
		}
		catch (UnsupportedEncodingException | FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Iterator<String> iter = this.companyToCollect.listIterator();
		//String url = "http://webcache.googleusercontent.com/search?q=cache:xxxxxxxxxxxx&strip=1";
		while(iter.hasNext())
		{
			String companyURL = iter.next();
			//collect data
			
			//format the data
			try
			{
			System.out.println("Company URL "+companyURL);
			//String tempURL = url;
			//String overviewData = getOverviewData(tempURL.replaceAll("xxxxxxxxxxxx", companyURL));
			//out.append(companyURL).append("=").append(overviewData).append("\r\n");

				String contactData = getOverviewData(companyURL);
				out.append(contactData).append("\r\n");

			out.flush();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			try
			{
				Thread.sleep(1500);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
 	
	
	private String getOverviewData(String company)
	{
		BufferedReader in1;
		InputStream is1 = null;
		//https://www.aihitdata.com/company/01357EFD/NAMESCONSULTANT/overview
		URL obj1 = null;
		StringBuffer response1 = new StringBuffer();

		try
		{
			obj1 = new URL(company);
		}
		catch (MalformedURLException e1)
		{
			e1.printStackTrace();
		}
		HttpURLConnection con1 = null;
		try
		{
			con1 = (HttpURLConnection) obj1.openConnection();
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
		try
		{
			con1.setRequestMethod("GET");
		}
		catch (ProtocolException e1)
		{
			e1.printStackTrace();
		}
		con1.setRequestProperty("Cookie", "__utma=183319076.2109995852.1473450177.1474826904.1475078063.3; __utmc=183319076; __utmz=183319076.1474826906.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(notprovided); __unam=b14a8e2-15710797644-72d6b7ed-9");
		con1.setRequestProperty("Origin", "http://google.com");
		con1.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		con1.setRequestProperty("Cache-Control", "no-cache");
		con1.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/json,application/xml;q=0.9,image/webp,*/*;q=0.8");
		con1.setRequestProperty("Connection", "keep-alive");
		con1.setRequestProperty("Host", "https://google.com");
		con1.setRequestProperty("Referer", "https://google.com");
		con1.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		con1.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
		try
		{
			is1 = con1.getInputStream();
		}
		catch (UnknownHostException uhe)
		{
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{ 
				e.printStackTrace();
			}
			uhe.printStackTrace();
		}
		catch (java.net.SocketTimeoutException ste)
		{
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{ 
				e.printStackTrace();
			}
			ste.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if(is1 == null)
		{
			return "";
		}
		in1 = new BufferedReader(new InputStreamReader(is1));
		String inputLine1;
		
		try
		{
			while ((inputLine1 = in1.readLine()) != null)
			{
				response1.append(inputLine1);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		con1.disconnect();
		return response1.toString();	
	}
}
