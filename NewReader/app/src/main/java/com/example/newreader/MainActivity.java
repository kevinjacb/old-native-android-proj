package com.example.newreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> newsArticles;
    String prefix = "https://hacker-news.firebaseio.com/v0/";
    String  result = "";
    ArrayList<String> htmlUrls, titles;
    ArrayAdapter arrayAdapter;
    ListView listView;
    SQLiteDatabase articlesDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsArticles = new ArrayList<String>();
        htmlUrls = new ArrayList<String>();
        titles = new ArrayList<String>();
        articlesDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, title VARCHAR, content VARCHAR)");
        titles.add("Loading");
        listView = (ListView)findViewById(R.id.news_selection);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(arrayAdapter);
        loadFromDB();
        getUrls get = new getUrls();
        //get.start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("html", htmlUrls.get(position));
                startActivity(intent);
            }
        });
    }

    public class getUrls extends Thread{
        URL newsFeed;
        HttpURLConnection httpURLConnection;
        InputStream in;
        InputStreamReader reader;
        @Override
        public void run() {
            try {
                newsFeed = new URL(prefix + "topstories.json");
                httpURLConnection = (HttpURLConnection) newsFeed.openConnection();
                httpURLConnection.connect();
                in = httpURLConnection.getInputStream();
                reader = new InputStreamReader(in);
                int data = 0;
                data = reader.read();
                while (data != -1) {
                    result += (char) data;
                    data = reader.read();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Log.i("result", result);
            try {
                JSONArray newsArr = new JSONArray(result);
                titles.clear();
                for (int i = 0; i < 4; i++) {
                    Log.i("inArray", prefix + "item/" + newsArr.get(i).toString() +".json");
                    result = getNewsArticles(prefix + "item/" + newsArr.get(i).toString() +".json");
                    JSONObject newsJson = new JSONObject(result);
                    result = getNewsArticles(newsJson.getString("url"));
                    String sql = "INSERT INTO articles (id, title, content) VALUES ( ?, ?, ?)";
                    SQLiteStatement action = articlesDB.compileStatement(sql);
                    action.bindString(1,newsJson.getString("id"));
                    action.bindString(2,newsJson.getString("title"));
                    action.bindString(3,result);
                    action.execute();
                }
                loadFromDB();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        public String getNewsArticles(String url) {
            result = "";
            try {
                newsFeed = new URL(url);
                httpURLConnection = (HttpURLConnection) newsFeed.openConnection();
                httpURLConnection.connect();
                in = httpURLConnection.getInputStream();
                reader = new InputStreamReader(in);
                int data = 0;
                data = reader.read();
                while (data != -1) {
                    result += (char) data;
                    data = reader.read();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }
    public void loadFromDB(){
        Cursor c = articlesDB.rawQuery("SELECT * FROM articles", null);
        int titleInd = c.getColumnIndex("title");
        int contentInd = c.getColumnIndex("content");
        if(c.moveToFirst()){
            titles.clear();
            htmlUrls.clear();
            do{
                titles.add(c.getString(titleInd));
                htmlUrls.add(c.getString(contentInd));
            }while (c.moveToNext());
        }
        arrayAdapter.notifyDataSetChanged();
    }

}