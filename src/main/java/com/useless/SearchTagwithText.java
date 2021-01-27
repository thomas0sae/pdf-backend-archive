package com.useless;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.jsoup.nodes.Element;




public class SearchTagwithText {
	private static String[] mustTitleArray =
		{ "founder", "president", "ceo", "chief executive officer", "cto", "chief technology officer", "managing director","vp", "director", "manager" };
	public static void main(String[] args) throws IOException {
		//String url = "https://www.docusign.com/company/leadership";
		//String url = "http://www.yume.com/about-us/leadership-team";
		//String url = "http://www.kincommunity.com/who-we-are/";
		//String url = "http://rubiconproject.com/leadership/";
		//String url = "https://adblade.com/doc/about-management";
		//String url = "https://freshdesk.com/company/leadership-team";
		//String url = "https://www.knowlarity.com/meet-our-team/";
		String url = 	"http://www.teamleader.eu/team";
		if(args!=null)
		{
			url = args[0];
		}
		//String url = "https://getsatisfaction.com/corp/about/team/";
		
		
		String tagtext = "";
		String tagtextparent = "";
		
		 //Elements elts = doc.select("*:containsOwn(ceo)");
		String test = "";
		Connection.Response response = Jsoup.connect(url)
                .timeout(1000*120)
                .execute();
		int statusCode = response.statusCode();
		if(statusCode == 200) {
			
			Document doc = Jsoup.connect(url).get();
	        Map<String, Word> countMap = new HashMap<String, Word>();
	        doc.getElementsByTag("img").remove();
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
	        String tegtextcheck = "";
	        for (Word word : sortedWords) {
	        	//System.out.println(word.count + "\t" + word.word);
	            if (Arrays.asList(wordsToExtract).contains(word.word.toLowerCase())) {
	            	
	            	//doc.getElementsContainingOwnText(word.word);
	            	//System.out.println(word.count + "\t" + word.word);
	           	 //for(String word : mustTitleArray)
	            	Elements element = doc.select(":containsOwn("+word.word+")").parents();
	            	Elements elements = doc.getElementsMatchingOwnText(word.word).parents().get(0).siblingElements();
	            	Elements divChildren = element.first().children();
	                for (Element elem : divChildren) {
	                	if(moreThanSpaceInString(elem.text()) == false)
	    	        	{                	
	                		System.out.println(elem.tag() + elem.text());
	    	        	}
	                   }
	      for(Element ed : element.parents())
	      {
	    	  //if(moreThanSpaceInString(ed.text()) == false)
	        	//{
          	//System.out.println(ed.tag()+"---<"+ed.text());
	        	//}
          	  
	      }
	            	
	            	
	            	for (Element item : elements) {
	            		//System.out.println(item.text());
	            				//&& item.parent().children().size() == 1 &&  StringUtil.isBlank(item.parent().ownText())
	            		//System.out.println(item.parents().hasAttr(item.tag().toString()) + "-->" + item.text());
	            				if(item.parent().firstElementSibling() != null )
	            		        {
		            				if(test != item.parent().child(0).ownText())
		            				{
			            				if(moreThanSpaceInString(item.ownText()) == false)
			            	        	{
			            					if(moreThanSpaceInString(item.parent().child(0).ownText()) == false)
			            		        	{	
			            						
			            							tagtext = item.ownText();
			            							tagtextparent = item.parent().child(0).text();
			            							
			            							if((tagtext).equals(tagtextparent) )
			            							{
			            								tagtextparent = item.parent().ownText();
			            							}
			            							
			            							if(!tagtextparent.isEmpty()){
			            								if(!(tegtextcheck).equals(tagtextparent))
			            								{
			            									//System.out.println(item.parent().child(0).tag()+"--->"+tagtextparent+ " ---> " + item.tagName() + "--->" + tagtext);
			            								//	System.out.println(url+"|"+tagtextparent +"|" + tagtext);
			            									tegtextcheck = tagtextparent;
			            								}
			            							}
			            		        	}
	            						}
		            				}
	            		        }	
	            			}
	            
	                i++;
	            } else {
	               // System.out.println(word.count + "\t" + word.word);
	                i++;
	            }
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

	private static boolean moreThanSpaceInString(String string) {
	    int numberOfNumbers = 0;
	    for (int i = 0; i < string.length(); i++) {
	        if (Character.isWhitespace(string.charAt(i))) {
	            numberOfNumbers++;
	            if (numberOfNumbers > 6) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
}
