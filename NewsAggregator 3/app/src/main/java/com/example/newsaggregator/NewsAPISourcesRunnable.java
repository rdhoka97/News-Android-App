package com.example.newsaggregator;

import android.net.Uri;
import android.util.Log;

import com.example.newsaggregator.utility.Utility;

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
import java.util.Locale;

public class NewsAPISourcesRunnable implements Runnable {
    private static final String TAG = "NewsAPIRunnable";
    private MainActivity mainActivity;
    private static final String API_KEY = "747532a529ca4fe69a820b3039e62438";
    // https://newsapi.org/v2/sources?apiKey=________
    private static final String NEWS_API_SOURCES_URI = "https://newsapi.org/v2/sources";

    public NewsAPISourcesRunnable() {}

    public NewsAPISourcesRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(NEWS_API_SOURCES_URI).buildUpon();
        buildURL.appendQueryParameter("apiKey", API_KEY);

        String urlToUse = buildURL.build().toString();
        StringBuilder sb = new StringBuilder();

        Log.d(TAG, "run: URL " + urlToUse);

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
            mainActivity.runOnUiThread(mainActivity::downloadSourcesFailed);
            return;
        }

        List<Source> sourcesList = parseJSON(s);
        mainActivity.runOnUiThread(() -> {
            if (sourcesList != null || sourcesList.size() > 0) {
                mainActivity.updateSourcesData(sourcesList);
            }
        });
    }

    private List<Source> parseJSON(String s) {
        ArrayList<Source> sources = new ArrayList<>();
        String languagesJsonString = Utility.convertJsonToString(mainActivity.getResources(), R.raw.language_codes);
        String countriesJsonString = Utility.convertJsonToString(mainActivity.getResources(), R.raw.country_codes);

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("sources");

            JSONObject languagesJsonObject  = new JSONObject(languagesJsonString);
            JSONArray languagesJsonArray = languagesJsonObject.getJSONArray("languages");

            JSONObject countriesJsonObject  = new JSONObject(countriesJsonString);
            JSONArray countriesJsonArray = countriesJsonObject.getJSONArray("countries");

            String language = null, country = null;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject source = jsonArray.getJSONObject(i);
                for (int j = 0; j < languagesJsonArray.length(); j++) {

                    JSONObject languageObject = languagesJsonArray.getJSONObject(j);
                    if (languageObject.getString("code").toLowerCase(Locale.ROOT).equalsIgnoreCase(source.getString("language"))) {
                        language = languageObject.getString("name");
                    }
                }
                for (int j = 0; j < countriesJsonArray.length(); j++) {
                    JSONObject countryObject = countriesJsonArray.getJSONObject(j);
                    if (countryObject.getString("code").toLowerCase(Locale.ROOT).equalsIgnoreCase(source.getString("country"))) {
                        country = countryObject.getString("name");
                    }
                }
                Source src = new Source(
                        source.getString("id"),
                        source.getString("name"),
                        source.getString("description"),
                        source.getString("url"),
                        source.getString("category"),
                        source.getString("language"),
                        source.getString("country"),
                        language,
                        country
                );
                sources.add(src);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sources;
    }
}
