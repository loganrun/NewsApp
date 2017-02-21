package com.example.android.newsapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by loganrun on 2/20/17.
 */
public class NewsAdapter extends ArrayAdapter <News> {

    public NewsAdapter(Context context, ArrayList<News> articles) {

        super(context, 0, articles);

    }


}
