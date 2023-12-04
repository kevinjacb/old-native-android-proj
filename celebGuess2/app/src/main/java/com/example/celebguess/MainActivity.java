package com.example.celebguess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView celebImg;
    Button opts[] = new Button[4];
    ArrayList<String> urls = new ArrayList<String>();
    ArrayList<String> alts = new ArrayList<String>();
    String result = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celebImg = (ImageView)findViewById(R.id.celebImg);
        opts[0] = (Button)findViewById(R.id.button1);
        opts[1] = (Button)findViewById(R.id.button2);
        opts[2] = (Button)findViewById(R.id.button3);
        opts[3] = (Button)findViewById(R.id.button4);
        loading();
        fetchData fData = new fetchData();
        fData.start();

    }
    public void loading(){
        for (int i = 0; i < 4; i++)
            opts[i].setText("LOADING...");
        celebImg.setImageResource(R.drawable.loading);
    }

    public class imgDownload extends Thread{
        int choice;
        @Override
        public void run() {
            URL url = null;
            try {
                url = new URL(urls.get(choice));
                Log.i("URL", urls.get(choice));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
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
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    celebImg.setImageBitmap(bitmap);
                }
            });
        }
    }
    public void randomizer(){
        int randomChoice, altChoice;
        do{
            randomChoice = new Random().nextInt(urls.size() + 1);
            altChoice = randomChoice + 1;
        }
        while(altChoice >= (alts.size() - 7));
        int optRandChoose = new Random().nextInt(4);
        opts[optRandChoose].setText(alts.get(altChoice));
        opts[optRandChoose].setTag("C");
        for( int i = 0;i < 4; i++){
            if(i != optRandChoose) {
                int random;
                do {
                    random = new Random().nextInt(alts.size());
                    opts[i].setText(alts.get(random));
                    opts[i].setTag("F");
                }
                while (random == 0 || random >= (alts.size() - 7));
            }
        }
        imgDownload img = new imgDownload();
        img.choice = randomChoice;
        img.start();
    }

    public void urls(){
        Pattern p = Pattern.compile("src=\"/f/items(.*?)jpg\"");
        Matcher m = p.matcher(result);
        while (m.find()) {
            urls.add("https://www.listchallenges.com/f/items" + m.group(1) + "jpg");
        }

        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(result);
        int j = 0;
        while (m.find())
            alts.add(m.group(1));
        randomizer();
    }
    public void selectedOpt(View view){
        boolean status = !"loading".equalsIgnoreCase(opts[0].getText().toString());
        if ("C".equals(view.getTag()) && status)
            check(true);
        else if(status)
            check(false);
    }
    public void check(boolean input){
        if(input){
            Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
            loading();
            randomizer();
        }
        else {
            Toast.makeText(this, "INCORRECT!", Toast.LENGTH_SHORT).show();
            loading();
            randomizer();
        }
    }
    public class fetchData extends Thread {
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
                while (data != -1) {
                    char curr = (char) data;
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
