package com.example.favouriteplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavPlacesActivity extends AppCompatActivity {
    ArrayList<String> arrayList, latitudes, longitudes;
    ArrayList<LatLng> latLngs;
    ListView listView;
    SharedPreferences sharedPreferences;

    boolean flag = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_favourite_places);
        latitudes = new ArrayList<String>();
        longitudes = new ArrayList<String>();
        sharedPreferences = this.getSharedPreferences("com.example.favouriteplaces", Context.MODE_PRIVATE);
        latLngs = new ArrayList<LatLng>();
        arrayList = new ArrayList<String>();
        listView = (ListView)findViewById(R.id.favPlaces);
        try {
            arrayList.addAll((ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("Array List",ObjectSerializer.serialize(new ArrayList<String>()))));
            longlatInit(false, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(arrayList.isEmpty())
            arrayList.add("Add a new place");
        setListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavPlacesActivity.this, MapsActivity.class);
                flag = true;
                switch(position){
                    case 0:
                        startActivityForResult(intent,1);
                        break;
                    default:
                        Log.i("arraysize", String.valueOf(latLngs.size()));
                        intent.putExtra("travelTo",latLngs.get(position - 1));
                        startActivityForResult(intent,1);


                }
            }
        });
    }
    public void setListView(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    public void longlatInit(Boolean store,ArrayList<LatLng> passed){

        if (store) {
            for (LatLng coordinates : passed) {
                latitudes.add(String.valueOf(coordinates.latitude));
                longitudes.add(String.valueOf(coordinates.longitude));
            }
            try {
                sharedPreferences.edit().putString("latitudes",ObjectSerializer.serialize(latitudes)).apply();
                sharedPreferences.edit().putString("longitudes",ObjectSerializer.serialize(longitudes)).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            try {
                latitudes = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("latitudes",ObjectSerializer.serialize(new ArrayList<String>())));
                longitudes = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("longitudes",ObjectSerializer.serialize(new ArrayList<String>())));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < latitudes.size(); i++)
                latLngs.add(new LatLng(Double.valueOf(latitudes.get(i)), Double.valueOf(longitudes.get(i))));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            try {
                ArrayList<LatLng> tempLatLng = new ArrayList<LatLng>();
                tempLatLng = (ArrayList<LatLng>)data.getSerializableExtra("Location");
                longlatInit(true, tempLatLng);
                latLngs.addAll(tempLatLng);
                addressFromLocation(tempLatLng);
                setListView();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void addressFromLocation(ArrayList<LatLng> newLatLngs){
        String address="";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        ArrayList<Address> addresses = new ArrayList<Address>();
        for(int i = 0; i < newLatLngs.size(); i++) {
            try {
                addresses.addAll(geocoder.getFromLocation(newLatLngs.get(i).latitude, newLatLngs.get(i).longitude, 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                    address = addresses.get(i).getAddressLine(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
                address = "Oops! Address unavailable";
            }
            arrayList.add(address);
            Log.i("arraylist",String.valueOf(arrayList.size()));
            Log.i("arraylist",String.valueOf(latLngs.size()));
            try {
                sharedPreferences.edit().clear().commit();
                sharedPreferences.edit().putString("Array List",ObjectSerializer.serialize(arrayList)).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
