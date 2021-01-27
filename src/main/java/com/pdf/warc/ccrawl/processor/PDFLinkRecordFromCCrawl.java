package com.pdf.warc.ccrawl.processor;

public class PDFLinkRecordFromCCrawl
{
/*
 * {"topDmn":"rutgers.edu","dmn":"16dayscwgl.rutgers.edu","sz":"374845","pgCnt":"2",
 * "yr":"2014","flNm":"Gender-Based Violence \u0026 Women Human Rights
 *  Defenders-NL.pdf filename*\u003dUTF-8\u0027\u0027Gender-Based%20Violence%20%26%20Women%20Huma
 *  n%20Rights%20Defenders-NL.pdf","url":"http://16dayscwgl.rutgers.edu/downloads/2014
 *  -campaign-docs/1267-gender-based-violence-women-human-rights-defenders-nl/file"}
 */
	
	private String webPageUrl;

	private String pdfFullURL;

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	private String linkText;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private String category;

	public String getWebPageUrl() {
		return webPageUrl;
	}

	public void setWebPageUrl(String webPageUrl) {
		this.webPageUrl = webPageUrl;
	}

	public String getPdfFullURL() {
		return pdfFullURL;
	}

	public void setPdfFullURL(String pdfFullURL) {
		this.pdfFullURL = pdfFullURL;
	}
}
