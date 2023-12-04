package com.example.uber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    Button submit;
    Switch aSwitch;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences("com.example.uber", Context.MODE_PRIVATE);
        aSwitch = findViewById(R.id.optionSwitch);
        submit = findViewById(R.id.button2);
        getSupportActionBar().hide();
        if(ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().getString("selection").equals("rider"))
            mapActivity();
        else if(ParseUser.getCurrentUser()!= null && ParseUser.getCurrentUser().getString("selection").equals("driver"))
            driveractivity();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
    public void choice(View view) {
        boolean checked = aSwitch.isChecked();
        if (!checked) {
            Log.i("USER:", " Entering as a rider");
            anonymousSignIn("rider");
        }
        else {
            anonymousSignIn("driver");
            Log.i("USER:", "Entering as a Driver");
        }
    }
    public void mapActivity(){
        Log.i("passed","now");
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    public void driveractivity(){
        Intent intent = new Intent(MainActivity.this, DriverClass.class);
        startActivity(intent);
    }
    public void anonymousSignIn(String select){
        if (ParseUser.getCurrentUser() == null) {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        user.put("selection",select);
                        user.saveInBackground();
                        if(select.equals("rider"))
                            mapActivity();
                        else
                            driveractivity();
                        //Log.i("passed","passed tf");
                    }
                    else
                        Log.i("LOGIN", e.getMessage());
                }
            });
        } else {
            Log.i("LOGIN", "Logged in already as " + ParseUser.getCurrentUser().getUsername());
            mapActivity();
        }
    }
}