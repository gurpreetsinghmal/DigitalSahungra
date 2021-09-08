package com.sahungra.digitalsahungra;

import java.util.Date;
import java.util.List;

public class blog_envelope {

    String name,dp;
    Date date;
    String key;
    List<blog_list> list;
    String phn;

    public blog_envelope()
    {

    }

    public blog_envelope(String name, String dp, Date date, String key, List<blog_list> list,String phn) {
        this.name = name;
        this.dp = dp;
        this.phn=phn;
        this.date = date;
        this.key = key;
        this.list = list;
    }


    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<blog_list> getList() {
        return list;
    }

    public void setList(List<blog_list> list) {
        this.list = list;
    }
}
