package com.useless.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser
{
  private static final int CONTACT_MAX_LENGTH = 300;
  private Document doc;

  public HtmlParser()
  {
  }

  public HtmlParser(InputStream is)
  {
    try
    {
      this.doc = parse(is);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Document parse(InputStream is) throws IOException {
    Document doc = Jsoup.parse(is, "UTF-8", "");
    return doc;
  }

  public Set<String> getAllLinks()
  {
    Elements links = this.doc.select("a[href]");
    Set<String> list = new HashSet<String>();
    for (Element link : links)
    {
      String l = link.attr("abs:href");
      if (l == null)
        continue;
      l = l.trim();

      list.add(l);
    }

    return list;
  }

  public String getFullTextFromHtml()
  {
    return this.doc.body().text();
  }

  public Set<String> serchText(String text)
  {
    Set list = new HashSet();
    Elements elements = this.doc.select(":containsOwn(" + text + ")");
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

  public Set<String> serchTextByRegex(String text)
  {
    Set list = new HashSet();
    Elements elements = this.doc.select(":matchesOwn(" + text + ")");
    for (Element elem : elements)
    {
      Element parent = elem.parent();
      if (parent == null)
        continue;
      String html = parent.text();
      if ((html == null) || (html.length() >= 300))
        continue;
      list.add(html.replaceAll("\n|\\|", ""));
    }

    return list;
  }
}