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
    private static String LOG_TAG = NewsUtil.class.getSimpleName();

    /**
     * Returns List of NewsStory objects parsed from API call.
     */
    public static List<NewsStory> fetchNewsData(String urlString){
        URL url = createUrl(urlString);

        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG,"Could not properly close connection",e);
        }

        return extractNewsStoriesFromJSON(jsonResponse);
    }

    /**
     * Returns string as proper URL object.
     */
    private static URL createUrl(String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"URL not properly formed", e);
        }
        return url;
    }

    /**
     * Makes HTTP request and returns the JSON as a String.
     */
    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream         = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem getting JSON.", e);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
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
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Parses input JSON string into List of NewsStory objects.
     */
    private static List<NewsStory> extractNewsStoriesFromJSON(String json){
        if(TextUtils.isEmpty(json)) return null;

        ArrayList<NewsStory> newsStories = new ArrayList<>();

        try{
            JSONObject data     = new JSONObject(json);
            JSONArray results   = data.getJSONObject("response").getJSONArray("results");

            for(int i = 0; i < results.length(); i++){
                JSONObject newsObj  = results.getJSONObject(i);
                JSONObject extras   = newsObj.getJSONObject("fields");


                // REQUIRED
                String title    = newsObj.getString("webTitle");
                String section  = newsObj.getString("sectionName");

                // MUST INCLUDE IF EXISTS
                // publication data
                String date = newsObj.getString("webPublicationDate");

                //author(s)
                JSONArray tags  = newsObj.getJSONArray("tags");
                String author   = null;
                if(tags.length()>0){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        StringJoiner joiner = null;
                        joiner = new StringJoiner(" and ");
                        for(int j = 0; j<tags.length();j++){
                            JSONObject authorObj = tags.getJSONObject(j);
                            joiner.add(authorObj.getString("webTitle"));
                        }
                        author = joiner.toString();
                    }else{
                        author = tags.getJSONObject(0).getString("webTitle");
                    }

                }

                // EXTRAS
                String trailText    = extras.getString("trailText");
                String thumbnail    = null;
                if(extras.has("thumbnail")){
                    thumbnail       = extras.getString("thumbnail");
                }
                Integer wordCount   = extras.getInt("wordcount");
                String url          = newsObj.getString("webUrl");
                newsStories.add(new NewsStory(wordCount,title, section, date, author, trailText, thumbnail,url));
            }
        }catch(JSONException e){
            Log.e(LOG_TAG,"Problem parsons JSON", e);
        }

        return newsStories;
    }
}
