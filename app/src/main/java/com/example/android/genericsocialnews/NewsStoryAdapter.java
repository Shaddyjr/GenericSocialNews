package com.example.android.genericsocialnews;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for handling NewsStories.
 */
public class NewsStoryAdapter extends ArrayAdapter<NewsStory> {
    private static String LOG_TAG = NewsStoryAdapter.class.getSimpleName();

    public NewsStoryAdapter(@NonNull Context context, List<NewsStory> newsStories) {
        super(context, 0, newsStories);
    }


    private class ViewHolder{
        TextView sectionView;
        TextView titleView;
        ImageView imageView;
        TextView readtimeView;
        TextView trailTextView;
        TextView authorAndDateView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.sectionView       = convertView.findViewById(R.id.section);
            viewHolder.titleView         = convertView.findViewById(R.id.title);
            viewHolder.imageView         = convertView.findViewById(R.id.img);
            viewHolder.readtimeView      = convertView.findViewById(R.id.readTime);
            viewHolder.trailTextView     = convertView.findViewById(R.id.trailText);
            viewHolder.authorAndDateView = convertView.findViewById(R.id.authorAndDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsStory newsStory = getItem(position);

        //SECTION
        viewHolder.sectionView.setText(newsStory.getSection());

        //TITLE
        viewHolder.titleView.setText(newsStory.getTitle());

        //IMAGE
        viewHolder.imageView.setImageBitmap(null);

        if (newsStory.hasBitmap()) {
            Bitmap bitmap = newsStory.getBitmap();
            viewHolder.imageView.setImageBitmap(bitmap);
            viewHolder.imageView.setVisibility(View.VISIBLE);
        } else {
            String imageURL = newsStory.getThumbnail();
            if (!TextUtils.isEmpty(imageURL)) {
                viewHolder.imageView.setVisibility(View.VISIBLE);

                // Adding image load on new thread
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.download(newsStory, viewHolder.imageView);
            } else {
                viewHolder.imageView.setVisibility(View.GONE);
            }
        }

        //READTIME
        String readTimeString = getContext().getResources().getString(R.string.readTime, getReadTime(newsStory.getWordCount()));
        viewHolder.readtimeView.setText(readTimeString);

        //TRAILTEXT
        // getting rid of HTML elements
        String trailText = android.text.Html.fromHtml(newsStory.getTrailText()).toString();
        viewHolder.trailTextView.setText(trailText);

        //AUTHOR AND DATE
        String date = getDate(newsStory.getDate());
        String author = newsStory.getAuthor();
        String authorAndDateString;
        if (author == null) {
            authorAndDateString = getContext().getResources().getString(R.string.date, date);
        } else {
            authorAndDateString = getContext().getResources().getString(R.string.authorAndDate, newsStory.getAuthor(), date);
        }

        viewHolder.authorAndDateView.setText(authorAndDateString);

        return convertView;
    }

    /**
     * Returns formatted date from ISO string.
     */
    private String getDate(String str) {
        String[] arr = str.split("T");
        DateFormat formatter = new SimpleDateFormat("yyyy-mm-d", Locale.US);
        Date date = null;
        try {
            date = formatter.parse(arr[0]);
        } catch (java.text.ParseException e) {
            Log.e(LOG_TAG, "Could not parse date", e);
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.US);

        return dateFormatter.format(date);
    }

    /**
     * Average person can read 130wpm.
     */
    private double getReadTime(int wordCount) {
        return wordCount / 130.0;
    }
}
