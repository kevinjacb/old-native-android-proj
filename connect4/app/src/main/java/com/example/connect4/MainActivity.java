package com.example.connect4;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        update();
        clear();
    }
    boolean clicked = false;
    ImageView images[][]= new ImageView[4][4];
    int ids[] = { R.id.pop1, R.id.pop2, R.id.pop3, R.id.pop4, R.id.pop5, R.id.pop6, R.id.pop7, R.id.pop8, R.id.pop9, R.id.pop10, R.id.pop11, R.id.pop12, R.id.pop13, R.id.pop14, R.id.pop15, R.id.pop16};

    public void clicked(View view) throws InterruptedException {
        ImageView image = (ImageView)findViewById(view.getId());
        if(image.getDrawable() == null) {
            Log.i("null","Is null");
            if (clicked) {
                view.setTag("BLUE");
                image.setImageResource(R.drawable.blue);
                image.setScaleX(1.1f);
                image.setScaleY(1.1f);
                image.setTranslationY(-2000f);
                image.animate().translationYBy(2000f).setDuration(1000);
                clicked = false;
            } else {
               view.setTag("RED");
                image.setImageResource(R.drawable.red);
                image.setTranslationY(-2000f);
                image.animate().translationYBy(2000f).setDuration(1000);
                clicked = true;
            }
            isFour(image);
            int ctr =0;
            for (int i =0; i < 4; i++)
                for (int j = 0;j < 4; j++)
                    if(images[i][j].getDrawable() != null) {
                        Log.i("CTR ",String.valueOf(ctr));
                        ctr++;
                    }
            if(ctr == 16)
                Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_LONG).show();
        }
        Log.i("Successs",String.valueOf(view.getTag()));
    }
    public void isFour(ImageView img){
        int pos[]={0 ,0};
        for (int i = 0; i < 4;i++)
            for (int j = 0; j < 4; j++) {

                if (getResources().getResourceEntryName(images[i][j].getId()).equalsIgnoreCase(getResources().getResourceEntryName(img.getId()) )) {
                    Log.i("Success","Yuuuuuup ");
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        int col = 0,row = 0;
        for (int i =0; i < 4;i++){
            if(images[pos[0]][i].getTag() == (img.getTag()))
                col++;
            if(images[i][pos[1]].getTag() == (img.getTag()))
                row++;
        }
        Log.i("rowcol",String.valueOf(row));
        Log.i("rowcol",String.valueOf(col));
        if(col == 4 || row == 4) {
            Log.i("flag","YUPP");
            String msg = String.valueOf(img.getTag()) + " is the WINNER !!";
            Toast.makeText(MainActivity.this, msg , Toast.LENGTH_LONG).show();
            
        }
    }
    public void reset(View view){
        clear();
    }
    public void update(){
        int k = 0;
        for (int i = 0; i < 4;i++)
            for (int j = 0; j < 4;j++)
                images[i][j] = (ImageView) findViewById(ids[k++]);
    }
    public void clear(){
        update();
        for (int i = 0; i < 4;i++)
            for (int j = 0; j < 4; j++)
                images[i][j].setImageDrawable(null);
    }
}