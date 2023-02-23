package com.example.newsaggregator;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsAPIArticlesRunnable implements Runnable {
    private static final String TAG = "NewsAPIArticlesRunnable";

    private MainActivity mainActivity;
    private String source;
    private static final String API_KEY = "747532a529ca4fe69a820b3039e62438";
    // https://newsapi.org/v2/top-headlines?sources=______&apiKey=______
    private static final String NEWS_API_ARTICLES_URI = "https://newsapi.org/v2/top-headlines";

    NewsAPIArticlesRunnable() {}

    NewsAPIArticlesRunnable(MainActivity mainActivity, String source) {
        this.mainActivity = mainActivity;
        this.source = source;
    }

    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(NEWS_API_ARTICLES_URI).buildUpon();
        buildURL.appendQueryParameter("sources", source);
        buildURL.appendQueryParameter("apiKey", API_KEY);

        String urlToUse = buildURL.build().toString();
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent", "");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTPS ResponseCode NOT OK: " + conn.getResponseCode() + " , " + conn.getResponseMessage());
                handleResults(null);
                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handleResults(sb.toString());
    }

    private void handleResults(String s) {

        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            mainActivity.runOnUiThread(mainActivity::downloadArticlesFailed);
            return;
        }

        List<Article> articlesList = parseJSON(s);
        mainActivity.runOnUiThread(() -> {
            if (articlesList != null || articlesList.size() > 0) {
                mainActivity.updateArticlesData(articlesList);
            }
        });
    }

    private List<Article> parseJSON(String s) {
        ArrayList<Article> articlesList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("articles");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject article = jsonArray.getJSONObject(i);
                JSONObject source = article.getJSONObject("source");
                ArticleSource articleSource = new ArticleSource(
                        source.getString("id"),
                        source.getString("name")
                );
                String imageUrl = article.getString("urlToImage");
                String url = article.getString("url");
                Drawable drawable = mainActivity.getResources().getDrawable(R.drawable.loading);
//                try {
//                    InputStream is = (InputStream) new URL(imageUrl).getContent();
//                    drawable = Drawable.createFromStream(is, url);
//                } catch (Exception e) {
//                    drawable = mainActivity.getResources().getDrawable(R.drawable.noimage);
//                }
                Article article1 = new Article(
                        articleSource,
                        article.getString("author"),
                        article.getString("title"),
                        article.getString("url"),
                        article.getString("description"),
                        imageUrl,
                        article.getString("publishedAt"),
                        article.getString("content"),
                        drawable
                );
                articlesList.add(article1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return articlesList;
    }
}
