package com.single.website.crawler;

import com.google.gson.Gson;
import com.pdf.warc.ccrawl.processor.PDFLinkRecordFromCCrawl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashSet;

/**
 * Usage BookToLearnCrawler "http://dl.booktolearn.com" dl-booktolearn-com
 */
public class PDFDriveCrawler
{
    private static HashSet<String> links;
    private static File crawledPDFLinks;
    private static Writer crawledPDFsLinkJSON;

    public PDFDriveCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String parentURL, String URL) throws IOException {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)

        if (!links.contains(URL) && !URL.contains("login"))
        {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL))
                {
                    System.out.println(URL);
                }
                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");
                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    System.out.println("Link Name "+page.text());
                    getPageLinks(URL, page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String fn = "";
        String fileName = "";
        if (args != null && args.length > 1 )
        {
            fn = args[0];
            fileName = args[1];
        }
        else
        {
            System.out.println("Invalid Arguments Exiting ");
            System.exit(0);
        }
        crawledPDFLinks = new File(fileName+ ".json");
        crawledPDFsLinkJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawledPDFLinks), "UTF8"));

        new PDFDriveCrawler().getPageLinks(fn, fn);
        System.out.println("Crawled PDFs "+links.size());
        crawledPDFsLinkJSON.close();
       }
}
