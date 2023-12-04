package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textViews[] = new TextView[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViews[0] = (TextView)findViewById(R.id.StartButton);
        textViews[1] = (TextView)findViewById(R.id.timer);
        textViews[2] = (TextView)findViewById(R.id.score);
        textViews[3] = (TextView)findViewById(R.id.equation);
        textViews[4] = (TextView)findViewById(R.id.square1);
        textViews[5] = (TextView)findViewById(R.id.square2);
        textViews[6] = (TextView)findViewById(R.id.square3);
        textViews[7] = (TextView)findViewById(R.id.square4);
        textViews[8] = (TextView)findViewById(R.id.Result);
        for (int i =1; i <= 8; i++) {
            textViews[i].setVisibility(View.INVISIBLE);
            if(i != 1 && i != 2 && i != 3)
                textViews[i].setTextSize(50);
            else
                textViews[i].setTextSize(20);
            textViews[3].setTextSize(30);
        }
    }
    int chooseRandSquare;
    public void randGenerator(){
        int randoms[] = new int[2];
        randoms[0] = new Random().nextInt(100) + 1;
        randoms[1] = new Random().nextInt(100) + 1;
        String text =String.valueOf(randoms[0])+" + "+String.valueOf(randoms[1]);
        textViews[3].setText(text);
        chooseRandSquare = new Random().nextInt(4) + 4;
        textViews[chooseRandSquare].setText(String.valueOf(randoms[0]+randoms[1]));
        for (int i = 4; i <= 7; i++){
            if(i != chooseRandSquare)
                textViews[i].setText(String.valueOf(new Random().nextInt(200) + 1));
        }
    }
    int total = 0, correct = 0;
    public void squareClicked(View view){
        if(view.getTag().toString().equalsIgnoreCase("start")) {
            textViews[0].setVisibility(View.INVISIBLE);
            for(int i = 1; i <= 8; i++)
                textViews[i].setVisibility(View.VISIBLE);
            timeOut();
            randGenerator();
        }
        else if( Integer.parseInt(view.getTag().toString()) == chooseRandSquare) {
            textViews[8].setText("Correct !");
            updateScore(true);
        }
        else {
            textViews[8].setText("Incorrect");
            updateScore(false);
        }
        randGenerator();
    }
    public void updateScore(boolean check){
        total++;
        if(check)
            textViews[2].setText(++correct+"/"+total);
        else
            textViews[2].setText(correct+"/"+total);
    }
    public void timeOut(){
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                textViews[1].setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                for(int i = 1; i <= 8; i++)
                    textViews[i].setVisibility(View.INVISIBLE);
                textViews[0].setTextSize(30);
                textViews[0].setText("Your Score :\n"+correct+"/"+total);
                textViews[0].setVisibility(View.VISIBLE);
                textViews[2].setText("0/0");
                correct = 0;
                total = 0;
            }
        }.start();
    }
}