package com.example.android.genericsocialnews;

import android.app.LoaderManager;
import android.arch.core.BuildConfig;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Main activity.
 */
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>> {

    final private String GUARDIAN_URL = "https://content.guardianapis.com/search";
    private String API_KEY = com.example.android.genericsocialnews.BuildConfig.ApiKey;
    private int REQUEST_LIMIT = 50;
    private static final int NEWS_STORY_LOADER_ID = 0;

    private ListView listView;
    private ProgressBar progressBar;
    private TextView emptyView;
    private NewsStoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsstory_activity);

        progressBar = findViewById(R.id.progBar);
        emptyView = findViewById(R.id.empty);
        // if no internet, report to user and do nothing
        if (!checkNetworkConnection()) {
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.noInternet);
            return;
        }

        listView = findViewById(R.id.list);
        listView.setEmptyView(emptyView);

        mAdapter = new NewsStoryAdapter(this, new ArrayList<NewsStory>());
        listView.setAdapter(mAdapter);

        // Setting on item click listener to open url of news story
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsStory newsStory = mAdapter.getItem(position);
                String url = newsStory.getUrl();

                // ensure's url is parsed properly
                Uri newsStoryUri = Uri.parse(url);

                // creating intent
                Intent intent = new Intent(Intent.ACTION_VIEW, newsStoryUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.noBrowser), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // loader that actually handles the http request from API
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_STORY_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {
        return new NewsStoryLoader(NewsActivity.this, getUri());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
        mAdapter.clear();

        if (newsStories != null && !newsStories.isEmpty()) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String orderBy = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_default_order_by_value));

            // in case user wants list ordered by readTime
            if (orderBy.equals(getString(R.string.settings_order_by_readTime_value))) {
                /**
                 * Comparator to sort by read time (which is derived from word count).
                 */
                class sortByReadTime implements Comparator<NewsStory> {
                    @Override
                    public int compare(NewsStory newsStory, NewsStory t1) {
                        return newsStory.getWordCount() - t1.getWordCount();
                    }
                }
                Collections.sort(newsStories, new sortByReadTime());
            } else {
                // Oddly, default order by newest still returns articles in odd order
                class sortByDate implements Comparator<NewsStory> {
                    private String LOG_TAG = this.getClass().getSimpleName();

                    @Override
                    public int compare(NewsStory newsStory, NewsStory t1) {
                        return getDate(t1.getDate()).compareTo(getDate(newsStory.getDate()));
                    }

                    private Date getDate(String str) {
                        String[] arr = str.split("T");
                        DateFormat formatter = new SimpleDateFormat("yyyy-mm-d", Locale.US);
                        Date date = null;
                        try {
                            date = formatter.parse(arr[0]);
                        } catch (java.text.ParseException e) {
                            Log.e(LOG_TAG, "Could not parse date", e);
                        }
                        return date;
                    }
                }
                Collections.sort(newsStories, new sortByDate());
            }
            mAdapter.addAll(newsStories);
        }

        emptyView.setText(R.string.empty);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        mAdapter.clear();
    }

    /**
     * Helper method that returns true if the network is connected.
     */
    private boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Returns the fully constructed URL.
     */
    private String getUri() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_default_section_value));

        Uri baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("page-size", String.valueOf(REQUEST_LIMIT));
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("show-fields", "trailText,thumbnail,wordcount");
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        return uriBuilder.toString();
    }
}
