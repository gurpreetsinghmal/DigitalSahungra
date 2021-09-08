package com.sahungra.digitalsahungra;

import java.util.Date;

public class News_list {
    String title,desc,url,source,link_url;
    String date;

    public News_list(){

    }
    public News_list(String title, String desc, String url, String source,String link_url,String date) {
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.source = source;
        this.link_url=link_url;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
