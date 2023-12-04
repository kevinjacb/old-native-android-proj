package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView) findViewById(R.id.myListView);
        ArrayList<String> myAim = new ArrayList<String>();
        myAim.add("Billionaire");
        myAim.add("Happy af");
        myAim.add("Proud AF Parents and family");
        myAim.add("Save Earth!");
        myAim.add("Plant Billions of trees");
        myAim.add("Eliminate injustice");
        myAim.add("Put an end to misery and educate people");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myAim);
        lv.setAdapter(arrayAdapter);
    }
}