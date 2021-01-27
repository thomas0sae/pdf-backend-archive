package com.useless;

public enum SocialMediaSearchConf
{
  FACEBOOK("facebook.com|fb.com", true, "plugins|sharer|post|media/set|photos.php|photo.php"),
  LINKEDIN("linkedin.com|linked.in", true, null),
  /*LINKEDIN("^https:\\/\\/[a-z]{2,3}\\.linkedin\\.com\\/.*$", false, null), */
  TWITTER("twitter.com", false, "search|status|intent"),
  YOUTUBE("youtube.com|youtu.be", true, "watch"),
  GOOGLE("plus.google", false, "share"),
  INSTAGRAM("instagram.com", false, null),
  PINTEREST("pinterest.com", false, "pin"),
  TUMBLR("tumblr.com", false, null),
  APPLE("itunes.apple.com", false, null),
  GPLAY("play.google.com", false, null),
  WINDOWSPHONE("windowsphone.com", false, null),
  REDDIT("reddit.com", false, null),
  GITHUB("github.com", false, null),
  MEDIUM("medium.com", false, null),
  VIMEO("vimeo.com", false, null),
  SLACK("slack.com", false, null),
  YELP("yelp.com", false, null);

  private String regex;
  private boolean isRegex;
  private String excludeKeywords;

  private SocialMediaSearchConf(String reg, boolean isRegex, String keywords) {
    this.regex = reg;
    this.isRegex = isRegex;
    this.excludeKeywords = keywords;
  }

  public String getExcludeKeywords() {
    return this.excludeKeywords;
  }

  public String getRgex()
  {
    return this.regex;
  }

  public boolean getIsRegex()
  {
    return this.isRegex;
  }
}