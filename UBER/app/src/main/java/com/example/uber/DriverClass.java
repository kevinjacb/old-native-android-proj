package com.example.uber;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Response;

import static java.lang.Integer.signum;

public class DriverClass extends AppCompatActivity {
    Location location,currentLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<String> addresses;
    ArrayList<Double> distances;
    ArrayList<LatLng> positions;
    ArrayList<LocationNode> locationNodes;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_list);

        //getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recycler_list);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //here
                //initLists();
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //here
            initLists();
            Log.i("DISTANCE", String.valueOf(locationNodes.get(0).returnDistance()));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.map_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ParseUser.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(DriverClass.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //here
                initLists();
            }
        }
    }

    ArrayList<ParseObject> objects = new ArrayList<ParseObject>();
    public void getObjects() {
        ParseQuery<ParseObject> query = new ParseQuery("Requests");
        query.whereGreaterThanOrEqualTo("latitude", currentLocation.getLatitude() - 1);
        query.whereLessThanOrEqualTo("latitude", currentLocation.getLatitude() + 1);
        query.whereGreaterThanOrEqualTo("longitude", currentLocation.getLongitude() - 1);
        query.whereLessThanOrEqualTo("longitude", currentLocation.getLongitude() + 1);
        query.setLimit(10);
        try {
            objects.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

        public void getData(){
        locationNodes = new ArrayList<>();
        LocationNode content;
        if (objects != null) {
            Log.i("first", String.valueOf(objects.size()));
            for (ParseObject object : objects) {
                    ParseGeoPoint geoPoint = object.getParseGeoPoint("location");
                    LatLng to = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                    LatLng from = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    float[] distance = new float[1];
                    Location.distanceBetween(currentLocation.getLatitude(),currentLocation.getLongitude(),geoPoint.getLatitude(), geoPoint.getLongitude(),distance);
                    content = new LocationNode(Double.valueOf(distance[0]), object.getString("Address"), to);
                    locationNodes.add(content);
                    //Log.i("first", "giGGGGGGGGGGa");
            }
        }
        else
            Log.i("first","SHITITT");
        Collections.sort(locationNodes, new Comparator<LocationNode>() {
            @Override
            public int compare(LocationNode o1, LocationNode o2) {
                return signum((int) (o1.returnDistance() - o2.returnDistance()));
            }
        });
    }
    class LocationNode{
        double distance;
        String address;
        LatLng position;
        LocationNode locationNode = null;
        public LocationNode(double distance, String address, LatLng position){
            this.distance = distance;
            this.address = address;
            this.position = position;
        }
        public LocationNode(){
            address = null;
            position = null;
        }
        public String returnAddress(){ return address; }
        public  double returnDistance(){
            return distance;
        }
        public LatLng returnPosition(){
            return position;
        }

    }
   /* public void sortNodes(LocationNode start){
        double lowest = start.returnDistance();
        LocationNode current = start;
        while(current != null){
            LocationNode iter = current;
            LocationNode nodeBefore = current;
            boolean flag = false;
            while(iter.locationNode != null){
                if(iter.locationNode.returnDistance() < lowest){
                    lowest = iter.locationNode.returnDistance();
                    nodeBefore = iter;
                    flag = true;
                }
                iter = iter.locationNode;
            }
            if(flag) {
                LocationNode temp = current.locationNode;
                current.locationNode = nodeBefore.locationNode;
                nodeBefore.locationNode.locationNode = temp;
            }
            current = current.locationNode;
        }
    }*/
    public void initLists(){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        getObjects();
        getData();
        DriverAdapter adapter = new DriverAdapter(locationNodes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DriverClass.this));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}