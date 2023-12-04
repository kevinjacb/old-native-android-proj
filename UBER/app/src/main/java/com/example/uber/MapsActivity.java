package com.example.uber;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.uber.databinding.ActivityMapsBinding;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationManager locationManager;
    LocationListener locationListener;
    Location currLocation,newLoc = null;
    String address;
    Button req_cancel = null,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OPEEND","opened");
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener(){

            @Override
            public void onLocationChanged(@NonNull Location location) {
                currLocation = location;

            }
        };

        req_cancel = findViewById(R.id.req_cancel);
        //ParseObject prevUser = new ParseObject("Requested user");
        if(ParseUser.getCurrentUser() != null)
            checkPending();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    boolean flag = false,pending = true;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //getActionBar().show();
        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            centreOnMe();
        }
        // Add a marker in Sydney and move the camera
        LatLng currLoc = new LatLng(	(currLocation != null)?currLocation.getLatitude():	53.958332,(currLocation != null)?currLocation.getLongitude():-1.080278);
        mMap.addMarker(new MarkerOptions().position(currLoc).title("York, North Yorkshire, the UK"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull @NotNull LatLng latLng) {
                newLoc = new Location("com.example.uber");
                newLoc.setLatitude(latLng.latitude);
                newLoc.setLongitude(latLng.longitude);

                address =  getAddr(newLoc);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                flag = true;
            }
        });
    }
    public void centreOnMe(){
        try {
            String addr = getAddr(currLocation);
            LatLng currLoc = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(currLoc).title(addr));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 15));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.i("LOCATION",currLocation.toString());
                centreOnMe();
            }
        }
    }

    public String getAddr(Location location){
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return (addresses != null)?addresses.get(0).getAddressLine(0).toString():"Could't get the address";
    }
    public void confirm(View view){
        req_cancel = findViewById(R.id.req_cancel);
        logout = findViewById(R.id.logout);
        if (view.getTag().equals("confirm")) {
            ParseObject requests = new ParseObject("Requests");
            requests.put("username", ParseUser.getCurrentUser().getUsername());
            if (!flag) {
                requests.put("Address", getAddr(currLocation));
                ParseGeoPoint geoPoint = new ParseGeoPoint(currLocation.getLatitude(),currLocation.getLongitude());
                requests.put("location",geoPoint);
                requests.put("latitude",currLocation.getLatitude());
                requests.put("longitude",currLocation.getLongitude());
                requests.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MapsActivity.this, "Yoohoo! Your Uber is one the way to your current location!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else
                requests.put("Address", address);
                ParseGeoPoint geoPoint = new ParseGeoPoint(newLoc.getLatitude(),newLoc.getLongitude());
                requests.put("location",geoPoint);
                requests.put("latitude",newLoc.getLatitude());
                requests.put("longitude",newLoc.getLongitude());
                requests.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MapsActivity.this, "Yoohoo! Your Uber is one the way to your marked location!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            req_cancel.setText("Cancel UBER");
            req_cancel.setTag("cancel");
        }
        else if(view.getTag().toString().equals("cancel")){
            delRequests();
            req_cancel.setText("Call an UBER");
            req_cancel.setTag("confirm");
        }
        if(view.getTag().equals("logout")) {
            //delRequests();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            ParseUser.logOut();
            Intent intent = new Intent(MapsActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
    public void delRequests(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Requests");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject object : objects) {
                    try {
                        object.delete();
                        Toast.makeText(MapsActivity.this, "Request for the ride has been canceled!", Toast.LENGTH_LONG).show();
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                }
            }
        });
    }
    public void checkPending(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Requests");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    ParseGeoPoint Loc = (ParseGeoPoint) objects.get(0).get("location");
                    LatLng currLoc = new LatLng(Loc.getLatitude(),Loc.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currLoc).title(objects.get(0).getString("Address")));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
                    new AlertDialog.Builder(MapsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("There is a pending request!")
                            .setMessage("There is a pending request for an uber to "+objects.get(0).getString("Address")+" .\nWould you like to cancel it?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        for (ParseObject object : objects) {
                                            try {
                                                object.delete();
                                            } catch (ParseException parseException) {
                                                parseException.printStackTrace();
                                            }
                                        }
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    req_cancel = findViewById(R.id.req_cancel);
                                    req_cancel.setText("Cancel UBER");
                                    req_cancel.setTag("cancel");
                                }
                            })
                            .show();
                }
            }
        });
    }

}