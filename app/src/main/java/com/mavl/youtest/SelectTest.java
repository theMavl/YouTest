package com.mavl.youtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mavl.youtest.listAdapters.QuickAdapter;

import java.util.ArrayList;

public class SelectTest extends AppCompatActivity {
    public final static int MODE_TEST = 0;
    public final static int MODE_EDIT = 1;

    int mode;
    DB db;
    Cursor cursor;
    ArrayList<Integer> testsList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        cursor.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);

        setTitle(getResources().getString(R.string.select_test));
        db = DataBaseCommunication.db;

        // TodoDatabaseHandler is a SQLiteOpenHelper class connecting to SQLite

        // Get access to the underlying writeable database
        SQLiteDatabase tempDB = db.getWritableDatabase();
        // Query for items from the database and get a cursor back
        cursor = tempDB.rawQuery("SELECT  * FROM tests", null);
        // Find ListView to populate
        ListView lvTests = (ListView) findViewById(R.id.tests);
        // Setup cursor adapter using cursor from last step
        QuickAdapter adapter = new QuickAdapter(this, cursor);
        // Attach cursor adapter to the ListView
        lvTests.setAdapter(adapter);

        if (mode == MODE_EDIT) {
            View addNewButton = getLayoutInflater().inflate(R.layout.add_new_layout, null);
            lvTests.addFooterView(addNewButton);
        }

        cursor.moveToFirst();
        do {
            testsList.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        while (cursor.moveToNext());

        //TODO: Check if this shit will do something when too many elements
        //cursor.close();

        lvTests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                int selectedTestID;
                if (pos < testsList.size())
                    selectedTestID = testsList.get(pos);
                else
                    selectedTestID = -2; // new test

                Intent goToTest;

                if (mode == MODE_EDIT)
                    goToTest = new Intent(getApplicationContext(), EditTestActivity.class);
                else
                    goToTest = new Intent(getApplicationContext(), theTest.class);

                goToTest.putExtra("testID", selectedTestID);
                Log.d("on-item-click", "Test id = " + selectedTestID+ " "+testsList.toString());
                startActivity(goToTest);
                finish();
            }
        });
    }



    /*boolean getTests(int testID) {
        SQLiteDatabase tempDB = db.getWritableDatabase();
        if (testID == -1)
            return false;

        testsList.clear();
        Cursor cursor = tempDB.query("tests", null, null, null, null, null, null);
        //cursor.moveToFirst();

        int ID;
        String shortName;
        String description;
        int authorID;
        int time;
        boolean random;
        int mode;
        String pass;

        int columnID = cursor.getColumnIndex("_id");
        int columnShortName = cursor.getColumnIndex("shortName");
        int columnAuthorID = cursor.getColumnIndex("authorID");
        int columnDescription = cursor.getColumnIndex("description");
        int columnTime = cursor.getColumnIndex("time");
        int columnRandom = cursor.getColumnIndex("random");
        int columnMode = cursor.getColumnIndex("mode");
        int columnPass = cursor.getColumnIndex("pass");
        if (cursor.moveToFirst()) {
            do {
                ID = cursor.getInt(columnID);
                shortName = cursor.getString(columnShortName);
                description = cursor.getString(columnDescription);
                authorID = cursor.getInt(columnAuthorID);
                time = cursor.getInt(columnTime);
                random = (cursor.getInt(columnRandom) == 1);
                mode = cursor.getInt(columnMode);
                pass = cursor.getString(columnPass);

                testsList.add(new Test(ID, shortName, authorID, description, time, random, mode, pass));
            }
            while (cursor.moveToNext());
        }
        else
            return false;
        cursor.close();
        return true;
    } */
}

