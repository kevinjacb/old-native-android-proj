/*package com.example.celebguess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivitydup extends AppCompatActivity {

    ImageView celebImg;
    Button opts[] = new Button[4];
    String urls[] = new String[40], alts[] = new String[40];
    String result = "";
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<Bitmap>();
    Boolean flag = true;
    int k;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celebImg = (ImageView)findViewById(R.id.celebImg);
        celebImg.setImageResource(R.drawable.loading);
        opts[0] = (Button)findViewById(R.id.button1);
        opts[1] = (Button)findViewById(R.id.button2);
        opts[2] = (Button)findViewById(R.id.button3);
        opts[3] = (Button)findViewById(R.id.button4);
        fetchData dataExt= new fetchData();
        dataExt.start();

    }

    public void downloadImages(){
        for (int i = 0; i < k; i++){
            URL url = null;
            try {
                url = new URL(urls[i]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream in = null;
            try {
                in = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bitmapArrayList.add(BitmapFactory.decodeStream(in));
            }
            catch (Exception e){
                Log.e("downloadimg", "Oops ! error!");
            }
        }
        test();
    }

    public void test(){

            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < k; i++){
                            Log.i("Executed?", String.valueOf(i));
                            celebImg.setImageBitmap(bitmapArrayList.get(i));
                            Toast.makeText(MainActivitydup.this, alts[i+1], Toast.LENGTH_SHORT);
                            try {
                                Thread.sleep(2000);
                            }
                            catch (Exception e){
                                Log.i("sleeep", "nope");
                            }
                        }
                    }
                });
            }
            catch (Exception e){
                Log.e("PrintError", "Oopsss , fuked up");
        }
    }

    public void urls(){
        Pattern p = Pattern.compile("data-src=\"/f/items(.*?)jpg\"");
        Matcher m = p.matcher(result);
        k = 0;
        while (m.find()) {
            urls[k] = m.group(1);
            urls[k] = "https://www.listchallenges.com/f/items" + urls[k] + "jpg";
            k++;
        }

        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(result);
        int j = 0;
        while (m.find() && j < k+2)
            alts[j++] = m.group(1);
        downloadImages();
    }


    public class fetchData extends Thread{
        String url = "https://www.listchallenges.com/100-famous-people";
        HttpURLConnection httpURLConnection = null;

        @Override
        public void run() {
            URL dataUrl = null;
            try {
                dataUrl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            {
                try {
                    httpURLConnection = (HttpURLConnection) dataUrl.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            InputStream in = null;

            {
                try {
                    in = httpURLConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            InputStreamReader reader = new InputStreamReader(in);
            int data = 0;

            {
                try {
                    data = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i("result", "waiting");
            while(data != -1) {
                char curr = (char)data;
                result += curr;
                try {
                    data = reader.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i("result", result);
            urls();
        }
    }
}
*/