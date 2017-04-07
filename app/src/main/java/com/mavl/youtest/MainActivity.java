package com.mavl.youtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        gimmeAdmin();
        mindGap();
    }

    void gimmeAdmin() {

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Cursor c = tempDB.query("users", null, null, null, null, null, null);
        if (!c.moveToFirst()) {
            ContentValues value = new ContentValues();
            value.put("displayName", "Administrator");
            value.put("login", "admin");
            value.put("pass", "");
            value.put("admin", 1);
            tempDB.insert("users", null, value);
        }
        c.close();
    }

    void mindGap() {

        SQLiteDatabase tempDB = db.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("shortName", "The first");
        tempDB.insert("tests", null, value);
        //tempDB.execSQL(Test.sqlInsertString("tests", "The First", 0, "My first test", 0, false, 0, "pass"));
    }
}
