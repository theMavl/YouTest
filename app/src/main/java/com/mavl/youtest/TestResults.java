package com.mavl.youtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mavl.youtest.listAdapters.ResultQuestionsListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    TextView tvTimeBegin;
    TextView tvTimeFinish;
    TextView tvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);
        lvResults = (ListView) findViewById(R.id.lvResults);
        tvTestName = (TextView)findViewById(R.id.tvTestName);
        tvTimeBegin = (TextView)findViewById(R.id.tvTimeBegin);
        tvTimeFinish = (TextView)findViewById(R.id.tvTimeFinish);
        tvDate = (TextView)findViewById(R.id.tvDate);

        intent = getIntent();
        resultID = intent.getIntExtra("resultID", -1);
        if (resultID == -1)
            finish();

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Log.d("get-results", "Trying to get the name of "+resultID);
        Cursor tmpCursor = tempDB.rawQuery("select results._id, tests.shortName, results.timeBegin, results.timeFinish\n" +
                "from results\n" +
                "join tests on results.testID = tests._id\n" +
                "where results._id = ?", new String[]{ resultID+"" });
        tmpCursor.moveToFirst();
        testName = tmpCursor.getString(tmpCursor.getColumnIndex("shortName"));
        tvTestName.setText(testName);



        String[] timeBegin = tmpCursor.getString(tmpCursor.getColumnIndex("timeBegin")).split("\t");
        String[] timeFinish = tmpCursor.getString(tmpCursor.getColumnIndex("timeFinish")).split("\t");


        tvTimeBegin.setText(timeBegin[1]);
        tvTimeFinish.setText(timeFinish[1]);
        tvDate.setText(timeBegin[0]);

        tmpCursor.close();

        // Get questionTexts, user answers, correct answers and score
        cursor = tempDB.rawQuery("select qresults._id, questions.questionText, questions.type, qresults.userAnswer, questions.correctOptions, qresults.score, questions.cost \n" +
                "from qresults\n" +
                "join results on qresults.resultID = results._id\n" +
                "join questions on qresults.questionID = questions._id\n" +
                "where resultID = ?", new String[]{ resultID+"" });
        totalQuestions = cursor.getCount();
        final ResultQuestionsListAdapter adapter = new ResultQuestionsListAdapter(this, cursor);
        lvResults.setAdapter(adapter);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQuestionDialog((int)adapter.getItemId(i), adapter.getCursor());
            }
        });


    }


    public void showQuestionDialog(int id, Cursor cursor) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("View question");


        View view = getLayoutInflater().inflate(R.layout.question_layout, null);

        TextView qt = (TextView) view.findViewById(R.id.txtQuestionType);



        dialog.setView(view);

        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.create();
        dialog.show();
    }

    public void exit(View view) {
        finish();
    }
}
