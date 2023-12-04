package com.example.parse;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ListView usersList;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> users;
    String currObjID;
    static Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signed_up_users);
        currObjID =  getIntent().getExtras().getString("OBJID");
        users =  new ArrayList<String>();
        usersList = (ListView)findViewById(R.id.usersList);
        setTitle("Current Users");
        parseQuery();
        if(users == null)
            users.add("No existing users");
        arrayAdapter = new ArrayAdapter<String>(UsersActivity.this, android.R.layout.simple_list_item_1, users);
        usersList.setAdapter(arrayAdapter);
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UsersActivity.this, UserFeed.class);
                intent.putExtra("username",users.get(position));
                startActivity(intent);
            }
        });
    }

    public void getPhotos(){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        runActivityForResult.launch(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!item.getTitle().equals("Logout"))
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else
            getPhotos();
        else{
            ParseUser.logOut();
            Intent intent = new Intent(UsersActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getPhotos();

    }

    public void parseQuery(){
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null) {
                    for (ParseUser userDat : objects)
                        if (!userDat.getObjectId().equals(currObjID))
                            users.add(userDat.getUsername());
                        arrayAdapter.notifyDataSetChanged();
                }
                else
                    Log.i("ERROR", e.getMessage());
            }

        });
    }

    static String caption;
    ActivityResultLauncher<Intent> runActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                            Intent captionsIntent = new Intent(UsersActivity.this,SetCaption.class);
                            runActivityForCaption.launch(captionsIntent);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> runActivityForCaption = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {// There are no request codes
                        try {
                            Log.i("WORKS", "1111111111111111111111111111111111111111111111111GGGGGGGgg");
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                            byte[] array = byteArrayOutputStream.toByteArray();
                            ParseFile file = new ParseFile("image.png", array);
                            ParseObject dataObj = new ParseObject("ImageHouse");
                            dataObj.put("Image", file);
                            dataObj.put("username", ParseUser.getCurrentUser().getUsername());
                            dataObj.put("caption", caption);
                            dataObj.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if( e == null)
                                        Toast.makeText(UsersActivity.this,"Posted Successfully", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(UsersActivity.this, "An Error Occured! Try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    //else
                        //Log.i("WORKS", "1111111111111111111111111111111111111111111111111GGGGGGGgg");
                }
            });
}
