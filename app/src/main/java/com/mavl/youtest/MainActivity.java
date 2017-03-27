package com.mavl.youtest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DB(this);
        mindGap();
    }

    void gimmeAdmin() {
        SQLiteDatabase tempDB = db.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("shortName", "The first");
        tempDB.insert("users", null, value);
    }

    void mindGap() {

        SQLiteDatabase tempDB = db.getWritableDatabase();
        /*ContentValues value = new ContentValues();
        value.put("shortName", "The first");
        tempDB.insert("tests", null, value);*/
        tempDB.execSQL(Test.sqlInsertString("tests", "The First", 0, "My first test", 0, false, 0, ""));
    }
}
