package com.mavl.youtest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class theTest extends AppCompatActivity {

    ArrayList<Question> questionsList = new ArrayList<>();
    DB db;
    TextView txtQuestionType;
    TextView txtQuestionText;
    ImageView questionPic;
    LinearLayout lyOptions;
    Button btPrevious;
    Button btFinish;
    Button btNext;
    int currentQuestionID;
    int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_test);
        txtQuestionText = (TextView)findViewById(R.id.txtQuestionText);
        txtQuestionType = (TextView)findViewById(R.id.txtQuestionType);
        questionPic = (ImageView)findViewById(R.id.questionPic);
        lyOptions = (LinearLayout)findViewById(R.id.lyOptions);
        btPrevious = (Button)findViewById(R.id.btPrevious);
        btFinish = (Button)findViewById(R.id.btFinish);
        btNext = (Button)findViewById(R.id.btNext);

        Intent intent = getIntent();
        db = DataBaseCommunication.db;
        int testID = intent.getIntExtra("testID", -1);
        getQuestions(testID);
        Collections.shuffle(questionsList);
        totalQuestions = questionsList.size();
        renderQuestion();

    }

    void renderQuestion() {
        Question currentQuestion = questionsList.get(currentQuestionID);
        String questionText = currentQuestion.getQuestionText();
        ArrayList<String> options = currentQuestion.options;
        int questionType = currentQuestion.getType();
        txtQuestionType.setText(questionTypeString(questionType));
        txtQuestionText.setText(questionText);
    }



    String questionTypeString(int type) {
        switch (type) {
            case 0:
                return getResources().getString(R.string.radioactive_question);
            case 1:
                return getResources().getString(R.string.multi_question);
            case 2:
                return getResources().getString(R.string.writing_question);
        }
        return "";
    }

    boolean getQuestions(int testID) {
        SQLiteDatabase tempDB = db.getWritableDatabase();
        if (testID == -1)
            return false;

        questionsList.clear();
        Cursor cursor = tempDB.query("questions", null, "testID = " + testID, null, null, "number", null);
        cursor.moveToFirst();

            int ID;
            int type;
            int number;
            String questionText;
            int time;
            int cost;
            String pic;
            int[] correctOptions;
            ArrayList<String> options = new ArrayList<>();
            int columnID = cursor.getColumnIndex("_id");
            int columnNumber = cursor.getColumnIndex("number");
            int columnType = cursor.getColumnIndex("type");
            int columnQuestionText = cursor.getColumnIndex("questionText");
            int columnTime = cursor.getColumnIndex("time");
            int columnCost = cursor.getColumnIndex("cost");
            int columnCorrectOptions = cursor.getColumnIndex("correctOptions");
            int columnOption1 = cursor.getColumnIndex("option1");
            do {
                ID = cursor.getInt(columnID);
                type = cursor.getInt(columnType);
                number = cursor.getInt(columnNumber);
                questionText = cursor.getString(columnQuestionText);
                time = cursor.getInt(columnTime);
                cost = cursor.getInt(columnCost);
                correctOptions = Question.parseCorrects(cursor.getString(columnCorrectOptions));
                int j = 1;
                for (int i = columnOption1; i < columnOption1+10; i++) {
                    options.add(j, cursor.getString(i));
                }
                questionsList.add(new Question(ID, testID, type, number, questionText, time, cost, "", correctOptions, options));
                options.clear();
            }
            while (cursor.moveToNext());
        cursor.close();
        return true;
    }
}
