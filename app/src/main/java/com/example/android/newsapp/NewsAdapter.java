package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by loganrun on 2/20/17.
 */
public class NewsAdapter extends ArrayAdapter <News> {

    public NewsAdapter(Context context, ArrayList<News> articles) {

        super(context, 0, articles);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.articleTitle);
        titleView.setText(currentNews.getTitle());

        TextView authorView = (TextView) listItemView.findViewById(R.id.date);
        authorView.setText(currentNews.getName());

        //TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        //dateView.setText(currentBook.getDate());



        return listItemView;
    }


}
