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

//Great article! https://android-developers.googleblog.com/2010/07/multithreading-for-performance.html
public class ImageDownloader {
    public void download(NewsStory newsStory, ImageView imageView){
        String url = newsStory.getThumbnail();
        if(cancelDownload(url,imageView)){
            DownloadImageTask task = new DownloadImageTask(imageView);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(newsStory);
        }
    }

    private static boolean cancelDownload(String url, ImageView imageView){
        DownloadImageTask  task = getDownloaderTask(imageView);

        if(task != null){
            String oldURL = task.newsStory.getThumbnail();
            if((oldURL==null)||(!oldURL.equals(url))){
                task.cancel(true);
            }
            else{
                // same url, so don't cancel
                return false;
            }
        }

        return true;
    }
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
            newsStory= newsStories[0];
            String url = newsStory.getThumbnail();
            Bitmap bitmap = null;
            try{
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            }catch (java.net.MalformedURLException e){
                Log.e(LOG_TAG,"Could not properly form image URL",e);
            }catch (java.io.IOException e){
                Log.e(LOG_TAG,"IO exception",e);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(isCancelled()) bitmap = null;
            if(imageViewRef != null) {
                ImageView imageView = imageViewRef.get();
                DownloadImageTask downloadImageTask = getDownloaderTask(imageView);
                if (this == downloadImageTask) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

    }

    private static DownloadImageTask getDownloaderTask(ImageView imageView){
        if(imageView != null){
            Drawable drawable = imageView.getDrawable();
            if(drawable instanceof DownloadedDrawable){
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
        public DownloadImageTask getTask(){
            return taskRef.get();
        }
    }


}
