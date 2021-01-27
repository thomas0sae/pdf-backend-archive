package com.useless.util;

import com.useless.SocialMediaSearchConf;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialMediaLinksExtractorUtil
{
  private static final int LINK_MAX_LENGTH = 100;

  public static Set<String> getLinkForSocialMedia(Set<String> list, SocialMediaSearchConf socialMedia, String domain)
  {
    Set resultList = new HashSet();
    String keywords = socialMedia.getExcludeKeywords();
    Matcher kwMatcher = null;
    if (keywords != null)
    {
      Pattern excludePattern = Pattern.compile(socialMedia.getRgex(), 2);
      kwMatcher = excludePattern.matcher(keywords);
    }
    String link;
    if (socialMedia.getIsRegex())
    {
      Pattern pattern = Pattern.compile(socialMedia.getRgex(), 2);
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); )
      {
        link = (String)localIterator.next();
        Matcher matcher = pattern.matcher(link);
        if ((!matcher.find()) || (link.length() >= 100))
          continue;
        if ((kwMatcher == null) || (kwMatcher.find()))
          continue;
        resultList.add(link);
      }

    }
    else
    {
      for (String link1 : list)
      {
        if ((!StringUtils.containsIgnoreCase(link1, socialMedia.getRgex())) || (link1.length() >= 100))
          continue;
        if ((kwMatcher == null) || (kwMatcher.find()))
          continue;
        resultList.add(link1);
      }

    }

    return filterByDomain(resultList, domain);
  }

  public static Map<SocialMediaSearchConf, Set<String>> getAllLinks(Set<String> list, String domain)
  {
    Map map = new HashMap();
    for (SocialMediaSearchConf socialMedia : SocialMediaSearchConf.values())
    {
      Set outList = getLinkForSocialMedia(list, socialMedia, domain);
      if (outList.isEmpty())
        continue;
      map.put(socialMedia, outList);
    }

    return map;
  }

  public static Set<String> filterByDomain(Set<String> list, String domain)
  {
    boolean flag = false;
    Set filteredList = new HashSet();
    for (String link : list)
    {
      if (!StringUtils.containsIgnoreCase(link, domain))
        continue;
      filteredList.add(link);
      flag = true;
    }

    if (flag)
    {
      return filteredList;
    }
    return list;
  }
}