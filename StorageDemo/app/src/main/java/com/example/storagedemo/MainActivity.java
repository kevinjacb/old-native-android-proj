package com.example.storagedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<LatLng> names = new ArrayList<>();
        names.add("smack");
        names.add("that");
        names.add("on");
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.storagedemo", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("username",ObjectSerializer.serialize(names)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> get = new ArrayList<String>();
        try {
            get = (ArrayList<>) ObjectSerializer.deserialize(sharedPreferences.getString("username",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("username",get.get(0));
    }
}