package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loganrun on 2/20/17.
 */
public final class QueryUtils {

    private final static String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
            Log.e(LOG_TAG,"Problem making Http request", e);
        }

        List<News> articles = extractItemFromJson(jsonResponse);


        return articles;


    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            //
            Log.e( LOG_TAG, "Error with creating URL", e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem with stream connection", e);
            Log.e(LOG_TAG, "HTTP response code =" + urlConnection.getResponseCode(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    public static List<News> extractItemFromJson(String articleJson) {
        if (TextUtils.isEmpty(articleJson)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<News> articles = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject baseJsonResponse = new JSONObject(articleJson);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray articleArray = response.getJSONArray("results");


            for (int i = 0; i < articleArray.length(); i++) {

                JSONObject currentArticle = articleArray.getJSONObject(i);
                String name = currentArticle.getString("sectionName");
                String title = currentArticle.getString("webTitle");
                String web = currentArticle.getString("webUrl");

                articles.add(new News(name,title,web));
            }


        }catch(JSONException e){

            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return articles;
    }

}
