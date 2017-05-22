package com.mavl.youtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mavl.youtest.listAdapters.ResultQuestionsListAdapter;
import com.mavl.youtest.objects.Question;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestResults extends AppCompatActivity {

    Intent intent;
    int resultID;
    int testID;
    int userID;
    int mark;
    double score;
    String testName;
    int totalQuestions;
    DB db = DataBaseCommunication.db;
    Cursor cursor;
    ListView lvResults;
    TextView tvTestName;
    TextView tvTimeBegin;
    TextView tvTimeFinish;
    TextView tvDate;
    TextView tvScore;
    TextView tvMark;

    LinearLayout lyOptions;
    RadioGroup radioGroup;
    RadioButton[] radioButtons = new RadioButton[10];
    ImageView questionPic;
    boolean buttonsAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);
        lvResults = (ListView) findViewById(R.id.lvResults);
        tvTestName = (TextView)findViewById(R.id.tvTestName);
        tvTimeBegin = (TextView)findViewById(R.id.tvTimeBegin);
        tvTimeFinish = (TextView)findViewById(R.id.tvTimeFinish);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvScore = (TextView)findViewById(R.id.tvScore);
        tvMark = (TextView)findViewById(R.id.tvMark);

        intent = getIntent();
        resultID = intent.getIntExtra("resultID", -1);
        if (resultID == -1)
            finish();

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Log.d("get-results", "Trying to get the name of "+resultID);
        Cursor tmpCursor = tempDB.rawQuery("select results._id, tests.shortName, results.timeBegin, results.timeFinish, results.score, results.mark\n" +
                "from results\n" +
                "join tests on results.testID = tests._id\n" +
                "where results._id = ?", new String[]{ resultID+"" });
        tmpCursor.moveToFirst();
        testName = tmpCursor.getString(tmpCursor.getColumnIndex("shortName"));
        tvTestName.setText(testName);

        String[] timeBegin = tmpCursor.getString(tmpCursor.getColumnIndex("timeBegin")).split("\t");
        String[] timeFinish = tmpCursor.getString(tmpCursor.getColumnIndex("timeFinish")).split("\t");
        mark = tmpCursor.getInt(tmpCursor.getColumnIndex("mark"));
        score = tmpCursor.getDouble(tmpCursor.getColumnIndex("score"));


        tvTimeBegin.setText(timeBegin[1]);
        tvTimeFinish.setText(timeFinish[1]);
        tvDate.setText(timeBegin[0]);
        tvMark.setText(mark+"");
        tvScore.setText(Math.round(score*100)+"%");

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
        setListViewHeightBasedOnChildren(lvResults);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQuestionDialog((int)adapter.getItemId(i));
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        int lastHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            lastHeight = view.getMeasuredHeight();
            totalHeight += lastHeight;
        }
        totalHeight+=lastHeight;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void showQuestionDialog(int id) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //dialog.setTitle("View question");
        Cursor cursor;
        int questionID;
        String userAnswer;
        SQLiteDatabase tempDB = db.getWritableDatabase();
        cursor = tempDB.rawQuery("select * from "+DB.BY_QUESTION_RESULTS_TABLE+" where _id = "+id,null);
        cursor.moveToFirst();
        questionID = cursor.getInt(cursor.getColumnIndex("questionID"));
        userAnswer = cursor.getString(cursor.getColumnIndex("userAnswer"));
        cursor = tempDB.rawQuery("select * from "+DB.QUESTIONS_TABLE+" where _id = "+questionID,null);
        cursor.moveToFirst();
        Question currentQuestion = new Question(cursor);
        cursor.close();
        currentQuestion.setUserAnswer(userAnswer);

        View view = getLayoutInflater().inflate(R.layout.question_layout, null);
        TextView qt = (TextView) view.findViewById(R.id.txtQuestionType);
        TextView qn = (TextView) view.findViewById(R.id.txtQuestionText);
        questionPic = (ImageView) view.findViewById(R.id.questionPic);
        lyOptions = (LinearLayout) view.findViewById(R.id.lyOptions);
        radioGroup = new RadioGroup(this);

        qt.setText(currentQuestion.getTypeString(this));
        qn.setText(currentQuestion.getQuestionText());

        if (!buttonsAvailable)
            initOptionViews();

        switch (currentQuestion.getType()) {
            case 0:
                int nUserAnswer = Integer.parseInt(userAnswer);
                questionPic.setVisibility(View.GONE);
                for (int i = 0; i < currentQuestion.getOptionsNumber(); i++) {
                    Log.d("render-buttons", "added "+i);
                    radioButtons[i].setText(currentQuestion.getOption(i).getText());
                    if ((i+1) == nUserAnswer)
                        radioButtons[i].setChecked(true);
                    else
                        radioButtons[i].setChecked(false);
                    radioGroup.addView(radioButtons[i]);
                }
                lyOptions.addView(radioGroup);
                break;
            case 1:
                break;
            case 2:
                break;
        }



        dialog.setView(view);

        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                lyOptions.removeAllViews();
                radioGroup.removeAllViews();
            }
        });

        dialog.create();
        dialog.show();
    }

    public void exit(View view) {
        finish();
    }

    void initOptionViews() {
        radioGroup = new RadioGroup(this);
        for (int i = 0; i < 10; i++) {
            radioButtons[i] = new RadioButton(this);
            radioButtons[i].setClickable(false);
        }
        buttonsAvailable = true;
    }
}
