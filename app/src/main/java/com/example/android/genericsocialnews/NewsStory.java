package com.example.android.genericsocialnews;

public class NewsStory {

    private Integer wordCount;
    private String
                title,
                section,
                date,
                author,
                trailText,
                thumbnail,
                url;

    public NewsStory(Integer count, String... args) {
        wordCount   = count;
        title       = args[0];
        section     = args[1];
        date        = args[2];
        author      = args[3];
        trailText   = args[4];
        thumbnail   = args[5];
        url         = args[6];
    }

    /**
     * Returns the wordCount.
     */
    public Integer getWordCount() {
        return wordCount;
    }

    /**
     * Returns the author(s).
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the section.
     */
    public String getSection() {
        return section;
    }

    /**
     * Returns the thumbnail.
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the trailText.
     */
    public String getTrailText() {
        return trailText;
    }

    /**
     * Returns the url.
     */
    public String getUrl() {
        return url;
    }

}
