package com.useless.local;

import com.useless.mapred.CSV;
import java.io.BufferedWriter;
import java.io.IOException;

public class CSVRecordWriterLocal
{
  private static final String SEPARATOR = "|";
  private BufferedWriter out;

  public CSVRecordWriterLocal()
  {
  }

  public CSVRecordWriterLocal(BufferedWriter out)
  {
    this.out = out;
  }

  public void write(String arg0, CSV arg1) throws IOException {
    this.out.write(arg0);
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

    writeBytes(arg1.getContactDetails());
    writeBytes("\r\n");
  }

  public void flush() throws IOException {
    this.out.flush();
  }

  private boolean isNullOrEmpty(String s1) {
    return (s1 == null) || (s1.isEmpty());
  }

  private void writeBytes(String text) throws IOException
  {
    if (!isNullOrEmpty(text))
    {
      this.out.write(text);
    }
  }
}