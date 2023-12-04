package com.example.favouriteplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String latitude, longitude, accuracy, altitude;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng recievedLoc = null;
    ArrayList<LatLng> favPlaces;
    boolean flag = true, flag2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            recievedLoc = (LatLng) getIntent().getExtras().getParcelable("travelTo");
            flag2 = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        favPlaces = new ArrayList<LatLng>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
                if(!flag2)
                    setLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                else
                    savedLocation();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
                favPlaces.add(latLng);
                Toast.makeText(MapsActivity.this, "This place has been saved!",Toast.LENGTH_SHORT).show();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        else
            if(!flag2)
                setLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            else
                savedLocation();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng markerLocation = marker.getPosition();
                String address = addressFromLocation(markerLocation.latitude,markerLocation.longitude);
                marker.setTitle(address);
                return false;
            }
        });
    }

    public void savedLocation(){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(recievedLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).title(addressFromLocation(recievedLoc.latitude,recievedLoc.longitude)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recievedLoc, 11));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("Location", favPlaces);
            intent.putExtra("State", true);
            setResult(1,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setLocation(Location location){
        if (location != null){
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            accuracy = String.valueOf(location.getAccuracy());
            altitude = String.valueOf(location.getAltitude());

            String unAddr = addressFromLocation(Double.valueOf(latitude), Double.valueOf(longitude));
            LatLng newLoc = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            try {
                mMap.clear();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            mMap.addMarker(new MarkerOptions().position(newLoc));
            if (flag)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 10));
            flag = false;

        }
    }
    public String addressFromLocation(double latitude, double longitude){
        String address="";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (addresses.get(0).getAddressLine(0) != null) {
                address = addresses.get(0).getAddressLine(0);
                //address = address.replace(",", ",\n");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            address = "Oops! Address unavailable";
        }
        return address;
    }
}