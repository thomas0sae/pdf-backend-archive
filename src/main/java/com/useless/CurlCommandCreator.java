package com.useless;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CurlCommandCreator
{
	private static String cUrl= "http://webcache.googleusercontent.com/search?q=cache:xxxxxxxxxxxx&strip=1";
	public static void main(String[] args) throws Exception
	{
/*		File fileDir12 = new File("curlCommand.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir12), "UTF8"));
		if((cUrl = br.readLine()) == null)
		{
			br.close();
			throw new Exception();
		}*/
	
		try
		{
			File fileDir = new File("Command.txt");
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
			List<String> companyURLList = getCompanyURLsList(args[0]);
			Iterator<String> iter = companyURLList.listIterator();
			while(iter.hasNext())
			{
				String companyURL = iter.next();
				String peopleURL = companyURL.replace("/overview", "/people");
				String contactURL = companyURL.replace("/overview", "/contact");
				peopleURL = cUrl.replaceAll("xxxxxxxxxxxx", peopleURL);
				contactURL = cUrl.replaceAll("xxxxxxxxxxxx", contactURL);
				out.append(companyURL).append("\r\n");
				out.append(peopleURL).append("\r\n");
				out.append(contactURL).append("\r\n");
				out.flush();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	private static List<String> getCompanyURLsList(String fileName)
	{
		File fileDir12 = new File(fileName);

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir12), "UTF8"));
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		List<String> companyURLList = new ArrayList<String>();
		try
		{
			while ((line = br.readLine()) != null)//parsing as text file. No time for xml parsing. Big file. XML parser might parse whole file in one shot.
			{
				int isLocPresent = line.indexOf("<loc>");
				if (isLocPresent == -1)
				{
					continue;
				}
				line = line.replaceAll("<loc>", "").replaceAll("</loc>", "");
				companyURLList.add(line.trim());
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return companyURLList;
	}

}
