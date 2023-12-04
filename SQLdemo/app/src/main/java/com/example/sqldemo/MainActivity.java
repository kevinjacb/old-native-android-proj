  package com.example.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{

            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("users",MODE_PRIVATE,null);

//            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3)) ");
//            sqLiteDatabase.execSQL("INSERT INTO users (name , age) VALUES ('Senoj KC', 54)");
//            sqLiteDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Jyoti Mary Jose', 49)");
//            sqLiteDatabase.execSQL("INSERT INTO users(name , age) VALUES ('Kevin', 18)");
//            sqLiteDatabase.execSQL("INSERT INTO users( name, age) VALUES ('Aiswarya', 23)");
//            sqLiteDatabase.execSQL("DELETE FROM users WHERE name = 'Kevin'");
            sqLiteDatabase.execSQL("UPDATE users SET age = 20 WHERE name = 'Aiswarya'");
            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users", null);
            c.moveToFirst();
            int eventIndex = c.getColumnIndex("name");
            int dateIndex = c.getColumnIndex("age");
            while(c != null){
                Log.i("event", c.getString(eventIndex));
                Log.i("data",String.valueOf(c.getInt(dateIndex)));

                c.moveToNext();
            }
        }
        catch (Exception e) {
        }
    }
}