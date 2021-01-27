package com.useless.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class ClassPathFileReader
{
  public static Set<String> getFileLines(String fileName)
  {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    if (is == null)
    {
      System.out.println("could not load file from classpath" + fileName);
      System.exit(0);
    }
    Set list = new HashSet();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = null;
    try
    {
      while ((line = reader.readLine()) != null)
      {
        list.add(line);
      }
    }
    catch (IOException e) {
      e.printStackTrace();

      if (reader != null)
        try
        {
          reader.close();
        }
        catch (IOException e1) {
          e1.printStackTrace();
        }
    }
    finally
    {
      if (reader != null) {
        try
        {
          reader.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return list;
  }
  public static void main(String[] args) {
    Set list = getFileLines("domain.txt");
    System.out.println(list);
  }
}