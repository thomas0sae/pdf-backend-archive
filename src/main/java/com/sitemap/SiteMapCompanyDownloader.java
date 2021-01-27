package com.sitemap;

import com.useless.CompanyCollectorThread;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SiteMapCompanyDownloader
{
	public static void main(String[] args)
	{

		// System.out.println(httpProps);
		int argsSize = args.length;
		if (argsSize < 1)
		{
			System.out.println("Error - Pass the input file as the first argument ");
			System.exit(1);
		}

		/*
		 * 1. Read the xml file which is passed as argument
		 * 2. Read a number which is the number of threads to collect data
		 * 3. Parse the file and load the companies list to a HashMap - This will eat a lot of memory
		 * 4. Divide the Map to the threads to download
		 * 5. Save the html  
		 */
		
		//1. Read the xml file which is passed as argument
		try
		{
			
			List<String> companyURLList = getCompanyURLsList(args[0]);
		/*	int companiesCount = companyURLList.size();
			System.out.println("companiesCount "+companiesCount);
			int threadCount = companiesCount/5; //Hard coding 5 threads
			System.out.println("threadCount "+threadCount);
			int remainingCompanies = companiesCount % 5;
			System.out.println("remainingCompanies "+remainingCompanies);
			int counter = 0;
			int loopCounter = 0;
		*///	while(loopCounter < 5)
			{
				//System.out.println("This thread will run from "+ (counter) +" to "+(counter + threadCount));
				//List<String> listForThreads = new ArrayList<String>(companyURLList.subList(counter, counter + threadCount));
				//counter = counter + threadCount;
				//System.out.println("Thread operating on the list size "+listForThreads.size());
				Thread t = new Thread(new CompanyCollectorThread(companyURLList));
				t.start();
				//loopCounter++;
			}
			//if(remainingCompanies != 0)
			/*{
				List<String> listForThreads = new ArrayList<String>(companyURLList.subList(companiesCount - remainingCompanies, companiesCount));
				Thread t = new Thread(new CompanyCollectorThread(listForThreads));
				t.start();
			}*/
		}
		catch(Exception e)
		{
			
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
