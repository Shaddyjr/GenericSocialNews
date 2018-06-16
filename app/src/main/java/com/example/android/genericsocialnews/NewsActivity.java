package com.example.android.genericsocialnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewsActivity extends AppCompatActivity {

    private static String GUARDIAN_URL = "https://content.guardianapis.com/search?api-key=ddff30e5-4cd8-4c47-baf3-548367aa8cb2&section=society&&page-size=50&format=json&show-fields=trailText,thumbnail,wordcount,starRating&show-tags=contributor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
