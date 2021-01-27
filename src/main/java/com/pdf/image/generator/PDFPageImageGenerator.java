package com.pdf.image.generator;

import com.pdf.image.generator.imageUtil.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFPageImageGenerator {
	private static final String OUT_DIRECTORY = "F:\\everything_pdf\\source-code\\java-all-pdf-work\\pdf-imageutil\\beauty_out\\";
	//private static final String IN_DIRECTORY = "F:\\everything_pdf\\source-code\\java-all-pdf-work\\pdf-imageutil\\beauty\\";
	private static final String IN_DIRECTORY = "C:\\Users\\easothomas\\Desktop\\pdf1\\";

	
	public static void main(String[] args) {
		File directory = new File(IN_DIRECTORY);
		if (!directory.exists())
			throw new IllegalArgumentException("Invalid path to image");
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (int k = 0; k < files.length; k++) {
				System.out.println("files[k] " + files[k].getName());
				printImageFromPDFPage(files[k]);
			}
		}
	}

	public static void printImageFromPDFPage(File file) {
		try (PDDocument pdDoc = PDDocument.load(file)) {
			PDFRenderer pdfRenderer = new PDFRenderer(pdDoc);
			for (int page = 0; page < 1; ++page) {
				BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 120, ImageType.RGB);
				generateThumbNailImagesFromPDFPage(new Image(bim, com.pdf.image.generator.imageUtil.ImageType.JPG), file.getName());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		/*
		 * try { Image img = null; if (imgLocation.startsWith("http")) { // read the
		 * image from a URL img = ImageLoader.fromUrl(new URL(imgLocation)); } else {
		 * File f = new File(imgLocation); if (!f.exists() || !f.isFile()) throw new
		 * IllegalArgumentException("Invalid path to image"); else { // read the image
		 * from a file img = ImageLoader.fromFile(f); }
		 * generateThumbNailImagesFromPDFPage(img); //} } catch (IOException ioe) {
		 * ioe.printStackTrace(); }
		 */
	}

	public static void generateThumbNailImagesFromPDFPage(Image img, File imageFilePath) throws IOException {
		System.out.println("Image dimensions: " + img.getWidth() + "x" + img.getHeight());
		Image resized = img.getResizedToWidth(350);
		softenAndSave(resized, 1.0f, 0.0f, imageFilePath);
		resized.dispose();
		img.dispose();
	}


	public static void generateThumbNailImagesFromPDFPage(Image img, String fileName) throws IOException {
		// output source type
		System.out.println("Image source type: " + img.getSourceType());
		// output dimensions
		System.out.println("Image dimensions: " + img.getWidth() + "x" + img.getHeight());

		/*
		 * // crop it Image cropped = img.crop(200, 200, 500, 350);
		 * cropped.writeToJPG(new File(
		 * "F:\\everything_pdf\\source-code\\java-all-pdf-work\\pdf-imageutil\\beauty_out\\cropped.jpg"
		 * ), 0.95f); cropped.dispose();
		 */

		// resize
		Image resized = img.getResizedToWidth(350);
		// save it with varying softness and quality
		//softenAndSave(resized, 0.95f, 0f, fileName);
		//softenAndSave(resized, 0.95f, 0.1f, fileName);
		//softenAndSave(resized, 0.95f, 0.2f, fileName);
		//softenAndSave(resized, 0.95f, 0.3f, fileName);
		softenAndSave(resized, 1.0f, 0.0f, fileName);
		//softenAndSave(resized, 0.4f, 0.08f, fileName);
		resized.dispose();

		// write a 0.95 quality JPG without using Sun's JPG codec
		//resized.writeToFile(new File("F:\\everything_pdf\\source-code\\java-all-pdf-work\\pdf-imageutil\\beauty_out\\resized--q0.95--s0.0--nocodec.jpg"));

		/*
		 * // resize it to a square with different settings for edge cropping
		 * squareIt(img, 400, 0.0, 0.95f, 0.08f); squareIt(img, 400, 0.1, 0.95f, 0.08f);
		 * squareIt(img, 400, 0.2, 0.95f, 0.08f);
		 * 
		 * // small thumbs squareIt(img, 50, 0.0, 0.95f, 0.08f); squareIt(img, 50, 0.1,
		 * 0.95f, 0.08f); squareIt(img, 50, 0.1, 0.5f, 0.08f);
		 */

	}

	private static void softenAndSave(Image img, float quality, float soften, File imageFilePath) throws IOException {
		System.out.println(imageFilePath.toString());
		img.soften(soften).writeToJPGNew(imageFilePath, quality);
	}

	private static void softenAndSave(Image img, float quality, float soften, String fileName) throws IOException {
		img.soften(soften).writeToFile(new File(OUT_DIRECTORY+fileName.replace(".pdf", "-")+"resized--q" + quality + "--s" + soften + ".png"));
	}
}
