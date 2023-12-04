package com.example.downloadimgs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView)findViewById(R.id.imageView);
    }
    public void downloadImage(View view){
        imgDownload newImg = new imgDownload();
        newImg.start();
    }
    class imgDownload extends Thread{
        String url = "https://static.wikia.nocookie.net/ironman/images/5/52/Infinity_War_Model_Prime.png/revision/latest/scale-to-width-down/404?cb=20190603020743";
        @Override
        public void run() {
            URL imgUrl = null;
            try {
                imgUrl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection)imgUrl.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpURLConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream in = null;
            try {
                in = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap convImg = BitmapFactory.decodeStream(in);
            img.post(new Runnable() {
                @Override
                public void run() {
                    img.setImageBitmap(convImg);
                }
            });
        }
    }
}