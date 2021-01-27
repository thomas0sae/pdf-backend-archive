package com.useless.mapred;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CSV
{
  private String domain;
  private String url;
  private String fbLinks;
  private String linkedInLinks;
  private String twitterLinks;
  private String googlePlus;
  private String youtube;
  private String instagram;
  private String pinterest;
  private String tumblr;
  private String apple;
  private String gplay;
  private String windowsPhone;
  private String reddit;
  private String yelp;
  private String gitHub;
  private String medium;
  private String slack;
  private String vimeo;
  private String contactDetails;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSlack() {
    return slack;
  }

  public void setSlack(String slack) {
    this.slack = slack;
  }

  public String getVimeo() {
    return vimeo;
  }

  public void setVimeo(String vimeo) {
    this.vimeo = vimeo;
  }

  public String getMedium() {
    return medium;
  }

  public void setMedium(String medium) {
    this.medium = medium;
  }

  public String getGitHub() {
    return gitHub;
  }

  public void setGitHub(String gitHub) {
    this.gitHub = gitHub;
  }

  public String getReddit()
  {
    return this.reddit;
  }

  public void setReddit(String reddit) {
    this.reddit = reddit;
  }

  public String getYelp() {
    return this.yelp;
  }

  public void setYelp(String yelp) {
    this.yelp = yelp;
  }

  public String getInstagram() {
    return this.instagram;
  }

  public void setInstagram(String instagram) {
    this.instagram = instagram;
  }

  public String getPinterest() {
    return this.pinterest;
  }

  public void setPinterest(String pinterest) {
    this.pinterest = pinterest;
  }

  public String getTumblr() {
    return this.tumblr;
  }

  public void setTumblr(String tumblr) {
    this.tumblr = tumblr;
  }

  public String getApple() {
    return this.apple;
  }

  public void setApple(String apple) {
    this.apple = apple;
  }

  public String getGplay() {
    return this.gplay;
  }

  public void setGplay(String gplay) {
    this.gplay = gplay;
  }

  public String getWindowsPhone() {
    return this.windowsPhone;
  }

  public void setWindowsPhone(String windowsPhone) {
    this.windowsPhone = windowsPhone;
  }

  public String getYoutube() {
    return this.youtube;
  }

  public void setYoutube(String youtube) {
    this.youtube = youtube;
  }

  public String getDomain() {
    return this.domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getFbLinks() {
    return this.fbLinks;
  }

  public void setFbLinks(String fbLinks) {
    this.fbLinks = fbLinks;
  }

  public String getLinkedInLinks() {
    return this.linkedInLinks;
  }

  public void setLinkedInLinks(String linkedInLinks) {
    this.linkedInLinks = linkedInLinks;
  }

  public String getTwitterLinks() {
    return this.twitterLinks;
  }

  public void setTwitterLinks(String twitterLinks) {
    this.twitterLinks = twitterLinks;
  }

  public String getGooglePlus() {
    return this.googlePlus;
  }

  public void setGooglePlus(String googlePlus) {
    this.googlePlus = googlePlus;
  }

  public String getContactDetails() {
    return this.contactDetails;
  }

  public void setContactDetails(String contactDetails) {
    this.contactDetails = contactDetails;
  }

  public boolean isEmpty()
  {
    return (isNullOrEmpty(this.fbLinks))
            && (isNullOrEmpty(this.linkedInLinks))
            && (isNullOrEmpty(this.googlePlus))
            && (isNullOrEmpty(this.twitterLinks))
            && (isNullOrEmpty(this.youtube))
            && (isNullOrEmpty(this.contactDetails))
            && (isNullOrEmpty(this.instagram))
            && (isNullOrEmpty(this.pinterest))
            && (isNullOrEmpty(this.tumblr))
            && (isNullOrEmpty(this.reddit))
            && (isNullOrEmpty(this.yelp))
            && (isNullOrEmpty(this.apple))
            && (isNullOrEmpty(this.vimeo))
            && (isNullOrEmpty(this.slack))
            && (isNullOrEmpty(this.medium))
            && (isNullOrEmpty(this.gitHub))
            && (isNullOrEmpty(this.gplay))
            && (isNullOrEmpty(this.windowsPhone));
  }

  private boolean isNullOrEmpty(String s1)
  {
    return (s1 == null) || (s1.isEmpty());
  }

  private void write(String s, DataOutput out) throws IOException
  {
    int sz = 0;
    if (isNullOrEmpty(s))
    {
      out.writeInt(sz);
    }
    else
    {
      byte[] b = s.getBytes();
      sz = b.length;
      out.writeInt(sz);
      out.write(b);
    }
  }

  private String read(DataInput in) throws IOException
  {
    String s = null;
    int sz = in.readInt();
    if (sz == 0)
    {
      return s;
    }

    byte[] b = new byte[sz];
    for (int i = 0; i < sz; i++)
    {
      b[i] = in.readByte();
    }
    s = new String(b);

    return s;
  }

  public String toString()
  {
    return "CSV [domain=" + this.domain + ", fbLinks=" + this.fbLinks + ", linkedInLinks=" + this.linkedInLinks + 
      ", twitterLinks=" + this.twitterLinks + ", googlePlus=" + this.googlePlus + ", youtube=" + this.youtube + 
      ", instagram=" + this.instagram + ", pinterest=" + this.pinterest + ", tumblr=" + this.tumblr + ", apple=" + this.apple + 
      ", gplay=" + this.gplay + ", windowsPhone=" + this.windowsPhone + ", reddit=" + this.reddit + ", yelp=" + this.yelp + 
      ", contactDetails=" + this.contactDetails + "]";
  }
}