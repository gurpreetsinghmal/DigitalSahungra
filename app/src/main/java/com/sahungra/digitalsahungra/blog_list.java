package com.sahungra.digitalsahungra;

import android.widget.TextView;

import java.util.Date;

public class blog_list {

    String Heading,subtitle,para,url,link;
    int viewtype;

    public blog_list(){

    }

    public blog_list(String heading, String subtitle,String para, String url, String link,int viewtype) {
        Heading = heading;
        this.subtitle = subtitle;
        this.url = url;
        this.link = link;
        this.viewtype=viewtype;
        this.para=para;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public int getViewtype() {
        return viewtype;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
