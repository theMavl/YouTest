package com.mavl.youtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    DB db;
    Button btTestMode;
    Button btEditorMode;
    Button btStatictic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btTestMode = (Button) findViewById(R.id.btTestMode);
        btEditorMode = (Button) findViewById(R.id.btEditorMode);
        btStatictic = (Button) findViewById(R.id.btStatistic);
        DataBaseCommunication dbComm = new DataBaseCommunication(this);
        db = dbComm.db;
        gimmeAdmin();
        dummyTest();

        btTestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent goToTest = new Intent(getApplicationContext(), theTest.class);
                int testID = 0;
                goToTest.putExtra("testID", testID);
                startActivity(goToTest);*/

                Intent goToTest = new Intent(getApplicationContext(), SelectTest.class);
                goToTest.putExtra("mode", SelectTest.MODE_TEST);
                startActivity(goToTest);
            }
        });

        btEditorMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTest = new Intent(getApplicationContext(), SelectTest.class);
                goToTest.putExtra("mode", SelectTest.MODE_EDIT);
                startActivity(goToTest);
            }
        });

        btStatictic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTest = new Intent(getApplicationContext(), StatisticActivity.class);
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

    void dummyTest() {

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Cursor c = tempDB.query("tests", null, null, null, null, null, null);
        if (!c.moveToFirst()) {
            ContentValues value = new ContentValues();
            value.put("shortName", "First Test");
            value.put("authorID", 0);
            value.put("description", "The first test ever just for debug purposes");
            value.put("questionsNum", 4);
            tempDB.insert("tests", null, value);

            value.put("shortName", "Second Test");
            value.put("authorID", 0);
            value.put("description", "The author feels like creating more tests for students");
            value.put("questionsNum", 0);
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

            value.put("testID", 1);
            value.put("number", 3);
            value.put("questionText", "At last you are smart clever enough");
            value.put("correctOptions", "1");
            value.put("option1", "Noooo");
            value.put("option2", "Screw you");
            tempDB.insert("questions", null, value);

            value.put("testID", 1);
            value.put("number", 4);
            value.put("cost", 4);
            value.put("questionText", "Shut up");
            value.put("correctOptions", "1");
            value.put("option1", "Damn it");
            value.put("option2", "Gggg");
            tempDB.insert("questions", null, value);
        }
        c.close();
    }
}
