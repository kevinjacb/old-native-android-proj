package com.example.dialogandalert;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Boolean spanish = false;
    SharedPreferences sharedPreferences;
    TextView language;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("com.example.dialogandalert", Context.MODE_PRIVATE);
        language = (TextView)findViewById(R.id.lang);
        try {
            spanish = sharedPreferences.getBoolean("lang",false);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        new AlertDialog.Builder(this)
                .setTitle("Select the language of your choice")
                .setPositiveButton("Spanish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spanish = true;
                    }
                })
                .setNegativeButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spanish = false;
                    }
                })
                .show();
        sharedPreferences.edit().clear().commit();
        sharedPreferences.edit().putBoolean("lang",spanish);
        language.setText((spanish)?"Spanish":"English");
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.spanish:
                spanish =true;
                break;
            case R.id.English:
                spanish = false;
                break;
        }
        sharedPreferences.edit().clear().commit();
        sharedPreferences.edit().putBoolean("lang",spanish);
        language.setText((spanish)?"Spanish":"English");
        return super.onOptionsItemSelected(item);
    }
}