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
public class BookToLearnCrawler
{
    private static HashSet<String> links;
    private static HashSet<String> dontCrawllinks;
    private static File crawledPDFLinks;
    private static Writer crawledPDFsLinkJSON;
    private static String rootURL = "";
    public BookToLearnCrawler() {
        links = new HashSet<String>();
        dontCrawllinks = new HashSet<String>();
    }

    public void getPageLinks(String parentURL, String URL) throws IOException {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if(URL.contains(".pdf"))
        {
            PDFLinkRecordFromCCrawl pdfLinkRecordFromCCrawl = new PDFLinkRecordFromCCrawl();
            pdfLinkRecordFromCCrawl.setWebPageUrl(parentURL);
            pdfLinkRecordFromCCrawl.setPdfFullURL(URL);
            String[] categoryArr = URL.split("/");
            int size = categoryArr.length;
            System.out.println("size "+size);
            String category = "General";
            if( size > 5)
            {
                if(size == 6)
                {
                    category = categoryArr[4];
                }
                else if(size == 7)
                {
                    category = categoryArr[4] +","+ categoryArr[5];
                }
                else if(size == 8)
                {
                    category = categoryArr[4] +","+ categoryArr[5]+","+ categoryArr[6];
                }
                else if(size == 9)
                {
                    category = categoryArr[4] +","+categoryArr[5] +","+ categoryArr[6]+","+ categoryArr[7];
                }
                else if(size == 10)
                {
                    category = categoryArr[4] +","+categoryArr[5] +","+ categoryArr[6]+","+
                            categoryArr[7]+","+
                            categoryArr[8];
                }
                pdfLinkRecordFromCCrawl.setCategory(category);
                dontCrawllinks.add(URL);
            }
            //pdflinks.add(URL);
            crawledPDFsLinkJSON.append(new Gson().toJson(pdfLinkRecordFromCCrawl)).append("\r\n");
            crawledPDFsLinkJSON.flush();
            return;
        }
        System.out.println("URL "+URL);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (links.contains(URL))
        {
            dontCrawllinks.add(URL);
            return;
        }
        if (!links.contains(URL) && URL.contains(rootURL)) {
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
                    String uri = page.attr("abs:href");
                    if(uri.toLowerCase().endsWith(".jpg") ||
                            uri.toLowerCase().endsWith(".jpeg")||
                            uri.toLowerCase().endsWith(".json")||
                            uri.toLowerCase().endsWith(".7z")||
                            uri.toLowerCase().endsWith(".zip")||
                            uri.toLowerCase().endsWith(".png")||
                            uri.toLowerCase().endsWith(".tar")||
                            uri.toLowerCase().endsWith(".tiff")||
                            uri.toLowerCase().endsWith(".tif")||
                            uri.toLowerCase().endsWith(".eps")||
                            uri.toLowerCase().endsWith(".zst")||
                            uri.toLowerCase().endsWith(".gif")||
                            uri.toLowerCase().endsWith(".pict")||
                            uri.toLowerCase().endsWith(".mp4")||
                            uri.toLowerCase().endsWith(".mpv")||
                            uri.toLowerCase().endsWith(".epub")||
                            uri.toLowerCase().endsWith(".mobi")||
                            uri.toLowerCase().endsWith(".m4v")||
                            uri.toLowerCase().endsWith(".mov")||
                            uri.toLowerCase().endsWith(".mp3")||
                            uri.toLowerCase().endsWith(".m4b")||
                            uri.toLowerCase().endsWith(".torrent")||
                            uri.toLowerCase().endsWith(".mpv")||
                            uri.toLowerCase().endsWith(".avi")||
                            uri.toLowerCase().endsWith(".gz")||
                            uri.toLowerCase().endsWith(".svg")||
                            uri.toLowerCase().endsWith(".xml") ||
                                    !page.attr("abs:href").contains(rootURL)) {
                        if(!uri.endsWith(".pdf")) {
                            System.out.println("Not adding to crawl " + uri);
                            continue;
                        }
                    }
                    if(!dontCrawllinks.contains(uri))
                        getPageLinks(URL, page.attr("abs:href"));
                }
            } catch (IOException e) {
                //System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String fn = "https://freecomputerbooks.com/";
        String fileName = "freecomputerbooks_com";
        if (args != null && args.length > 1 )
        {
            fn = args[0];
            fileName = args[1];
        }

        crawledPDFLinks = new File(fileName+ ".json");
        crawledPDFsLinkJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawledPDFLinks), "UTF8"));
        rootURL = fn;
        new BookToLearnCrawler().getPageLinks(fn, fn);
        System.out.println("Crawled PDFs "+links.size());
        crawledPDFsLinkJSON.close();
       }
}
