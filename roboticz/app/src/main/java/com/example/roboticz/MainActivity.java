package com.example.roboticz;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SeekBar seekbar;
    TextView text;
    Button art_op;
    CountDownTimer CountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        art_op = (Button)findViewById(R.id.artop);
        text = (TextView) findViewById(R.id.timer_disp);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setMax(240);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text.setText("Count Down :\n"+String.valueOf(progress/4)+":"+String.valueOf(progress%4*15));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void timerint(View view){
        int progress = seekbar.getProgress();
        int millis = (progress/4*60*1000) + (progress%4*15*1000);
        if(art_op.getText().equals("Start")) {
            seekbar.setVisibility(View.GONE);
            Timer(millis);
            art_op.setText("Stop");
        }
        else {
            text.setText("Count Down :\n" + String.valueOf(progress / 4) + ":" + String.valueOf(progress % 4 * 15));
            seekbar.setVisibility(View.VISIBLE);
            art_op.setText("Start");
            CountDown.cancel();
        }
    }
    public void Timer(int time){
         CountDown = new CountDownTimer(time,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                text.setText("Time Left: \n" + String.valueOf(millisUntilFinished/1000/60)+ ":" + String.valueOf(millisUntilFinished/1000%60));
            }

            @Override
            public void onFinish() {
                text.setText("CountDown finished!");
                MediaPlayer medi = MediaPlayer.create(MainActivity.this, R.raw.alert);
                medi.start();
                timerint(findViewById(R.id.artop));
            }
        }.start();
    }

}