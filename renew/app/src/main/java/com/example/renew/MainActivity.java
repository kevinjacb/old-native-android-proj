package com.example.renew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

   Random randNum = new Random();
   int random = randNum.nextInt(30) + 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void genRand(){
        random = randNum.nextInt(30) + 1;
    }
    public void guessWork(View view) {
        EditText guessBox1 = (EditText)findViewById(R.id.guessBox);
        Log.i("input","works");
        if (Integer.parseInt(guessBox1.getText().toString()) == random){
            Toast.makeText(MainActivity.this,"Hooray ! You guessed right !",Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this,"Lets go again!",Toast.LENGTH_LONG).show();
            genRand();
        }
        else{
            Toast.makeText(MainActivity.this,"Nope ! Try Again",Toast.LENGTH_SHORT).show();
        }
    }
}