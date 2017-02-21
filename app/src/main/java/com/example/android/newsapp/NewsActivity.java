package com.example.android.newsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int NEWS_LOADER_ID = 1;

    private static final String NEWS_SEARCH_REQ = "https://content.guardianapis.com/search?q=debate%20AND%20economy&tag=politics/politics&from-date=2014-01-01&api-key=test";
            //"https://content.guardianapis.com/search?q=";
    private NewsAdapter mAdapter;
    //private EditText mBookSearch;
    //private String mBookQuery;
    //private String query;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    private static final String LOG_TAG = NewsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ListView articleListView = (ListView)findViewById(R.id.result_list);
        mEmptyStateTextView = (TextView)findViewById(R.id.empty_text);
        articleListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        articleListView.setAdapter(mAdapter);
        View loadingIndicator = findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, NewsActivity.this);
            loadingIndicator.setVisibility(View.VISIBLE);
            //hideSoftKeyboard(NewsActivity.this);



        } else {

            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView = (TextView)findViewById(R.id.empty_text);
            mEmptyStateTextView.setText(R.string.no_internet_connection);

            return;
        }
    }

   /* public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }*/

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this,NEWS_SEARCH_REQ);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> articles) {

        View loadingIndicator = findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);

        if (articles != null && !articles.isEmpty()){
            mAdapter.clear();
            mAdapter.addAll(articles);

        }else {
            mEmptyStateTextView.setText(R.string.no_data_found);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        mAdapter.clear();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
