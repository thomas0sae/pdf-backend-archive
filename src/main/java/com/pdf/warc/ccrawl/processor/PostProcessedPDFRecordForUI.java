package com.pdf.warc.ccrawl.processor;

import java.util.Calendar;
import java.util.Date;

public class PostProcessedPDFRecordForUI {
    /*
     * {"topDmn":"rutgers.edu","dmn":"16dayscwgl.rutgers.edu","sz":"374845","pgCnt":"2",
     * "yr":"2014","flNm":"Gender-Based Violence \u0026 Women Human Rights
     *  Defenders-NL.pdf filename*\u003dUTF-8\u0027\u0027Gender-Based%20Violence%20%26%20Women%20Huma
     *  n%20Rights%20Defenders-NL.pdf","url":"http://16dayscwgl.rutgers.edu/downloads/2014
     *  -campaign-docs/1267-gender-based-violence-women-human-rights-defenders-nl/file"}
     */

    private String category;
    private String cpRit;
    private String title;
    private String author;
    private String desc;//Text which is say 10 or 20 lines.
    private String flNm;
    private String foundUrl;
    private String imgFlPth;
    private long ixDate = Calendar.getInstance().getTimeInMillis();
    private String language;
    private String lnkTxt;
    private long pgCnt;
    private long sz;
    private String keywords; //comma separated
    private String url;
    private long yr;
    private String dwnLdCnt;
    private int rtng;
    private Boolean prevwOk = true;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRtng() {
        return rtng;
    }

    public void setRtng(int rtng) {
        this.rtng = rtng;
    }

    public String getDwnLdCnt() {
        return dwnLdCnt;
    }

    public void setDwnLdCnt(String dwnLdCnt) {
        this.dwnLdCnt = dwnLdCnt;
    }

    public Boolean getPrevwOk() {
        return prevwOk;
    }

    public void setPrevwOk(Boolean prevwOk) {
        this.prevwOk = prevwOk;
    }

    public String getLnkTxt() {
        return lnkTxt;
    }

    public void setLnkTxt(String lnkTxt) {
        this.lnkTxt = lnkTxt;
    }

    public long getIxDate() {
        return ixDate;
    }

    public void setIxDate(long ixDate) {
        this.ixDate = ixDate;
    }

    public String getCpRit() {
        return cpRit;
    }

    public void setCpRit(String cpRit) {
        this.cpRit = cpRit;
    }

    public long getSz() {
        return sz;
    }

    public void setSz(long sz) {
        this.sz = sz;
    }

    public long getPgCnt() {
        return pgCnt;
    }

    public void setPgCnt(long pgCnt) {
        this.pgCnt = pgCnt;
    }

    public long getYr() {
        return yr;
    }

    public void setYr(long yr) {
        this.yr = yr;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFoundUrl() {
        return foundUrl;
    }

    public void setFoundUrl(String foundUrl) {
        this.foundUrl = foundUrl;
    }

    public String getFlNm() {
        return flNm;
    }

    public void setFlNm(String flNm) {
        this.flNm = flNm;
    }

    public String getImgFlPth() {
        return imgFlPth;
    }

    public void setImgFlPth(String imgFlPth) {
        this.imgFlPth = imgFlPth;
    }
}
