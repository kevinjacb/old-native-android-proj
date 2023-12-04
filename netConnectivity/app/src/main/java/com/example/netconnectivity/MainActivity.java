package com.example.netconnectivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newThread netConnect = new newThread();
        netConnect.start();
    }

    class newThread extends Thread{
        @Override
        public void run() {
            URL url = null;
            try {
                url = new URL("https://www.google.com/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream in = null;
            try {
                in = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader reader = new InputStreamReader(in);
            int data = 0;
            try {
                data = reader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(data != -1) {
                result += (char) data;
                try {
                    data = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Here",result);
                }
            });

        }
    }
}