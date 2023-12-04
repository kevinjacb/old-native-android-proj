package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String result = "";
    EditText editText;
    Button button;
    TextView textCity, textWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.enquiry);
        editText = (EditText)findViewById(R.id.city);
        textCity = (TextView) findViewById(R.id.selectedCity);
        textWeather = (TextView)findViewById(R.id.Info);
        downloadCWeatherJSON findWeather = new downloadCWeatherJSON("London");
        findWeather.start();
    }

    public void weather(View view){
        textCity.setText("");
        textWeather.setText("");
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textCity.getWindowToken(), 0);
        String city = null;
        try {
            city = URLEncoder.encode(editText.getText().toString().trim(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(!city.equalsIgnoreCase("")) {
            downloadCWeatherJSON findWeather = new downloadCWeatherJSON(city);
            findWeather.start();
        }
        else
            textWeather.setText("The field cannot be left blank");
    }

    public class downloadCWeatherJSON extends Thread {
        URL dataURL = null; String city;
        public downloadCWeatherJSON(String city){
            String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=06cda9bafd982ae2770f61ef70a4f0fa";
            Log.i("url", url);
            try {
                dataURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            this.city = city;
        }
        @Override
        public void run() {
            result = "";
            ArrayList<String> mainWeather = new ArrayList<String>();
            ArrayList<String> description = new ArrayList<String>();
            try {
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) dataURL.openConnection();
                } catch (IOException e) {
                    textCity.setText("");
                    textWeather.setText("Oops! Are you sure you spelled that right?");
                    e.printStackTrace();
                }
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = 0;
                    data = reader.read();
                while (data != -1) {
                    result += (char) data;
                        data = reader.read();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                textCity.setText("");
                textWeather.setText("Could not find the weather");
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonInd = jsonArray.getJSONObject(i);
                    mainWeather.add(jsonInd.getString("main"));
                    description.add(jsonInd.getString("description"));
                }
            }
            catch (Exception e){
                e.printStackTrace();
                textCity.setText("");
                textWeather.setText("Could not find the weather");
            }
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("Check", city);
                        textCity.setText(city);
                        Log.i("Check", String.valueOf(mainWeather.size()));
                        Log.i("Check", description.get(0));
                        textWeather.setText("Weather: \t" + mainWeather.get(0) + "\n" + "Specifics: \t" + description.get(0));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}