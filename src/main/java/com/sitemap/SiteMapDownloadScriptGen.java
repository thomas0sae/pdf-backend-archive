package com.sitemap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SiteMapDownloadScriptGen
{
	public static String[] urlString = new String[]
	{ 		"http://www.ebook777.com/post-sitemap"
	};

	public static void main(String[] args) throws IOException
	{
		String s1 = "#!/bin/sh";
		File company = new File("runSiteMap.bat");
		Writer companyOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(company), "UTF8"));
		companyOut.append(s1).append("\r\n");
		companyOut.flush();
		for (String urlOne : urlString)
		{
			String gzFile = urlOne.substring(urlOne.lastIndexOf("/")+1, urlOne.length());
			String createDir = "mkdir "+gzFile;
			String intoDir = "cd "+gzFile;
			companyOut.append(createDir).append("\r\n");
			companyOut.append(intoDir).append("\r\n");
			companyOut.flush();
			
			for (int i = 0; i < 10; i++)
			{
				String s = "wget "+urlOne+ i + ".xml";
				
				String s2 = "java -cp ../elastic-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.cc.pdfparser.elastic.WarcPdfJsonGenerator "+gzFile+"00" + i + ".warc.gz";
				String s3 = "rm -f "+gzFile+"00" + i + ".warc.gz";
				
				companyOut.append(s).append("\r\n");
				//companyOut.append(s2).append("\r\n");
				//companyOut.append(s3).append("\r\n");
				companyOut.flush();
			}
			for (int i = 10; i < 100; i++)
			{
				String s = "wget "+urlOne+ i + ".xml";
				//String s2 = "java -cp ../elastic-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.cc.pdfparser.elastic.WarcPdfJsonGenerator "+gzFile+"0" + i + ".warc.gz";
				//String s3 = "rm -f "+gzFile+"0" + i + ".warc.gz";

				companyOut.append(s).append("\r\n");
				//companyOut.append(s2).append("\r\n");
				//companyOut.append(s3).append("\r\n");
				companyOut.flush();
			}
			for (int i = 100; i < 560; i++)
			{
				String s = "wget https://commoncrawl.s3.amazonaws.com/"+urlOne+ i + ".warc.gz";
				String s2 = "java -cp ../elastic-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.cc.pdfparser.elastic.WarcPdfJsonGenerator "+gzFile+ i + ".warc.gz";
				String s3 = "rm -f "+gzFile+ i + ".warc.gz";
				companyOut.append(s).append("\r\n");
				companyOut.append(s2).append("\r\n");
				companyOut.append(s3).append("\r\n");
				companyOut.flush();
			}
			companyOut.append("cd ..").append("\r\n");
			companyOut.flush();
			
		}
		companyOut.close();
	}
}
