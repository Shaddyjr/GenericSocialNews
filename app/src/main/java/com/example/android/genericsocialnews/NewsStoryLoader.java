package com.example.android.genericsocialnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStory>> {
    public NewsStoryLoader(Context context) {
        super(context);
    }

    @Override
    public List<NewsStory> loadInBackground() {
        return null;
    }
}
