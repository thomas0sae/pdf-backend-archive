package com.useless;

import com.useless.util.WarcReaderUtil;
import java.util.List;

public class WarcReaderUtilTest
{
  public static void main(String[] args)
  {
    String f = "E:\\everything_pdf\\scrapes_and_downloads\\social-media-links-extract\\CC-MAIN-20200918061627-20200918091627-00460.warc.gz";
    List list = WarcReaderUtil.readWarc(f);
  }
}