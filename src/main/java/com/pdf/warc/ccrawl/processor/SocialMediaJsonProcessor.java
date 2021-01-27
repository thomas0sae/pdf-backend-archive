package com.pdf.warc.ccrawl.processor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SocialMediaJsonProcessor {
    /**
     * @param args
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private static Writer postProcessedPDFRecordForUIJsonWriter;
    private static MessageDigest md;
    static {
        try {
            md = MessageDigest.getInstance("MD5");
            postProcessedPDFRecordForUIJsonWriter = getElasticJsonFileWriter();
        } catch (Exception nse) {
            nse.printStackTrace();
        }
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

    private static void readAndProcessNonFictionJSON(File jsonFileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(jsonFileName));
        String st;
        while ((st = br.readLine()) != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(st, JsonObject.class);
            String domain = jsonObject.get("domain").getAsString();
            System.out.println("Domain "+domain);
            String md5 = (new HexBinaryAdapter()).marshal(md.digest(domain.getBytes())).toLowerCase();
            System.out.println("md5 "+md5);
            postProcessedPDFRecordForUIJsonWriter.append("{\"index\" : {\"_id\":\"" + md5 + "\"}}").append("\r\n");
            postProcessedPDFRecordForUIJsonWriter.append(st).append("\r\n");
            postProcessedPDFRecordForUIJsonWriter.flush();
        }
    }

    private static Writer getElasticJsonFileWriter() throws IOException {
        File postProcessedPDFRecordForUIJson = new File("social-elastic-upload" + System.currentTimeMillis() + ".json");
        Writer postProcessedPDFRecordForUIJsonWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(postProcessedPDFRecordForUIJson), StandardCharsets.UTF_8));
        return postProcessedPDFRecordForUIJsonWriter;
    }

    public static void main(String[] args) throws Exception {
        final File folder = new File(args[0]);
        listFilesForFolder(folder);
     }
}
