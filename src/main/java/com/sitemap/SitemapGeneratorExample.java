package com.sitemap;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashSet;

// Java Code To Generate Sitemap

public class SitemapGeneratorExample {

    private static HashSet<String> links;
    private static HashSet<String> dontCrawllinks;
    private static File crawledPDFLinks;
    private static Writer crawledPDFsLinkJSON;
    private static String rootURL = "";
    private static WebSitemapGenerator sitemapGenerator;
    public SitemapGeneratorExample() {
        links = new HashSet<String>();
        dontCrawllinks = new HashSet<String>();
    }

    public void getPageLinks(String parentURL, String URL) throws IOException {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        System.out.println("URL "+URL);
        WebSitemapUrl sitemapUrl = new WebSitemapUrl.Options(
                URL).lastMod(new Date()).priority(1.0)
                .changeFreq(ChangeFreq.WEEKLY).build();
        // this will configure the URL with lastmod=now, priority=1.0,
        // changefreq=hourly
        // You can add any number of urls here
        sitemapGenerator.addUrl(sitemapUrl);
        System.out.println("Added to SITEMAP list "+URL);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (links.contains(URL))
        {
            dontCrawllinks.add(URL);
            System.out.println("Already Crawled. Added to NOCRAWL list "+URL);
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
                    if(!dontCrawllinks.contains(uri) && uri.contains(rootURL))
                        getPageLinks(URL, page.attr("abs:href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            fn = "https://pdfdomain.com";
            fileName = "pdfdomain-sitemap";
        }

        //crawledPDFLinks = new File(fileName+ ".json");
        //crawledPDFsLinkJSON = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(crawledPDFLinks), "UTF8"));
        rootURL = fn;
        sitemapGenerator = WebSitemapGenerator
                .builder(fn, new File("E:\\everything_pdf\\scrapes_and_downloads\\sitemap\\"))
                .gzip(false).build();
        new SitemapGeneratorExample().getPageLinks(fn, fn);
        sitemapGenerator.write();
    }

    /*public static void main(String[] args) throws MalformedURLException {
        // If you need gzipped output
        WebSitemapGenerator sitemapGenerator = WebSitemapGenerator
                .builder("https://pdfdomain.com", new File("pdfdomain-sitemap.xml"))
                .gzip(false).build();

        WebSitemapUrl sitemapUrl = new WebSitemapUrl.Options(
                "http://www.javatips.net/blog/2011/08/findbugs-in-eclipse-java-tutorial")
                .lastMod(new Date()).priority(1.0)
                .changeFreq(ChangeFreq.WEEKLY).build();
        // this will configure the URL with lastmod=now, priority=1.0,
        // changefreq=hourly

        // You can add any number of urls here
        sitemapGenerator.addUrl(sitemapUrl);
        sitemapGenerator
                .addUrl("http://www.javatips.net/blog/2011/09/create-sitemap-using-java");
        sitemapGenerator.write();
    }*/
}