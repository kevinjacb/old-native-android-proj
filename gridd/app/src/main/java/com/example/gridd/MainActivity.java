package com.example.gridd;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MediaPlayer medi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick(View view){
        int id = 0;
        String tag = (String) view.getTag();
        switch (tag){
            case "1":
                id = R.raw.heloo;
                Toast.makeText(this, "Works tho",Toast.LENGTH_SHORT)
                break;
            case "2":
                id = R.raw.goodevening;
                break;
            case "3":
                id = R.raw.doyouspeakeng;
                break;
            case "4":
                id = R.raw.howareyou;
                break;
            case "5":
                id = R.raw.ilivein;
                break;
            case "6":
                id = R.raw.mynameis;
                break;
            case "7":
                id = R.raw.please;
                break;
            case "8":
                id = R.raw.welcome;
                break;
        }
        if (id != 0) {
            medi = MediaPlayer.create(this, id);
            medi.start();
        }
    }
}