package com.useless;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SearchTextcount {
	private static String[] mustTitleArray =
		{ "founder", "president", "ceo", "chief executive officer", "cto", "chief technology officer", "managing director",
				"vp", "director", "manager" };
	public static void main(String[] args) throws IOException {

		Document doc = Jsoup.connect("http://www.blurb.com/executive-team").get();
	        Map<String, Word> countMap = new HashMap<String, Word>();

	        String text = doc.body().text();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] words = line.split("[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+");
	            for (String word : words) {
	                if ("".equals(word)) {
	                    continue;
	                }

	                Word wordObj = countMap.get(word.toLowerCase());
	                if (wordObj == null) {
	                    wordObj = new Word();
	                    wordObj.word = word;
	                    wordObj.count = 0;
	                    countMap.put(word, wordObj);
	                }
	                wordObj.count++;
	            }
	        }

	        reader.close();

	        SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());
	        int i = 0;
	       
	        String[] wordsToExtract = { "founder", "president", "ceo", "chief executive officer", "cto", "chief technology officer", "managing director","vp", "director", "manager" };
	        
	        for (Word word : sortedWords) {
	        	//System.out.println(word.count + "\t" + word.word);
	            if (Arrays.asList(wordsToExtract).contains(word.word.toLowerCase())) {
	            	
	            	//doc.getElementsContainingOwnText(word.word);
	            	 System.out.println(word.count + "\t" + word.word);
	            	 
	                i++;
	             
	            } else {
	               // System.out.println(word.count + "\t" + word.word);
	                i++;
	            }

	        }
	    }

	    public static class Word implements Comparable<Word> {
	        String word;
	        int count;

	        @Override
	        public int hashCode() { return word.hashCode(); }

	        @Override
	        public boolean equals(Object obj) { return word.equals(((Word)obj).word); }

	        public int compareTo(Word b) { return b.count; }
	    }

	 

}
