package com.example.android.genericsocialnews;

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
import java.util.StringJoiner;

/**
 * Utility class that handles fetching data from API.
 */
public class NewsUtil {
    private static String LOG_TAG                 = NewsUtil.class.getSimpleName();
    private static final int READ_TIMOUT          = 10000;
    private static final int CONNECTION_TIMOUT    = 15000;

//    PARSING JSON
    private static final String RESPONSE            = "response";
    private static final String RESULTS             = "results";
    private static final String FIELDS              = "fields";
    private static final String WEBTITLE            = "webTitle";
    private static final String SECTIONNAME         = "sectionName";
    private static final String WEBPUBLICATIONDATE  = "webPublicationDate";
    private static final String TAGS                = "tags";
    private static final String TRAILTEXT           = "trailText";
    private static final String THUMBNAIL           = "thumbnail";
    private static final String WORDCOUNT           = "wordcount";
    private static final String WEBURL              = "webUrl";
    private static final String DELIMITER           = " and ";

    /**
     * Returns List of NewsStory objects parsed from API call.
     */
    public static List<NewsStory> fetchNewsData(String urlString) {
        URL url = createUrl(urlString);

        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not properly close connection", e);
        }

        return extractNewsStoriesFromJSON(jsonResponse);
    }

    /**
     * Returns string as proper URL object.
     */
    private static URL createUrl(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL not properly formed", e);
        }
        return url;
    }

    /**
     * Makes HTTP request and returns the JSON as a String.
     */
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() ==  java.net.HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem getting JSON.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // this may throw IOException, so much be handled
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Converts stream into string of whole JSON response.
     */
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

    /**
     * Parses input JSON string into List of NewsStory objects.
     */
    private static List<NewsStory> extractNewsStoriesFromJSON(String json) {
        if (TextUtils.isEmpty(json)) return null;

        ArrayList<NewsStory> newsStories = new ArrayList<>();

        try {
            JSONObject data = new JSONObject(json);
            JSONArray results = data.optJSONObject(RESPONSE).optJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject newsObj = results.optJSONObject(i);
                JSONObject extras = newsObj.optJSONObject(FIELDS);


                // REQUIRED
                String title = newsObj.optString(WEBTITLE);
                String section = newsObj.optString(SECTIONNAME);

                // MUST INCLUDE IF EXISTS
                // publication data
                String date = newsObj.optString(WEBPUBLICATIONDATE);

                //author(s)
                JSONArray tags = newsObj.optJSONArray(TAGS);
                String author = null;
                if (tags.length() > 0) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        StringJoiner joiner = null;
                        joiner = new StringJoiner(DELIMITER);
                        for (int j = 0; j < tags.length(); j++) {
                            JSONObject authorObj = tags.optJSONObject(j);
                            joiner.add(authorObj.optString(WEBTITLE));
                        }
                        author = joiner.toString();
                    } else {
                        author = tags.optJSONObject(0).optString(WEBTITLE);
                    }

                }

                // EXTRAS
                String trailText = extras.optString(TRAILTEXT);
                String thumbnail = null;
                if (extras.has(THUMBNAIL)) {
                    thumbnail = extras.optString(THUMBNAIL);
                }
                Integer wordCount = extras.optInt(WORDCOUNT);
                String url = newsObj.optString(WEBURL);
                newsStories.add(new NewsStory(wordCount, title, section, date, author, trailText, thumbnail, url));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsons JSON", e);
        }

        return newsStories;
    }
}
