package com.useless.mapred;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.DataOutputStream;
import java.io.IOException;

public class CSVRecordWriter extends RecordWriter<Text, CSV>
{
  private static final String SEPARATOR = "|";
  private DataOutputStream out;

  public CSVRecordWriter()
  {
  }

  public CSVRecordWriter(DataOutputStream out)
  {
    this.out = out;
  }

  public void close(TaskAttemptContext arg0)
    throws IOException, InterruptedException
  {
    this.out.close();
  }

  public void write(Text arg0, CSV arg1)
    throws IOException, InterruptedException
  {
    this.out.writeBytes(arg0.toString());
    writeBytes("|");

    writeBytes(arg1.getFbLinks());
    writeBytes("|");

    writeBytes(arg1.getLinkedInLinks());
    writeBytes("|");

    writeBytes(arg1.getGooglePlus());
    writeBytes("|");

    writeBytes(arg1.getTwitterLinks());
    writeBytes("|");

    writeBytes(arg1.getYoutube());
    writeBytes("|");

    writeBytes(arg1.getInstagram());
    writeBytes("|");

    writeBytes(arg1.getPinterest());
    writeBytes("|");

    writeBytes(arg1.getTumblr());
    writeBytes("|");

    writeBytes(arg1.getApple());
    writeBytes("|");

    writeBytes(arg1.getGplay());
    writeBytes("|");

    writeBytes(arg1.getWindowsPhone());
    writeBytes("|");

    writeBytes(arg1.getReddit());
    writeBytes("|");

    writeBytes(arg1.getYelp());
    writeBytes("|");

    writeBytes(arg1.getGitHub());
    writeBytes("|");

    writeBytes(arg1.getMedium());
    writeBytes("|");

    writeBytes(arg1.getSlack());
    writeBytes("|");

    writeBytes(arg1.getVimeo());
    writeBytes("|");

    writeBytes(arg1.getContactDetails());
    writeBytes("\r\n");
  }

  private boolean isNullOrEmpty(String s1)
  {
    return (s1 == null) || (s1.isEmpty());
  }

  private void writeBytes(String text) throws IOException
  {
    if (!isNullOrEmpty(text))
    {
      this.out.write(text.getBytes());
    }
  }
}