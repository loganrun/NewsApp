package com.example.android.newsapp;

/**
 * Created by loganrun on 2/20/17.
 */
public class News {
    private String mName;
    private String mTitle;
    private String mWeb;

    public News(String name, String title, String web){
        mName = name;
        mTitle = title;
        mWeb = web;
    }

    public String getname(){return mName;}
    public String gettitle(){return mTitle;}
    public String getweb(){return mWeb;}


}
