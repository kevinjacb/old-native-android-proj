package com.example.parse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SetCaption extends AppCompatActivity {

    EditText caption;
    Button post;
    ImageView img;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captions);
        caption = findViewById(R.id.caption);
        post = findViewById(R.id.post);
        img = findViewById(R.id.imageView);
        img.setImageBitmap(UsersActivity.bitmap);
    }

    public void postExit(View view){
        UsersActivity.caption = caption.getText().toString();
        setResult(-1);
        finish();
    }
}
