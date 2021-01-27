package com.useless;

import com.useless.util.DomainExtractUtil;
import com.useless.util.HtmlParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.jwat.common.Payload;
import org.jwat.warc.WarcHeader;
import org.jwat.warc.WarcReader;
import org.jwat.warc.WarcReaderFactory;
import org.jwat.warc.WarcRecord;

public class WarcReaderTest
{
  public static void main(String[] args)
    throws MalformedURLException, IOException
  {
    File f = new File("E:\\Data_dump\\ccrawl_WARC\\CC-MAIN-20150417045713-00281-ip-10-235-10-82.ec2.internal.warc.gz");
    InputStream is = new FileInputStream(f);
    WarcReader reader = WarcReaderFactory.getReader(is);
    WarcRecord record = null;

    while ((record = reader.getNextRecord()) != null)
    {
      WarcHeader header = record.header;
      if ((!header.contentType.contentType.equals("application")) || 
        (!header.contentType.mediaType
        .equals("http")))
        continue;
      String url = header.warcTargetUriStr;
      String domain = DomainExtractUtil.getDoamin(url);

      Payload payload = record.getPayload();
      HtmlParser parser = new HtmlParser(payload.getInputStreamComplete());
      System.out.println(parser.getFullTextFromHtml());
    }
  }
}