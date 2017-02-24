package com.example.android.newsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri articleUri = Uri.parse(currentNews.getWeb());

                Intent webIntent = new Intent(Intent.ACTION_VIEW,articleUri);
                startActivity(webIntent);


            }
        });

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String Key) {

        if (Key.equals(getString(R.string.topics_general_key)) ||
                Key.equals(getString(R.string.topics_search_terms_key))){
            mAdapter.clear();

            mEmptyStateTextView.setVisibility(View.GONE);

            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null,this);
        }

    }

   /* public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }*/

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

            SharedPreferences shareprefs = PreferenceManager.getDefaultSharedPreferences(this);
            String generalTopics = shareprefs.getString(
                    getString(R.string.topics_general_key),
                    getString(R.string.topics_general_default));

            String searchItems = shareprefs.getString(
                    getString(R.string.topics_search_terms_key),
                    getString(R.string.topics_search_terms_default));

            Uri baseUri = Uri.parse(NEWS_SEARCH_REQ);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter("section", generalTopics);
            uriBuilder.appendQueryParameter("tag", searchItems);

            return new NewsLoader(this, uriBuilder.toString());

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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent topicsIntent = new Intent(this, TopicsActivity.class);
            startActivity(topicsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
