package com.useless.links;

import java.util.Set;

public class LinkedPDFRecord
{
	private String topDmn; 
	
	private String url;
	
	private Set<String> lnkPdfs;

	public String getTopDmn()
	{
		return topDmn;
	}

	public void setTopDmn(String topDmn)
	{
		this.topDmn = topDmn;
	}
 
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Set<String> getLnkPdfs()
	{
		return lnkPdfs;
	}

	public void setLnkPdfs(Set<String> lnkPdfs)
	{
		this.lnkPdfs = lnkPdfs;
	}

}
