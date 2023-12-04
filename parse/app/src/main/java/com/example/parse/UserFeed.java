package com.example.parse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserFeed extends AppCompatActivity {
    ArrayList<Bitmap> recImgs;
    ArrayList<String> texts;
    int k = 0;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        recImgs = new ArrayList<>();
        texts = new ArrayList<>();
        recyclerView = findViewById(R.id.r_view);
        String username = getIntent().getExtras().getString("username");
        setTitle(username +"'s Feed");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ImageHouse");
        query.whereEqualTo("username",username);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                k = 0;
                for(ParseObject object : objects){
                    Log.i("Size",String.valueOf(objects.size()));
                    texts.add(object.get("caption").toString());
                    ParseFile imgFile = (ParseFile)object.get("Image");
                    imgFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null)
                                recImgs.add(BitmapFactory.decodeByteArray(data,0,data.length));
                            RecycAdapter recycAdapter = new RecycAdapter(UserFeed.this,texts,recImgs);
                            recyclerView.setAdapter(recycAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(UserFeed.this));
                        }
                    });
                }
            }
        });
    }
}
