package com.mavl.youtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mavl.youtest.listAdapters.QuickAdapter;
import com.mavl.youtest.listAdapters.ResultQuestionsListAdapter;
import com.mavl.youtest.listAdapters.StatisticListAdapter;

import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity {
    DB db;
    Cursor cursor;
    ArrayList<Integer> resultsList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        cursor.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        setTitle(getResources().getString(R.string.select_result));
        db = DataBaseCommunication.db;
        SQLiteDatabase tempDB = db.getWritableDatabase();
        //cursor = tempDB.rawQuery("SELECT  * FROM "+DB.RESULTS_TABLE, null);

        cursor = tempDB.rawQuery("select results._id, tests.shortName, users.displayName, results.timeBegin \n" +
                "from results\n" +
                "join tests on results.testID = tests._id\n" +
                "join users on results.userID = users._id", null);

        ListView lvTests = (ListView) findViewById(R.id.results);
        StatisticListAdapter adapter = new StatisticListAdapter(this, cursor);
        lvTests.setAdapter(adapter);

        cursor.moveToFirst();
        if (cursor.getCount() == 0) {

            Toast.makeText(this, getResources().getString(R.string.no_results), Toast.LENGTH_LONG).show();
            Log.d("g", "finish");
            return;
            //onDestroy();
        }

        do {
            resultsList.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        while (cursor.moveToNext());

        lvTests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                int selectedResultID;
                if (pos < resultsList.size())
                    selectedResultID = resultsList.get(pos);
                else
                    selectedResultID = -2; // new test

                Intent goToTest;

                goToTest = new Intent(getApplicationContext(), TestResults.class);

                goToTest.putExtra("resultID", selectedResultID);
                startActivity(goToTest);
                finish();
            }
        });
    }
}
