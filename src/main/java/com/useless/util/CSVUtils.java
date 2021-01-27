package com.useless.util;

import com.useless.mapred.CSV;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CSVUtils
{
  public static CSV merge(Iterable<CSV> values)
  {
    CSV csv = new CSV();
    Set fbLinks = new HashSet();
    Set twitterLinks = new HashSet();
    Set gplusLinks = new HashSet();
    Set linkedinLinks = new HashSet();
    Set youtubeLinks = new HashSet();
    Set contacts = new HashSet();

    Set instagramLinks = new HashSet();
    Set pinterestLinks = new HashSet();
    Set tumblrLinks = new HashSet();

    Set appleLinks = new HashSet();
    Set gplayLinks = new HashSet();
    Set windowsPhoneLinks = new HashSet();

    Set redditLinks = new HashSet();
    Set yelpLinks = new HashSet();
    for (CSV value : values)
    {
      if (!isNullOrEmpty(value.getFbLinks()))
      {
        fbLinks.add(value.getFbLinks());
      }
      if (!isNullOrEmpty(value.getTwitterLinks()))
      {
        twitterLinks.add(value.getTwitterLinks());
      }
      if (!isNullOrEmpty(value.getGooglePlus()))
      {
        gplusLinks.add(value.getGooglePlus());
      }
      if (!isNullOrEmpty(value.getLinkedInLinks()))
      {
        linkedinLinks.add(value.getLinkedInLinks());
      }
      if (!isNullOrEmpty(value.getYoutube()))
      {
        youtubeLinks.add(value.getYoutube());
      }
      if (!isNullOrEmpty(value.getInstagram()))
      {
        instagramLinks.add(value.getInstagram());
      }
      if (!isNullOrEmpty(value.getPinterest()))
      {
        pinterestLinks.add(value.getPinterest());
      }
      if (!isNullOrEmpty(value.getTumblr()))
      {
        tumblrLinks.add(value.getTumblr());
      }
      if (!isNullOrEmpty(value.getApple()))
      {
        appleLinks.add(value.getApple());
      }
      if (!isNullOrEmpty(value.getGplay()))
      {
        gplayLinks.add(value.getGplay());
      }
      if (!isNullOrEmpty(value.getWindowsPhone()))
      {
        windowsPhoneLinks.add(value.getWindowsPhone());
      }
      if (!isNullOrEmpty(value.getReddit()))
      {
        redditLinks.add(value.getReddit());
      }
      if (!isNullOrEmpty(value.getYelp()))
      {
        yelpLinks.add(value.getYelp());
      }
      if (isNullOrEmpty(value.getContactDetails()))
        continue;
      contacts.add(value.getContactDetails());
    }

    csv.setContactDetails(mergeString(contacts));
    csv.setFbLinks(mergeString(fbLinks));
    csv.setLinkedInLinks(mergeString(linkedinLinks));
    csv.setGooglePlus(mergeString(gplusLinks));
    csv.setTwitterLinks(mergeString(twitterLinks));
    csv.setYoutube(mergeString(youtubeLinks));
    csv.setInstagram(mergeString(instagramLinks));
    csv.setPinterest(mergeString(pinterestLinks));
    csv.setTumblr(mergeString(tumblrLinks));
    csv.setApple(mergeString(appleLinks));
    csv.setGplay(mergeString(gplayLinks));
    csv.setReddit(mergeString(redditLinks));
    csv.setYelp(mergeString(yelpLinks));
    csv.setWindowsPhone(mergeString(windowsPhoneLinks));
    return csv;
  }

  public static String mergeString(Set<String> list)
  {
    if (list.size() == 0)
    {
      return null;
    }

    String s = Arrays.toString(list.toArray(new String[list.size()]));
    return s.substring(1, s.length() - 1);
  }

  private static boolean isNullOrEmpty(String s)
  {
    return (s == null) || (s.isEmpty());
  }
}