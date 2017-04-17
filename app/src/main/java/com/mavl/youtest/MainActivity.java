package com.mavl.youtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    DB db;
    Button btTestMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseCommunication dbComm = new DataBaseCommunication(this);
        btTestMode = (Button) findViewById(R.id.btTestMode);
        db = dbComm.db;
        gimmeAdmin();
        dummyTest();
        mindGap();

        btTestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent goToTest = new Intent(getApplicationContext(), theTest.class);
                int testID = 0;
                goToTest.putExtra("testID", testID);
                startActivity(goToTest);*/

                Intent goToTest = new Intent(getApplicationContext(), SelectTest.class);
                startActivity(goToTest);
            }
        });
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

    public void debugResult(View view) {
        Intent intent = new Intent(this, TestResults.class);
        intent.putExtra("resultID", 24);
        startActivity(intent);
    }

    void dummyTest() {

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Cursor c = tempDB.query("tests", null, null, null, null, null, null);
        if (!c.moveToFirst()) {
            ContentValues value = new ContentValues();
            value.put("shortName", "First Test");
            value.put("authorID", 0);
            value.put("description", "The first test ever just for debug purposes");
            tempDB.insert("tests", null, value);

            value.put("shortName", "Second Test");
            value.put("authorID", 0);
            value.put("description", "The author feels like creating more tests for students");
            tempDB.insert("tests", null, value);

            value.put("shortName", "Third Test");
            value.put("authorID", 0);
            value.put("description", "He really enjoys doing this.");
            tempDB.insert("tests", null, value);

            value.put("shortName", "Fourth Test");
            value.put("authorID", 0);
            value.put("description", "I think someone should stop him");
            tempDB.insert("tests", null, value);

            value.put("shortName", "Fifth Test");
            value.put("authorID", 0);
            value.put("description", "No, really. Enough.");
            tempDB.insert("tests", null, value);

            value.clear();
            value.put("testID", 1);
            value.put("number", 1);
            value.put("questionText", "Troubles?");
            value.put("correctOptions", "1");
            value.put("option1", "Nope");
            value.put("option2", "So what?");
            tempDB.insert("questions", null, value);

            value.clear();
            value.put("testID", 1);
            value.put("number", 2);
            value.put("questionText", "You jerk");
            value.put("correctOptions", "1");
            value.put("option1", "Stop doing this");
            value.put("option2", "Please");
            tempDB.insert("questions", null, value);
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
