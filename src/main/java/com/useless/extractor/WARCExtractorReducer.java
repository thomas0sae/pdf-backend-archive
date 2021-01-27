package com.useless.extractor;

import com.useless.mapred.CSV;
import com.useless.util.CSVUtils;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WARCExtractorReducer extends Reducer<Text, CSV, Text, CSV>
{
  protected void reduce(Text key, Iterable<CSV> value, Reducer<Text, CSV, Text, CSV>.Context arg2)
    throws IOException, InterruptedException
  {
    CSV csv = CSVUtils.merge(value);
    arg2.write(key, csv);
  }
}