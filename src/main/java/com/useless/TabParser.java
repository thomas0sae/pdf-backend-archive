package com.useless;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class TabParser
{
	public static void main(String[] args) throws IOException
	{
		File folder = new File("C:/Work/commoncrawl/extracted");
		String fileName = "C:/Work/commoncrawl/cleaned/";		
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isFile()) 
			{
				System.out.println("File " + listOfFiles[i].getName());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(listOfFiles[i]), "UTF8"));
				String line;
				File fileDir = new File(fileName+"file"+i+".csv");
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));
				while ((line = br.readLine()) != null)
				{
					String[] tabbedArray = line.split("\t");
					int length = tabbedArray.length;
					for(int j = 1; j < length;j++)
					{
						try
						{
							//if(tabbedArray[j].contains("...") || tabbedArray[j].contains("@yahoo.com") ||  tabbedArray[j].contains("@yahoo.com"))
							out.append(tabbedArray[j]).append("|/|").append(tabbedArray[0]).append("\r\n");
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}					
					}
				}
				br.close();
				out.flush();
				out.close();
		    }
		}
	}
}
