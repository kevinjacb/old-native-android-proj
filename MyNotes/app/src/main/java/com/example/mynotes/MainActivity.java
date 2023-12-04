package com.example.mynotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<String> notes;
    public static ArrayList<String> peek;
    ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("com.example.mynotes", Context.MODE_PRIVATE);
        notes = new ArrayList<String>();
        peek = new ArrayList<String>();
        loadNsave(true);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,peek);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        arrayAdapter.notifyDataSetChanged();
                        loadNsave(false);
                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TextActivity.class);
                intent.putExtra("position", position);
                activityResultLauncher.launch(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to delete this?")
                        .setIcon(android.R.drawable.ic_input_delete)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                peek.remove(position);
                                loadNsave(false);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO",null)
                        .show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, TextActivity.class);
        intent.putExtra("position", notes.size());
        activityResultLauncher.launch(intent);
        return super.onOptionsItemSelected(item);
    }

    public void loadNsave(Boolean load){
        if(load){
            try{
                notes = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>())));
                peek = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("peek",ObjectSerializer.serialize(new ArrayList<String>())));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Log.i("TRUEEEE?",String.valueOf(notes.isEmpty()));
            if(notes.isEmpty()) {
                notes.add("Example note");
                peek.add("Example note");
            }
        }
        else
            try {
                sharedPreferences.edit().clear().commit();
                sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
                sharedPreferences.edit().putString("peek", ObjectSerializer.serialize(peek)).apply();
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }
}