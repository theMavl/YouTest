package com.mavl.youtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mavl.youtest.listAdapters.ResultQuestionsListAdapter;

public class TestResults extends AppCompatActivity {

    Intent intent;
    int resultID;
    int testID;
    int userID;
    String testName;
    int totalQuestions;
    DB db = DataBaseCommunication.db;
    Cursor cursor;
    ListView lvResults;
    TextView tvTestName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);
        lvResults = (ListView) findViewById(R.id.lvResults);
        tvTestName = (TextView)findViewById(R.id.tvTestName);

        intent = getIntent();
        resultID = intent.getIntExtra("resultID", -1);
        if (resultID == -1)
            finish();

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Log.d("get-results", "Trying to get the name of "+resultID);
        Cursor tmpCursor = tempDB.rawQuery("select results._id, tests.shortName\n" +
                "from results\n" +
                "join tests on results.testID = tests._id\n" +
                "where results._id = ?", new String[]{ resultID+"" });
        tmpCursor.moveToFirst();
        testName = tmpCursor.getString(tmpCursor.getColumnIndex("shortName"));
        tvTestName.setText(testName);
        tmpCursor.close();

        // Get questionTexts, user answers, correct answers and score
        cursor = tempDB.rawQuery("select qresults._id, questions.questionText, qresults.userAnswer, questions.correctOptions, qresults.score, questions.cost \n" +
                "from qresults\n" +
                "join results on qresults.resultID = results._id\n" +
                "join questions on qresults.questionID = questions._id\n" +
                "where resultID = ?", new String[]{ resultID+"" });
        totalQuestions = cursor.getCount();
        ResultQuestionsListAdapter adapter = new ResultQuestionsListAdapter(this, cursor);
        lvResults.setAdapter(adapter);

    }

    public void exit(View view) {
        finish();
    }
}
