package com.useless.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactExtractorUtil
{
  private static final String PHONEREGEX = "((?i)tel|(?i)phone|(?i)mobile |(?i)ph|(?i)mob|(?i)telephone|(?i)fax)\\s{0,3}\\:?\\s{0,3}\\(?\\+?\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?";

  public static Set<String> getContacts(HtmlParser parser)
  {
    Set list = new HashSet();

    list.addAll(parser.serchTextByRegex("((?i)tel|(?i)phone|(?i)mobile |(?i)ph|(?i)mob|(?i)telephone|(?i)fax)\\s{0,3}\\:?\\s{0,3}\\(?\\+?\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?"));

    return list;
  }

  public static void main(String[] args)
  {
    Pattern pat = Pattern.compile("((?i)tel|(?i)phone|(?i)mobile |(?i)ph|(?i)mob|(?i)telephone|(?i)fax)\\s{0,3}\\:?\\s{0,3}\\(?\\+?\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?\\s*\\(?\\d([-.]?)\\)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?(\\s*\\(?\\d([-.]?)\\)?)?");
    Matcher matcher = pat.matcher("ph:+99 44 444 55");
    System.out.println(matcher.find());
  }
}