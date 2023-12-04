package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {


    TextView swap;
    EditText username, password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ParseUser.getCurrentUser().getUsername() != null)
            loggedIn();
        ParseUser.logOut();
        swap = (TextView)findViewById(R.id.swap);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.submit);
        submit.setTag("Login");
        swap.setTag("Sign up");
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                    logUp(submit);
                return false;
            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void logUp(View view){
        if(view.getId() == swap.getId()){
            if(view.getTag() == "Sign up"){
                submit.setText("Sign UP");
                swap.setText("Log in?");
                swap.setTag("Login");
                submit.setTag("Sign up");
            }
            else{
                submit.setText("login");
                swap.setText("Sign up?");
                swap.setTag("Sign up");
                submit.setTag("Login");
            }
        }
        else{
            Log.i("Tag", view.getTag().toString());
            if(ParseUser.getCurrentUser().getUsername() ==  null) {
                if (view.getTag().toString().equals("Login")) {
                    ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                loggedIn();
                            } else
                                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (view.getTag().toString().equals("Sign up")) {
                    ParseUser user = new ParseUser();
                    user.setUsername(username.getText().toString());
                    user.setPassword(username.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "Signed up!", Toast.LENGTH_SHORT).show();
                                Log.i("Sign up", "successful");
                                loggedIn();
                            } else
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("Sign up", "failed");
                        }
                    });
                }
            }
            else
                Toast.makeText(this, "Already signed in as "+ParseUser.getCurrentUser().getUsername().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        public void hideKeyBD(View view){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        public void loggedIn(){
            Toast.makeText(MainActivity.this, "Logged in as " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
            String objID = ParseUser.getCurrentUser().getObjectId();
            Intent intent = new Intent(MainActivity.this, UsersActivity.class);
            intent.putExtra("OBJID", objID);
            startActivity(intent);
        }
    }
