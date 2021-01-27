package com.useless.mapred;

import java.io.IOException;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CSVOutputPutFormat extends FileOutputFormat<Text, CSV>
{
  public RecordWriter<Text, CSV> getRecordWriter(TaskAttemptContext arg0)
    throws IOException, InterruptedException
  {
    Path path = FileOutputFormat.getOutputPath(arg0);
    Path fullPath = new Path(path, arg0.getJobID().toString());
    FileSystem fs = path.getFileSystem(arg0.getConfiguration());
    FSDataOutputStream fileOut = fs.create(fullPath, arg0);
    return new CSVRecordWriter(fileOut);
  }
}