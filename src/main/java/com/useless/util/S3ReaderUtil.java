package com.useless.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

public class S3ReaderUtil
{
  public static List<String> readLines(String file, String accessId, String accessKey, String bucketName)
  {
    List lines = new ArrayList();
    BufferedReader reader = null;
    InputStream is = null;
    try
    {
      AWSCredentials credentials = new AWSCredentials(accessId, accessKey);
      S3Service service = new RestS3Service(credentials);
      S3Bucket myBuckets = service.getBucket(bucketName);
      S3Object obj = service.getObject(myBuckets, file);
      is = obj.getDataInputStream();
      reader = new BufferedReader(new InputStreamReader(is));
      String line = null;
      while ((line = reader.readLine()) != null)
      {
        lines.add(line);
      }
    }
    catch (S3ServiceException e) {
      e.printStackTrace();
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
      try
      {
        if (reader != null)
          reader.close();
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
      try
      {
        if (reader != null)
          reader.close();
      }
      catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      try
      {
        if (reader != null)
          reader.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    return lines;
  }
}