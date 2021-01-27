package com.useless.local;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class LineReaderUtil {
    public static List<String> readLines(int start, int end, InputStream is) {
        if ((start < 1) && (start > end)) {
            throw new RuntimeException("invalid line range");
        }
        List list = new ArrayList();
        LineNumberReader reader;
        String line;
        int lineNumber;
        try {
            Object localObject1 = null;
            Object localObject4 = null;
            Object localObject3;
            label152:
            try {
                reader = new LineNumberReader(new InputStreamReader(is));
            } finally {

                //localObject3 = localThrowable; break label152; if (localObject3 != localThrowable) localObject3.addSuppressed(localThrowable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> readLinesFromWarcPaths(int start, int end) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("warc-paths");
        if (is == null) {
            return null;
        }
        return readLines(start, end, is);
    }

    public static void main(String[] args) {
        System.out.println(readLinesFromWarcPaths(1, 3));
    }
}