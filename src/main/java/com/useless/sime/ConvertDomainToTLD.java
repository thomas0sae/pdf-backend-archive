package com.useless.sime;

import java.io.*;

import com.google.common.net.InternetDomainName;

public class ConvertDomainToTLD
{
	private static final String filePath = "C:\\Users\\easothomas\\Desktop\\CCRAWL_EMAIL_TEMP_URL_DOMAIN_Dump.csv\\CCRAWL_EMAIL_TEMP_URL_DOMAIN_Dump.csv";

	public ConvertDomainToTLD()
	{
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) 
	{

		File f = new File(filePath);
		InputStream is = null;
		try
		{
			is = new FileInputStream(f);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		@SuppressWarnings("resource")
		BufferedReader bufferIn = new BufferedReader(new InputStreamReader(is));
		 
		String line = "";
		int lineCount = 0;
		String topDomain = "";
			try
			{
				while ( (line = bufferIn.readLine()) != null)
				{	
					System.out.println(line);	
					
					try
					{
						topDomain = InternetDomainName.from(line).topPrivateDomain().toString();
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(line+" ******** "+topDomain);
					lineCount++;
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{ 
				e.printStackTrace();
			}
			System.out.println(lineCount);
	} 
}
