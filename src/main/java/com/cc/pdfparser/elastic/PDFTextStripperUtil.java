package com.cc.pdfparser.elastic;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;

public class PDFTextStripperUtil
{

	private static final String INPUT_DIRECTORY = "F:\\everything_pdf\\source-code\\java-all-pdf-work\\pdf-imageutil\\beauty\\";
	
	/**
	 * @param fileName
	 * @return complete file data as string
	 * @throws NullPointerException
	 *             if fileName is nullvl
	 */
	public static Optional<String> getDataAsString(final String fileName)
	{
		if (Objects.isNull(fileName))
		{
			throw new NullPointerException("fileName shouldn't be null");
		}

		try (final PDDocument pdDoc = PDDocument.load(new File(fileName)))
		{
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setLineSeparator("\n");
			stripper.setAddMoreFormatting(true);
			PDDocumentInformation info = pdDoc.getDocumentInformation();
			Set<String> metaDataKeys = info.getMetadataKeys();
			for(String key :metaDataKeys)
			{
				System.out.format("%s , %s",key, info.getPropertyStringValue(key));
			}
			System.out.println( "Author=" + info.getAuthor() );
			System.out.println( "Subject=" + info.getSubject() );
			System.out.println( "Keywords=" + info.getKeywords() );
			System.out.println( "Creator=" + info.getCreator() );
			System.out.println( "Producer=" + info.getProducer() );
			System.out.println( "Creation Date=" + info.getCreationDate().get(Calendar.YEAR));
			System.out.println( "Modification Date=" + info.getModificationDate());
			System.out.println( "Trapped=" + info.getTrapped());
			return Optional.of(stripper.getText(pdDoc));

		} catch (IOException e)
		{
			System.out.println(e.getMessage());
			return Optional.empty();
		}

	}

	public static Optional<String> getDataAsString(PDDocument pdDoc, final int startPage, final int endPage)
	{
		try
		{
			PDFTextStripper stripper = new PDFTextStripper();
			//stripper.setLineSeparator("\n");
			stripper.setAddMoreFormatting(true);
			stripper.setStartPage(startPage);
			stripper.setEndPage(endPage);
			String text = stripper.getText(pdDoc);
			//return Optional.of(removeDups(text.trim()));
			return Optional.of(stripper.getText(pdDoc).trim()
					.replaceAll("\n", "::")
					.replaceAll("\r", "::")
					.replaceAll("[^\\w\\s]", " ")
					.replaceAll("\t", " ")
					.replaceAll("--", "-")
					.replaceAll("__", "-")
					.replaceAll("    ", " ")
					.replaceAll("   ", " ")
					.replaceAll("  ", " ")
					.replaceAll(" +", " "));

		} catch (IOException e)
		{
			System.out.println(e.getMessage());
			return Optional.empty();
		}
	}

	public static String removeDups(String s)
	{
	    if(s == null) return "";
		if ( s.length() <= 1 ) return s;
		if( s.substring(1,2).equals(s.substring(0,1)) ) return removeDups(s.substring(1));
		else return s.substring(0,1) + removeDups(s.substring(1));
	}


 	public static Optional<Integer> getNumberOfPages(String fileName)
	{
		if (Objects.isNull(fileName))
		{
			throw new NullPointerException("fileName shouldn't be null");
		}
		try (PDDocument pdDoc = PDDocument.load(new File(fileName)))
		{
			return Optional.of(pdDoc.getNumberOfPages());
		} catch (IOException e)
		{
			System.out.println(e.getMessage());
			return Optional.empty();
		}
	}
	public static void printMetaData(String fileName) throws IOException
	{
		PDDocument pdDoc = PDDocument.load(new File(fileName));
		Optional<String> data2 = PDFTextStripperUtil.getDataAsString(pdDoc, 1, 5);
		if (data2.isPresent())
		{
			String textData = data2.get();
			System.out.println("textData "+ textData);
			List<String> textToAnalyze = new ArrayList<String>();
			textToAnalyze.add(textData);
			final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
			final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(textToAnalyze);
			frequencyAnalyzer.setWordFrequenciesToReturn(5);
			frequencyAnalyzer.setMinWordLength(4); 
			if(wordFrequencies.size() > 0)
			System.out.println("wordFrequencies "+wordFrequencies.get(0));
		}
		
		try
		{
			PDDocumentInformation info = pdDoc.getDocumentInformation();
			/*System.out.println( "Title=" + info.getTitle() );
			System.out.println( "Author=" + info.getAuthor() );
			System.out.println( "Subject=" + info.getSubject() );
			System.out.println( "Keywords=" + info.getKeywords() );
			System.out.println( "Creator=" + info.getCreator() );
			System.out.println( "Producer=" + info.getProducer() );*/
			System.out.println( "Creation Date=" + info.getCreationDate().get(Calendar.YEAR));
			/*System.out.println( "Modification Date=" + info.getModificationDate());
			System.out.println( "Trapped=" + info.getTrapped() );*/
			pdDoc.close();

		} catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		  
	}

	 
	
	public static void main(String args[]) throws IOException {

		
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9780241254707_Eat_Beautiful_594e.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9780756626181_Sleep_dd3a.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9780823099696_Eye_Candy_afae.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781402786129_Fabulous_Teen_Hairstyles_8d2d.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781408129944_Haircare_2105.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781440545177_DIY_Nail_Art_d28c.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781440567391_DIY_Braids_5f21.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781452143088_Vintage_Hairstyles_d6f1.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781465416070_10_Minute_Makeup_0020.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781465454379_Essential_Oils_3ad4.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781465461971_1001_Ways_to_Stay_Young_Naturally_dc96.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781465485045_Trigger_Points_8bbb.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781497200708_Teach_Yourself_Henna_Tattoo_da51.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781612437286_Twist_Me_Pretty_Braids_c35a.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781615646548_Reflexology_Idiots_Guides_5de3.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781616288013_The_Art_of_Hair_db3f.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781631581199_My_Anti_Stress_Year_7064.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"9781633222410_Mehndi_for_the_Inspired_Artist_7d68.pdf");
		PDFTextStripperUtil.printMetaData(INPUT_DIRECTORY+"B07RYG8CG7_125_Fabulous_Full_Hand_Heena_Tattoo_Designs_abcd.pdf");
	}


}