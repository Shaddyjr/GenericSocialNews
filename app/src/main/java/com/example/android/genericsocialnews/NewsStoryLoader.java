package com.example.android.genericsocialnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loader for NewStories.
 */
public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStory>> {
    private String mURL;

    public NewsStoryLoader(Context context, String URL) {
        super(context);
        mURL = URL;
    }

    // Needed to kick off loader
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsStory> loadInBackground() {
        if (mURL.length() < 1) return null;
        return NewsUtil.fetchNewsData(mURL);
    }
}
