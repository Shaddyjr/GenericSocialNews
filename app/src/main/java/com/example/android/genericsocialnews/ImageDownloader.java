package com.example.android.genericsocialnews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;


/**
 * Responsible for handling downloading images for ListView list items.
 *
 * @see "https://android-developers.googleblog.com/2010/07/multithreading-for-performance.html"
 */
public class ImageDownloader {

    /**
     * Commences image download as long as the newsStory is the same as the one associated with the given imageView.
     */
    public void download(NewsStory newsStory, ImageView imageView) {
        if (cancelDownload(newsStory, imageView)) {
            DownloadImageTask task = new DownloadImageTask(imageView);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(newsStory);
        }
    }

    /**
     * Returns true if the current download should be cancelled.
     * This should only really occur if the user is quickly scrolling and a recycled imageView needs a more current image.
     */
    private static boolean cancelDownload(NewsStory newsStory, ImageView imageView) {
        DownloadImageTask task = getDownloaderTask(imageView);

        if (task != null) {
            NewsStory taskNewsStory = task.newsStory;
            if ((taskNewsStory == null) || (taskNewsStory != newsStory)) {
                task.cancel(true);
            } else {
                // same NewStory, so don't cancel
                return false;
            }
        }

        return true;
    }

    /**
     * Responsible for handling the background task.
     * Note the use of WeakReference to handle memory leaks.
     */
    public class DownloadImageTask extends AsyncTask<NewsStory, Void, Bitmap> {
        private String LOG_TAG = DownloadImageTask.class.getSimpleName();
        private final WeakReference<ImageView> imageViewRef;
        private NewsStory newsStory;

        public DownloadImageTask(ImageView imageView) {
            super();
            imageViewRef = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(NewsStory... newsStories) {
            newsStory = newsStories[0];
            String url = newsStory.getThumbnail();
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (java.net.MalformedURLException e) {
                Log.e(LOG_TAG, "Could not properly form image URL", e);
            } catch (java.io.IOException e) {
                Log.e(LOG_TAG, "IO exception", e);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (isCancelled()) bitmap = null;
            if (imageViewRef != null) {
                ImageView imageView = imageViewRef.get();
                DownloadImageTask downloadImageTask = getDownloaderTask(imageView);
                newsStory.setBitmap(bitmap); // image cached within NewStory object
                if (this == downloadImageTask) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

    }

    /**
     * Helper that returns the task associated with the imageView.
     */
    private static DownloadImageTask getDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getTask();
            }
        }
        return null;
    }

    /**
     * Custom class that will act as placeholder until image downloaded.
     * This also stores task reference.
     */
    public class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<DownloadImageTask> taskRef;

        public DownloadedDrawable(DownloadImageTask task) {
            super(Color.BLACK); // Black background until loaded
            taskRef = new WeakReference<>(task);
        }

        /**
         * Returns the stored task
         */
        public DownloadImageTask getTask() {
            return taskRef.get();
        }
    }


}
