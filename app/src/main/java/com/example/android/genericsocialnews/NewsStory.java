package com.example.android.genericsocialnews;

import android.graphics.Bitmap;

/**
 * Model class for news articles.
 */
public class NewsStory {

    private Integer mWordCount;
    private String mTitle;
    private String mSection;
    private String mDate;
    private String mAuthor;
    private String mTrailText;
    private String mThumbnail;
    private String mUrl;
    private Bitmap mBitmap;


    public NewsStory(Integer count, String title, String section, String date, String author, String trailText, String thumbnail, String url) {
        mWordCount = count;
        mTitle     = title;
        mSection   = section;
        mDate      = date;
        mAuthor    = author;
        mTrailText = trailText;
        mThumbnail = thumbnail;
        mUrl       = url;
        mBitmap    = null;
    }

    /**
     * Returns the wordCount.
     */
    public Integer getWordCount() {
        return mWordCount;
    }

    /**
     * Returns the author(s).
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the date.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the section.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the thumbnail.
     */
    public String getThumbnail() {
        return mThumbnail;
    }

    /**
     * Returns the title.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the trailText.
     */
    public String getTrailText() {
        return mTrailText;
    }

    /**
     * Returns the url.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Caches the bitmap.
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    /**
     * Checks if this object has a bitmap.
     */
    public boolean hasBitmap() {
        return mBitmap != null;
    }

    /**
     * Returns bitmap.
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }
}
