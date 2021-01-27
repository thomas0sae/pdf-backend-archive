package com.useless.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainExtractUtil
{
  private static final Pattern pattern = Pattern.compile("www[1-9]", 2);

  //private static Set<String> domainsList = ClassPathFileReader.getFileLines("domain.txt");

  public static String getDoamin(String url) {
    String domain = url.substring(url.indexOf("//") + 2);
    if (domain.contains("/"))
    {
      domain = domain.substring(0, domain.indexOf("/"));
    }
    else
    {
      domain = domain.substring(0);
      if (domain.contains("?"))
      {
        domain = domain.substring(0, domain.indexOf("?"));
      }
    }
    if ((StringUtils.containsIgnoreCase(domain, "blog")) || (StringUtils.containsIgnoreCase(domain, "porn")))
    {
      return null;
    }
    return domain;
  }

  public static String getOrgNameFromDomain(String domain)
  {
    String orgName = null;
    if (domain != null)
    {
      String[] splits = domain.split("\\.");
      if (splits.length > 1)
      {
        Matcher matcher = pattern.matcher(splits[0]);
        if (matcher.find())
        {
          orgName = splits[1];
        }
        /*else if (domainsList.contains(splits[1].toUpperCase()))
        {
          orgName = splits[0];
        }*/
        else
        {
          orgName = splits[1];
        }
      }
    }

    return orgName;
  }
}