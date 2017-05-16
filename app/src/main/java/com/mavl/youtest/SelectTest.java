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
        SQLiteDatabase tempDB = db.getWritableDatabase();
        cursor = tempDB.rawQuery("SELECT  * FROM tests", null);
        ListView lvTests = (ListView) findViewById(R.id.tests);
        QuickAdapter adapter = new QuickAdapter(this, cursor);
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
}

