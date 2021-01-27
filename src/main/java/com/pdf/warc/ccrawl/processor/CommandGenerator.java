package com.pdf.warc.ccrawl.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

//This class generates run*.sh files in the root folder of the project
// from warc paths to crawl and extract PDF or other social media links or other

public class CommandGenerator
{
	public static String[] urlString = new String[]
	{ 		"crawl-data/CC-MAIN-2020-40/segments/1600400187354.1/warc/CC-MAIN-20200918061627-20200918091627-00"
	};

	public static void main(String[] args) throws IOException
	{
		String s1 = "#!/bin/sh";
		for (String urlOne : urlString)
		{
			String gzFile = urlOne.substring(urlOne.lastIndexOf("/")+1, urlOne.length());
			File company;
			Writer companyOut = null;
			String createdfolderNameStr = null;
			String runningfolderNameStr = null;
			for (int i = 100; i < 560; i++)
			{
				if(i % 200 == 0 || i == 100)
				{
					if(companyOut!=null) companyOut.close();
					company = new File("runProg"+ i +".sh");
					companyOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(company), "UTF8"));
					companyOut.append(s1).append("\n");
					companyOut.flush();
					createdfolderNameStr = gzFile+ i;
					String createDir = "mkdir "+createdfolderNameStr;
					String intoDir = "cd "+createdfolderNameStr;
					companyOut.append(createDir).append("\n");
					companyOut.append(intoDir).append("\n");
					companyOut.flush();
				}
				String s = "wget https://commoncrawl.s3.amazonaws.com/"+urlOne+ i + ".warc.gz";
				String s2 = "java -cp ../ROOT.jar:../ROOT-jar-with-dependencies.jar com.pdf.warc.ccrawl.processor.WarcPdfDomainAndLinksGenerator "+gzFile + i + ".warc.gz";
				String s3 = "rm -f "+gzFile+ i + ".warc.gz";
				String s21 = "java -cp ../ROOT.jar:../ROOT-jar-with-dependencies.jar com.pdf.warc.ccrawl.processor.JsonLinkReaderAndProcessor "+gzFile+ i + ".warc.gz.json > log.txt";
				String s32 = "gzip "+gzFile+ i + ".warc.gz.json";
				String s321 = "mv log.txt log"+System.currentTimeMillis()+".txt";
				String s31 = "gzip log"+System.currentTimeMillis()+".txt";
				String s35 = "rm -rf downloads/";

				companyOut.append(s).append("\n");
				companyOut.append(s2).append("\n");
				companyOut.append(s3).append("\n");
				companyOut.append(s21).append("\n");
				companyOut.append(s32).append("\n");
				companyOut.append(s321).append("\n");
				companyOut.append(s31).append("\n");
				companyOut.append(s35).append("\n");
				companyOut.flush();
				if(i % 10 == 0 && i % 200 != 0 && i != 100)
				{
					String s33 = "tar -zcf pdf-images.gz pdf-images/";
					String s34 = "rm -rf pdf-images/";
					String s36 = "cd ..";
					String s4 = "tar -zcf "+createdfolderNameStr+".gz "+createdfolderNameStr+"/";
					String s5 = "rm -rf "+createdfolderNameStr+"/";
					companyOut.append(s33).append("\n");
					companyOut.append(s34).append("\n");
					companyOut.append(s36).append("\n");
					companyOut.append(s4).append("\n");
					companyOut.append(s5).append("\n");
					companyOut.flush();

					runningfolderNameStr = gzFile+ Integer.valueOf(i+1);
					String createDir = "mkdir "+runningfolderNameStr;
					String intoDir = "cd "+runningfolderNameStr;
					createdfolderNameStr = runningfolderNameStr;
					companyOut.append(createDir).append("\n");
					companyOut.append(intoDir).append("\n");
					companyOut.flush();
				}
				companyOut.flush();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			/*for (int i = 10; i < 100; i++)
			{
				String s = "wget https://commoncrawl.s3.amazonaws.com/"+urlOne+"0"	+ i + ".warc.gz";
				String s2 = "java -cp ../elastic-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.cc.pdfparser.elastic.WarcPdfJsonGenerator "+gzFile+"0" + i + ".warc.gz";
				String s3 = "rm -f "+gzFile+"0" + i + ".warc.gz";

				companyOut.append(s).append("\n");
				companyOut.append(s2).append("\n");
				companyOut.append(s3).append("\n");
				companyOut.flush();
			}
			for (int i = 100; i < 560; i++)
			{
				String s = "wget https://commoncrawl.s3.amazonaws.com/"+urlOne+ i + ".warc.gz";
				String s2 = "java -cp ../elastic-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.cc.pdfparser.elastic.WarcPdfJsonGenerator "+gzFile+ i + ".warc.gz";
				String s3 = "rm -f "+gzFile+ i + ".warc.gz";
				companyOut.append(s).append("\n");
				companyOut.append(s2).append("\n");
				companyOut.append(s3).append("\n");
				companyOut.flush();
			}*/
		}
	}
}
