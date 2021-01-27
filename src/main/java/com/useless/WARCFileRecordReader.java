package com.useless;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.jwat.warc.WarcReader;
import org.jwat.warc.WarcReaderFactory;

public class WARCFileRecordReader extends RecordReader<Text, WarcReader>
{
  private String arPath;
  private WarcReader ar;
  private FSDataInputStream fsin;
  private boolean hasBeenRead = false;

  public void initialize(InputSplit inputSplit, TaskAttemptContext context)
    throws IOException, InterruptedException
  {
    FileSplit split = (FileSplit)inputSplit;
    Configuration conf = context.getConfiguration();
    Path path = split.getPath();
    FileSystem fs = path.getFileSystem(conf);
    this.fsin = fs.open(path);
    this.arPath = path.getName();
    this.ar = WarcReaderFactory.getReader(this.fsin);
  }

  public void close() throws IOException
  {
    this.fsin.close();
    this.ar.close();
  }

  public Text getCurrentKey()
    throws IOException, InterruptedException
  {
    return new Text(this.arPath);
  }

  public WarcReader getCurrentValue()
    throws IOException, InterruptedException
  {
    return this.ar;
  }

  public float getProgress()
    throws IOException, InterruptedException
  {
    return this.hasBeenRead ? 1 : 0;
  }

  public boolean nextKeyValue()
    throws IOException, InterruptedException
  {
    if (this.hasBeenRead) {
      return false;
    }
    this.hasBeenRead = true;
    return true;
  }
}