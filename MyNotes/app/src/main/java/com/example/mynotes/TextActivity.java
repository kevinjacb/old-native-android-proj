package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TextActivity extends AppCompatActivity {

    EditText editText;
    int pos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_layout);
        editText = (EditText)findViewById(R.id.multiLine);
        pos = getIntent().getExtras().getInt("position");
        try {
            editText.setText(MainActivity.notes.get(pos));
        }
        catch (Exception e){
            editText.setHint("Enter you note here");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //Log.i("pos",MainActivity.peek.get(0));
            try {
                MainActivity.notes.set(pos,editText.getText().toString());
                MainActivity.peek.set(pos,MainActivity.notes.get(pos).substring(0,(MainActivity.notes.get(pos).length() < 40)?MainActivity.notes.get(pos).length():40) + String.valueOf((MainActivity.notes.get(pos).length() < 40)?"":"..."));
            }
            catch (Exception e){
                MainActivity.notes.add(editText.getText().toString());
                MainActivity.peek.add(MainActivity.notes.get(pos).substring(0,(MainActivity.notes.get(pos).length() < 40)?MainActivity.notes.get(pos).length():40) +  String.valueOf((MainActivity.notes.get(pos).length() < 40)?"":"..."));
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
