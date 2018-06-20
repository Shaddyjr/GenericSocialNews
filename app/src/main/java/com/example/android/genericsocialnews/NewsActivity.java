package com.example.android.genericsocialnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity.
 */
public class NewsActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<NewsStory>> {

    private String GUARDIAN_URL = "https://content.guardianapis.com/search?api-key=ddff30e5-4cd8-4c47-baf3-548367aa8cb2&section=society&&page-size=50&format=json&show-fields=trailText,thumbnail,wordcount,starRating&show-tags=contributor";
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
        emptyView   = findViewById(R.id.empty);
        // if no internet, report to user and do nothing
        if(!checkNetworkConnection()){
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.noInternet);
            return;
        }

        listView    = findViewById(R.id.list);
        listView.setEmptyView(emptyView);

        mAdapter    = new NewsStoryAdapter(this, new ArrayList<NewsStory>());
        listView.setAdapter(mAdapter);

        // Setting on item click listener to open url of news story
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsStory newsStory= mAdapter.getItem(position);
                String url = newsStory.getUrl();

                // ensure's url is parsed properly
                Uri newsStoryUri = Uri.parse(url);

                // creating intent
                Intent intent = new Intent(Intent.ACTION_VIEW,newsStoryUri);
                startActivity(intent);
            }
        });

        // loader that actually handles the http request from API
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_STORY_LOADER_ID,null, this);
    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {
        return new NewsStoryLoader(NewsActivity.this, GUARDIAN_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
        mAdapter.clear();

        if(newsStories != null && !newsStories.isEmpty()){
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
    private boolean checkNetworkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
