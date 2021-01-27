package com.useless.extractor;

import com.useless.mapred.CSV;
import com.useless.util.WarcReaderUtil;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.jwat.warc.WarcReader;
import org.jwat.warc.WarcRecord;

public class WARCExtractorMapper extends Mapper<Text, WarcReader, Text, CSV>
{
  protected void map(Text key, WarcReader value, Mapper<Text, WarcReader, Text, CSV>.Context context)
    throws IOException, InterruptedException
  {
    WarcRecord record = null;
    while ((record = value.getNextRecord()) != null)
    {
      CSV csvRecord = WarcReaderUtil.readWarc(record);
      if ((csvRecord == null) || (csvRecord.isEmpty()))
        continue;
      String domain = csvRecord.getDomain();
      if (domain == null)
        continue;
      context.write(new Text(domain), csvRecord);
    }
  }

  protected static enum MAPPERCOUNTER
  {
    RECORDS_IN, 
    EMPTY_PAGE_TEXT, 
    EXCEPTIONS, 
    NON_PLAIN_TEXT;
  }
}