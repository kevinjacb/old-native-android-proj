package com.example.login2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        swap = (TextView)findViewById(R.id.swap);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        submit = (Button)findViewById(R.id.submit);
        submit.setTag("Login");
        swap.setTag("Sign up");

        ParseObject obh = new ParseObject("Dammit");
        obh.put("Jam", "Jam");
        obh.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                    Log.i("Done", "YES");
                else
                    Log.i("Done", "NO");
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
                submit.setTag("login");
            }
        }
        else{
            Log.i("Tag", view.getTag().toString());
            if(view.getTag().toString().equals("Login")){
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            Toast.makeText(MainActivity.this, "Logged in as " + username.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Oops! couldn't sign in", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(view.getTag().toString().equals("Sign up")){
                ParseUser user = new ParseUser();
                user.setUsername("Kevin");
                user.setPassword("passboi");
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if( e == null){
                            Toast.makeText(MainActivity.this, "Signed up!", Toast.LENGTH_SHORT).show();
                            Log.i("Sign up","successful");
                        }
                        else
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            Log.i("Sign up", "failed");
                    }
                });
            }
        }
    }
}