package com.sitemap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class SiteMapResourceDownloader {

  public static void main(String argv[]) {

    try {

	File fXmlFile = new File(argv[0]);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
			
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
	NodeList nList = doc.getElementsByTagName("url");
			
	System.out.println("----------------------------");

	for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);
				
		System.out.println("\nCurrent Element :" + nNode.getNodeName());
				
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			System.out.println("loc : " + eElement.getElementsByTagName("loc").item(0).getTextContent());
		}
		NodeList nList1 = ((Element)nNode).getElementsByTagName("image:image");
		for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) {

			Node nNode1 = nList1.item(temp1);
					
			System.out.println("\nCurrent Element :" + nNode1.getNodeName());
					
			if (nNode1.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement1 = (Element) nNode1;

				System.out.println("imageloc : " + eElement1.getElementsByTagName("image:loc").item(0).getTextContent());
				System.out.println("imagecaption : " + eElement1.getElementsByTagName("image:caption").item(0).getTextContent());
				
			}
		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }

}