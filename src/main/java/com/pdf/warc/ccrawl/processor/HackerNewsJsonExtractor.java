package com.pdf.warc.ccrawl.processor;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HackerNewsJsonExtractor {
    public static void main(String[] args) throws IOException {
        Writer postProcessedPDFRecordForUIJsonWriter = getElasticJsonFileWriter();
        final File folder = new File("E:\\everything_pdf\\hacker_news_pdf_links\\old");
        for (final File fileEntry : folder.listFiles()) {
            JsonElement gson = new JsonParser().parse(new FileReader(fileEntry.getAbsolutePath()));
            JsonObject hitsElemObj = gson.getAsJsonObject();
            if (!hitsElemObj.isJsonNull() && hitsElemObj instanceof JsonObject) {
                JsonElement hitsElem = hitsElemObj.get("hits");
                if (!hitsElem.isJsonNull() && hitsElem instanceof JsonElement) {
                    System.out.println(fileEntry.getAbsolutePath());
                    JsonArray hitsElemArr = hitsElem.getAsJsonArray();
                    for (JsonElement hArr : hitsElemArr) {
                        JsonObject eachHitsObj = hArr.getAsJsonObject();
                        JsonElement titleObj = eachHitsObj.get("title");
                        JsonElement urlObj = eachHitsObj.get("url");
                        if (!urlObj.isJsonNull() && (urlObj instanceof JsonElement) && !titleObj.isJsonNull() && (titleObj instanceof JsonElement)) {
                            System.out.println(eachHitsObj);
                            PDFLinkRecordFromCCrawl recordFromCCrawl = new PDFLinkRecordFromCCrawl();
                            recordFromCCrawl.setWebPageUrl("");
                            recordFromCCrawl.setPdfFullURL(urlObj.getAsString());
                            recordFromCCrawl.setLinkText(titleObj.getAsString());
                            postProcessedPDFRecordForUIJsonWriter.append((new Gson()).toJson(recordFromCCrawl)).append("\r\n");
                            postProcessedPDFRecordForUIJsonWriter.flush();
                        }
                    }
                }
            }
        }
    }

    private static Writer getElasticJsonFileWriter() throws IOException {
        File postProcessedPDFRecordForUIJson = new File("hacker_news_json_result_" + System.currentTimeMillis() + ".json");
        Writer postProcessedPDFRecordForUIJsonWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(postProcessedPDFRecordForUIJson), StandardCharsets.UTF_8));
        return postProcessedPDFRecordForUIJsonWriter;
    }

    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
