package com.useless.mapred;

import java.io.IOException;

import com.useless.WARCFileRecordReader;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.jwat.warc.WarcReader;

public class WARCFileInputFormat extends FileInputFormat<Text, WarcReader>
{
  public RecordReader<Text, WarcReader> createRecordReader(InputSplit split, TaskAttemptContext context)
    throws IOException, InterruptedException
  {
    return new WARCFileRecordReader();
  }

  protected boolean isSplitable(JobContext context, Path filename)
  {
    return false;
  }
}