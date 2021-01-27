package com.libgen;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class LibGenMySqlDescriptionReader
{

    public static void main(String args[]) throws Exception {
        final File folder = new File("elastic_json_in\\");
        listFilesForFolder(folder);
    }

    public static void listFilesForFolder(final File folder) throws Exception {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry);
                readAndProcessNonFictionJSON(fileEntry);
            }
        }
    }

    private static void readAndProcessNonFictionJSON(File jsonFileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(jsonFileName));
        String st;
        Writer libGenImageDownloadJsonWriter = getElasticJsonFileWriter();
        LibGenImageDownload libGenImageDownload = new LibGenImageDownload();
        HashMap<String,String> topicMap = libGenImageDownload.getTopicMap();
        Class.forName("org.gjt.mm.mysql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/libgen", "root", "");
        while ((st = br.readLine()) != null) {
            System.out.println(st);
            if(st.startsWith("{\"index\""))
            {
                libGenImageDownloadJsonWriter.append(st).append("\r\n");
                libGenImageDownloadJsonWriter.flush();
                continue;
            }
            Gson gson = new Gson();
            LibGenNonFiction response = gson.fromJson(st, LibGenNonFiction.class);
            String topic = response.getTopic();
            if(topic != null && !topic.trim().isEmpty())
            {
                String topicMapStr = topicMap.get(topic);
                System.out.println("Topic actual is "+topic);
                if(topicMapStr != null)
                {
                    System.out.println("Topic newly set is "+topicMapStr);
                    response.setTopic(topicMapStr);
                }
            }
            callAndGetDescription(response, conn);
            libGenImageDownloadJsonWriter.append(libGenImageDownload.toJson(response)).append("\r\n");
            libGenImageDownloadJsonWriter.flush();
        }
        libGenImageDownloadJsonWriter.flush();
        libGenImageDownloadJsonWriter.close();
        conn.close();
    }


    private static void callAndGetDescription(LibGenNonFiction response, Connection conn) throws Exception {
        // our SQL SELECT query.
        // if you only need a few columns, specify them by name instead of using "*"

        Statement stat = conn.createStatement();
        String sql = "select descr, toc from description where md5=\"" + response.getMd5Hash().toUpperCase()+"\"";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            String descr = rs.getString("descr");
            String toc = rs.getString("toc");
            response.setDesc(descr);
            response.setToc(toc);
            System.out.println(toc);
            System.out.println(descr);
        }
        rs.close();

    }

    private static Writer getElasticJsonFileWriter() throws IOException {
        StringBuilder pathBaseName = new StringBuilder("elastic_json_desc_out").append("/").append("elastic-libgen-desc-upated")
                .append(System.currentTimeMillis()).append(".json");
        File postProcessedPDFRecordForUIJson = new File(pathBaseName.toString());
        postProcessedPDFRecordForUIJson.getParentFile().mkdirs();
        Writer postProcessedPDFRecordForUIJsonWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(postProcessedPDFRecordForUIJson), StandardCharsets.UTF_8));
        return postProcessedPDFRecordForUIJsonWriter;
    }
}