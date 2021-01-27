package com.useless.util;

import com.useless.SocialMediaSearchConf;
import com.useless.mapred.CSV;
import org.jwat.common.Payload;
import org.jwat.warc.WarcHeader;
import org.jwat.warc.WarcReader;
import org.jwat.warc.WarcReaderFactory;
import org.jwat.warc.WarcRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class WarcReaderUtil
{
  public static List<CSV> readWarc(WarcReader reader)
    throws IOException
  {
    List csvRecords = new ArrayList();
    WarcRecord record = null;
    while ((record = reader.getNextRecord()) != null)
    {
      CSV csv = readWarc(record);
      if (csv == null)
        continue;
      csvRecords.add(csv);
    }

    return csvRecords;
  }

  public static CSV readWarc(WarcRecord record) throws IOException
  {
    CSV csvRecord = null;
    WarcHeader header = record.header;
    if ((header.contentType.contentType.equals("application")) && 
      (header.contentType.mediaType
      .equals("http")))
    {
      String url = header.warcTargetUriStr;
      String domain = DomainExtractUtil.getDoamin(url);
      if (domain != null)
      {
        csvRecord = new CSV();
        csvRecord.setDomain(domain);
        Payload payload = record.getPayload();
        InputStream is = payload.getInputStreamComplete();
        if (is != null)
        {
          HtmlParser parser = new HtmlParser(is);
          Map map = SocialMediaLinksExtractorUtil.getAllLinks(parser.getAllLinks(), domain);
          csvRecord.setFbLinks(convertListToString((Collection)map.get(SocialMediaSearchConf.FACEBOOK)));
          csvRecord.setLinkedInLinks(convertListToString((Collection)map.get(SocialMediaSearchConf.LINKEDIN)));
          csvRecord.setTwitterLinks(convertListToString((Collection)map.get(SocialMediaSearchConf.TWITTER)));
          csvRecord.setYoutube(convertListToString((Collection)map.get(SocialMediaSearchConf.YOUTUBE)));
          csvRecord.setInstagram(convertListToString((Collection)map.get(SocialMediaSearchConf.INSTAGRAM)));
          csvRecord.setPinterest(convertListToString((Collection)map.get(SocialMediaSearchConf.PINTEREST)));
          csvRecord.setTumblr(convertListToString((Collection)map.get(SocialMediaSearchConf.TUMBLR)));
          csvRecord.setApple(convertListToString((Collection)map.get(SocialMediaSearchConf.APPLE)));
          csvRecord.setGplay(convertListToString((Collection)map.get(SocialMediaSearchConf.GPLAY)));
          csvRecord.setWindowsPhone(convertListToString((Collection)map.get(SocialMediaSearchConf.WINDOWSPHONE)));
          csvRecord.setReddit(convertListToString((Collection)map.get(SocialMediaSearchConf.REDDIT)));
          csvRecord.setYelp(convertListToString((Collection)map.get(SocialMediaSearchConf.YELP)));
          csvRecord.setGitHub(convertListToString((Collection)map.get(SocialMediaSearchConf.GITHUB)));
          csvRecord.setMedium(convertListToString((Collection)map.get(SocialMediaSearchConf.MEDIUM)));
          csvRecord.setVimeo(convertListToString((Collection)map.get(SocialMediaSearchConf.VIMEO)));
          csvRecord.setSlack(convertListToString((Collection)map.get(SocialMediaSearchConf.SLACK)));
          csvRecord.setContactDetails(convertListToString(ContactExtractorUtil.getContacts(parser)));
        }
        is.close();
      }
    }
    System.out.println(csvRecord);
    return csvRecord;
  }

  public static List<CSV> readWarc(String path) {
    InputStream is = null;
    WarcReader reader = null;
    List record = null;
    try
    {
      is = new FileInputStream(path);
      reader = WarcReaderFactory.getReader(is);
      record = readWarc(reader);
    }
    catch (IOException e)
    {
      System.out.println("error opening file" + path);

      if (reader != null)
      {
        reader.close();
      }
      if (is != null)
        try
        {
          is.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
    }
    finally
    {
      if (reader != null)
      {
        reader.close();
      }
      if (is != null) {
        try
        {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return record;
  }

  private static String convertListToString(Collection<String> list)
  {
    if ((list == null) || (list.isEmpty()))
    {
      return null;
    }

    String s = Arrays.toString(list.toArray(new String[list.size()]));
    return s.substring(1, s.length() - 1);
  }
}