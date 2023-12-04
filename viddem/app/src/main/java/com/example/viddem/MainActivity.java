package com.example.viddem;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // VideoView vid = (VideoView)findViewById(R.id.videoView);
     //   vid.setVideoPath("android.resource://"+ getPackageName()+ "/" + R.raw.samp);
   //     MediaController medi = new MediaController(this);
        MediaPlayer medi2 = MediaPlayer.create(this, R.raw)
       // medi.setAnchorView(vid);
     //   vid.setMediaController(medi);
        // vid.start();
    }
}