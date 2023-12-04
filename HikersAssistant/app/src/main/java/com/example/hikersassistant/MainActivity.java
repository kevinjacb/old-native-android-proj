package com.example.hikersassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    LocationManager locationManager;
    LocationListener locationListener;
    String latitude, longitude, accuracy, altitude, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                getAddress(location);
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            getAddress(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                getAddress(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
        }
    }
    public void getAddress(Location location){
        if (location != null){
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            accuracy = String.valueOf(location.getAccuracy());
            altitude = String.valueOf(location.getAltitude());

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(latitude),Double.parseDouble(longitude),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addresses.get(0).getAddressLine(0) != null) {
                address = addresses.get(0).getAddressLine(0);
                address = address.replace(",", ",\n");
            }
            else
                address = "Oops! Address unavailable";
            textView.setGravity(0);
            textView.setTextSize(20);
            textView.setText("Latitude: \t"+latitude+" \nLongitude: \t"+ longitude +"\n \nAccuracy: \t"+ accuracy+" \n\nAltitude: \t"+ altitude+ "\n \n Address: \t"+ address);

        }
    }
}