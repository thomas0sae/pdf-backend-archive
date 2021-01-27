package com.socialmedia.handles.extractor;

import com.google.common.net.InternetDomainName;
import com.google.gson.Gson;
import com.useless.SocialMediaSearchConf;
import com.useless.mapred.CSV;
import com.useless.util.SocialMediaLinksExtractorUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SocialMediaHandlesExtractorAndCrawler {
    private static String filePath = "E:\\everything_pdf\\ccrawl-downloads\\cc-main-2020-feb-mar-may-domain-ranks.txt";

    private static Writer allLinksWriter = null;
    private static Writer socialMediaWriter = null;

    static
    {
        getAllLinksFileWriter();
        getSocialMediaFileWriter();
    }

    public SocialMediaHandlesExtractorAndCrawler() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        String inputFile = args[0];
        if(inputFile != null)
        {
            filePath = inputFile;
        }
        File f = new File(filePath);
        InputStream is = null;
        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        @SuppressWarnings("resource")
        BufferedReader bufferIn = new BufferedReader(new InputStreamReader(is));

        String line = "";
        int lineCount = 0;
        String topDomain = "";
        String hostName = "";
        try {
            while ((line = bufferIn.readLine()) != null) {
                lineCount++;
                //if (lineCount == 1) continue;
                String[] splitLine = line.split("\t");
                Map allLinks = new HashMap();
                if (splitLine.length != 0)
                {
                    String harmonicc_pos = splitLine[0];
                    String pr_pos = splitLine[2];
                    String host_rev = splitLine[4];
                    String n_hosts = splitLine[5];
                    hostName = reverseHostName(host_rev);
                    allLinks.put("harmonicc_pos", harmonicc_pos);
                    allLinks.put("pr_pos", pr_pos);
                    allLinks.put("n_hosts", n_hosts);
                }
                if (!hostName.trim().isEmpty()) {
                    topDomain = getTopDomain(hostName);
                    String url = "http://www."+topDomain;
                    URL obj1 = new URL(url);
                    String webPage = downloadAndProcessHomePage(obj1);
                    if(webPage.trim().isEmpty())
                    {
                        url = "https://www."+topDomain;
                        obj1 = new URL(url);
                        webPage = downloadAndProcessHomePage(obj1);
                    }
                    if(webPage.trim().isEmpty())
                    {
                        url = "http://"+topDomain;
                        obj1 = new URL(url);
                        webPage = downloadAndProcessHomePage(obj1);
                    }
                    if(webPage.trim().isEmpty())
                    {
                        url = "https://"+topDomain;
                        obj1 = new URL(url);
                        webPage = downloadAndProcessHomePage(obj1);
                    }

                    if(webPage.trim().isEmpty())
                    {
                        url = "http://www."+hostName;
                        obj1 = new URL(url);
                        webPage = downloadAndProcessHomePage(obj1);
                    }
                    if(webPage.trim().isEmpty())
                    {
                        url = "https://www."+hostName;
                        obj1 = new URL(url);
                        webPage = downloadAndProcessHomePage(obj1);
                    }
                    if(webPage.trim().isEmpty())
                    {
                        url = "http://"+hostName;
                        obj1 = new URL(url);
                        webPage = downloadAndProcessHomePage(obj1);
                    }
                    if(webPage.trim().isEmpty())
                    {
                        url = "https://"+hostName;
                        obj1 = new URL(url);
                        webPage = downloadAndProcessHomePage(obj1);
                    }
                    if(webPage.trim().isEmpty())
                    {
                        continue;
                    }
                    Document doc = Jsoup.parse(webPage, "UTF-8");
                    Set<String> hrefLinks = getAllLinks(doc);
                    CSV csvRecord = readAndGetSocialCSV(url, topDomain, hrefLinks);
                    csvRecord.setContactDetails(convertListToString(getContacts(doc)));

                    allLinks.put("domain", topDomain);
                    allLinks.put("url", url);
                    allLinks.put("socialLinks", csvRecord);
                    socialMediaWriter.append(new Gson().toJson(allLinks)).append("\r\n");
                    socialMediaWriter.flush();
                    allLinks = null;
                    csvRecord = null;
                    hrefLinks = null;
                    doc = null;
                    System.out.println("HostName Finished "+hostName);
                    if(lineCount % 500 == 0) System.gc();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getAllLinksFileWriter() {

        try {
            File allLinksJsonFile = new File("all-links" + System.currentTimeMillis() + ".json");
            allLinksWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allLinksJsonFile), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void getSocialMediaFileWriter() {
        try {
            File socialLinksJsonFile = new File("socialmedia-links" + System.currentTimeMillis() + ".json");
            socialMediaWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(socialLinksJsonFile), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String downloadAndProcessHomePage(URL obj1) throws IOException {

        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        String webPageOut = "";
        try {


            httpURLConnection = (HttpURLConnection) obj1.openConnection();
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            // optional default is GET
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Origin", "https://google.com");
            httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            httpURLConnection.setRequestProperty("Cache-Control", "max-age=0");
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "keep-alive");
            httpURLConnection.setRequestProperty("Host", "https://google.com");
            httpURLConnection.setRequestProperty("Referer", "https://google.com");
            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            httpURLConnection.setRequestProperty("User-Agent", "https://google.com/bot.html");
            httpURLConnection.addRequestProperty("User-Agent", "https://google.com/bot.html");
            int responseCode = httpURLConnection.getResponseCode();
            //System.out.println("responseCode " + responseCode);
            if (!Objects.equals(HttpURLConnection.HTTP_OK, responseCode)) {
                System.out.println("URL Not Found - " + obj1.toString());
                return "";
            }
            InputStream in = httpURLConnection.getInputStream();
            System.setProperty("http.agent", "Chrome");
            reader = new BufferedReader(new InputStreamReader(in));
            // write the output to stdout
            String line;

            while ((line = reader.readLine()) != null) {
                webPageOut = webPageOut + line;
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Exception "+e.getMessage());
            return "";
        } finally {
            if(reader!=null) reader.close();

            httpURLConnection.disconnect();

        }
        return webPageOut;
    }

    private static String getTopDomain(String hostName) {
        String topDomain = "";
        try {
            topDomain = InternetDomainName.from(hostName).topPrivateDomain().toString();
        } catch (Exception e) {
            return hostName;
        }
        return topDomain;
    }


    private static String reverseHostName(String host_rev) {
        String hostName = "";
        if (host_rev != null && !host_rev.isEmpty()) {
            //System.out.println("host_rev "+host_rev);
            String[] hostSplit = host_rev.split("\\.");

            //System.out.println("0 "+hostSplit[0]);
            //System.out.println("1 "+hostSplit[1]);
            String hostName1 = "";
            for (int i = (hostSplit.length - 1); i > -1; i--) {
                if (hostName1.isEmpty()) {
                    hostName1 = hostSplit[i];
                    continue;
                }
                hostName1 = hostName1 + "." + hostSplit[i];
            }
            hostName = hostName1;
            //System.out.println("hostname " + hostName);
        }
        return hostName;
    }

    public static Set<String> getAllLinks(Document doc) {
        Elements links = doc.select("a[href]");
        Set<String> list = new HashSet<String>();
        for (Element link : links) {
            String l = link.attr("abs:href");
            if (l == null)
                continue;
            l = l.trim();

            list.add(l);
        }
        return list;
    }

    public static CSV readAndGetSocialCSV(String url, String domain, Set<String> links) throws IOException {
        CSV csvRecord = new CSV();
        csvRecord.setDomain(domain);
        csvRecord.setUrl(url);
        Map map = SocialMediaLinksExtractorUtil.getAllLinks(links, domain);
        csvRecord.setFbLinks(convertListToString((Collection) map.get(SocialMediaSearchConf.FACEBOOK)));
        csvRecord.setLinkedInLinks(convertListToString((Collection) map.get(SocialMediaSearchConf.LINKEDIN)));
        csvRecord.setTwitterLinks(convertListToString((Collection) map.get(SocialMediaSearchConf.TWITTER)));
        csvRecord.setYoutube(convertListToString((Collection) map.get(SocialMediaSearchConf.YOUTUBE)));
        csvRecord.setInstagram(convertListToString((Collection) map.get(SocialMediaSearchConf.INSTAGRAM)));
        csvRecord.setPinterest(convertListToString((Collection) map.get(SocialMediaSearchConf.PINTEREST)));
        csvRecord.setTumblr(convertListToString((Collection) map.get(SocialMediaSearchConf.TUMBLR)));
        csvRecord.setApple(convertListToString((Collection) map.get(SocialMediaSearchConf.APPLE)));
        csvRecord.setGplay(convertListToString((Collection) map.get(SocialMediaSearchConf.GPLAY)));
        csvRecord.setWindowsPhone(convertListToString((Collection) map.get(SocialMediaSearchConf.WINDOWSPHONE)));
        csvRecord.setReddit(convertListToString((Collection) map.get(SocialMediaSearchConf.REDDIT)));
        csvRecord.setYelp(convertListToString((Collection) map.get(SocialMediaSearchConf.YELP)));
        csvRecord.setGitHub(convertListToString((Collection) map.get(SocialMediaSearchConf.GITHUB)));
        csvRecord.setMedium(convertListToString((Collection) map.get(SocialMediaSearchConf.MEDIUM)));
        csvRecord.setVimeo(convertListToString((Collection) map.get(SocialMediaSearchConf.VIMEO)));
        csvRecord.setSlack(convertListToString((Collection) map.get(SocialMediaSearchConf.SLACK)));
        //System.out.println("csvRecord "+csvRecord);
        //System.out.println("Links "+convertListToString(links));

        Map allLinks = new HashMap();
        allLinks.put("domain", domain);
        allLinks.put("allLinks", links);
        allLinksWriter.append(new Gson().toJson(allLinks)).append("\r\n");
        allLinksWriter.flush();

        return csvRecord;
    }

    private static String convertListToString(Collection<String> list) {
        if ((list == null) || (list.isEmpty())) {
            return null;
        }
        String s = Arrays.toString(list.toArray(new String[list.size()]));
        return s.substring(1, s.length() - 1);
    }

    public static Set<String> searchTextByRegex(Document doc, String text)
    {
        Set list = new HashSet();
        Elements elements = doc.select(":containsOwn(" + text + ")");
        for (Element elem : elements)
        {
            Element parent = elem.parent();
            if (parent == null)
                continue;
            String html = parent.html();
            if (html == null)
                continue;
            list.add(html.replaceAll("\n", ""));
        }
        return list;
    }

    public static Set<String> getContacts(Document doc)
    {
        Set hashSet = new HashSet();
        hashSet.addAll(searchTextByRegex(doc, "((?i)tel|(?i)phone|(?i)mobile |(?i)ph|(?i)mob|(?i)telephone|(?i)fax)\\s{0,3}\\:?\\s{0,3}\\(?\\+?\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?"));
        return hashSet;
    }
}
