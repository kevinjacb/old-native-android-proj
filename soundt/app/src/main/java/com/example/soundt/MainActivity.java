package com.example.soundt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer medi;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medi = MediaPlayer.create(this,R.raw.thers);
        medi.start();
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int max_vol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int cur_vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        int max_length = medi.getDuration();
        int cur_length = medi.getCurrentPosition();
        seekBar2.setMax(max_length);
        seekBar2.setProgress(medi.getCurrentPosition());
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    medi.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                medi.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                medi.start();
            }
        });
        seekBar.setMax(max_vol);
        seekBar.setProgress(cur_vol);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar2.setProgress(medi.getCurrentPosition());
            }
        },0,1000);
    }
    public void clickedplay(View view){
        medi.start();
    }
    public void clickedpause(View view){
        medi.pause();
    }
}