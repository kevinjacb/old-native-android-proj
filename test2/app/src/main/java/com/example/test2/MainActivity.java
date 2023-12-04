package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fadein(){};
    float movep = 2000.0f;
    public void fadeinto(View view){
        ImageView image = (ImageView) findViewById(R.id.Fe);
        ImageView image2 = (ImageView) findViewById(R.id.Fe2);
        image.animate().translationX(movep).setDuration(2000);
        image2.animate().translationX(movep-2000).setDuration(2000);
        movep = Math.abs(movep-2000);
    }
}