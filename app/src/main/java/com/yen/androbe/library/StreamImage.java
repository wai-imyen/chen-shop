package com.yen.androbe.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class StreamImage {

    public String url;
    public ImageView ImageView;

    public StreamImage() {
    }

    public StreamImage(String url, android.widget.ImageView imageView) {
        this.url = url;
        ImageView = imageView;
    }

    public void setImage() {
        new DownloadImageTask(ImageView)
                .execute(this.url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public android.widget.ImageView getImageView() {
        return ImageView;
    }

    public void setImageView(android.widget.ImageView imageView) {
        ImageView = imageView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
