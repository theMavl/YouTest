package com.mavl.youtest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mavl.youtest.objects.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class theTest extends AppCompatActivity {

    ArrayList<Question> questionsList = new ArrayList<>();
    DB db;

    TextView txtQuestionType;
    TextView txtQuestionText;
    TextView txtCounter;
    TextView txtPrice;
    ImageView questionPic;
    LinearLayout lyOptions;
    RadioGroup radioGroup;
    RadioButton[] radioButtons = new RadioButton[10];
    Button btPrevious;
    Button btFinish;
    Button btNext;
    ProgressBar testProgress;

    int currentQuestionID;
    int totalQuestions;
    Question currentQuestion;
    String[] answers;
    int testID;

    String timeStart;
    String timeFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_test);

        txtQuestionText = (TextView)findViewById(R.id.txtQuestionText);
        txtQuestionType = (TextView)findViewById(R.id.txtQuestionType);
        txtCounter = (TextView)findViewById(R.id.txtCounter);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        questionPic = (ImageView)findViewById(R.id.questionPic);
        lyOptions = (LinearLayout)findViewById(R.id.lyOptions);
        btPrevious = (Button)findViewById(R.id.btPrevious);
        //btFinish = (Button)findViewById(R.id.btFinish);
        btNext = (Button)findViewById(R.id.btNext);
        testProgress = (ProgressBar)findViewById(R.id.testProgress);
        initRadioButtons();

        Intent intent = getIntent();
        db = DataBaseCommunication.db;
        testID = intent.getIntExtra("testID", -1);
        Log.d("the-test", "Got id = "+testID);
        if (getQuestions(testID)) {
            totalQuestions = questionsList.size();
            testProgress.setProgress(0);
            testProgress.setMax(totalQuestions);
            answers = new String[totalQuestions];
            timeStart = saveTime();
            renderQuestion();
        }
        else {
            Log.d("GetQuestions", "Empty!");
            finish();
        }
    }

    String saveTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        String datetime = dateFormat.format(date);
        return datetime;
    }

    void initRadioButtons() {
        radioGroup = new RadioGroup(this);
        for (int i = 0; i < 10; i++) {
            radioButtons[i] = new RadioButton(this);
        }
    }

    void updateInfoBar() {
        txtCounter.setText(String.format("%d/%d", currentQuestionID+1, totalQuestions));
        txtPrice.setText(String.format("%d %s", currentQuestion.getCost(), getResources().getString(R.string.score_point)));
        txtQuestionType.setTextColor(getColor(android.R.color.darker_gray));
        testProgress.setProgress(currentQuestionID);

    }

    void renderQuestion() {
        currentQuestion = questionsList.get(currentQuestionID);
        Log.d("renderQuestion", questionsList +" "+currentQuestion.options.toString());
        String questionText = currentQuestion.getQuestionText();
        ArrayList<String> options = currentQuestion.options;
        int questionType = currentQuestion.getType();
        txtQuestionType.setText(questionTypeString(questionType));
        txtQuestionText.setText(questionText);
        updateInfoBar();
        renderButtons();
    }

    public void applyAnswer(View view) {
        checkButtons:
        {
            switch (currentQuestion.getType()) {
                case 0:
                    // TODO: This won't work out if shuffle the answers
                    for (int i = 0; i < currentQuestion.getOptionsNumber(); i++)
                        if (radioButtons[i].isChecked()) {
                            currentQuestion.setUserAnswer((i+1)+"");
                            //answers[currentQuestionID] = (i + 1) + "";
                            break checkButtons;
                        }
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            txtQuestionType.setTextColor(getColor(android.R.color.holo_red_dark));
            //Toast.makeText(this, getResources().getString(R.string.empty_answer), Toast.LENGTH_SHORT).show();
            return;
        }
        currentQuestionID++;
        if (currentQuestionID == totalQuestions) {
            btNext.setEnabled(false);
            finishTest();
            finish();
            return;
        }
        renderQuestion();
    }

    void finishTest() {
        timeFinish = saveTime();

        SQLiteDatabase tempDB = db.getWritableDatabase();
        ContentValues valueResults = new ContentValues();
        ContentValues valueQresults = new ContentValues();

        valueResults.put("userID", 1);
        valueResults.put("testID", testID);
        valueResults.put("timeBegin", timeStart);
        valueResults.put("timeFinish", timeFinish);
        tempDB.insert("results", null, valueResults);
        Cursor c = tempDB.query("results", null, null, null, null, null, null);
        c.moveToLast();

        int resultID = c.getInt(c.getColumnIndex("_id"));

        int userScore = 0;
        int totalScore = 0;
        int score = 0;
        int[] results = new int[totalQuestions];
        Question currentQuestion;
        for (int i = 0; i < totalQuestions; i++) {
            currentQuestion = questionsList.get(i);
            score = currentQuestion.checkUserAnswer();
            if (score > 0) {
                results[i] = score;
                userScore++;
            }
            valueQresults.put("resultID", resultID);
            valueQresults.put("questionID", currentQuestion.getID());
            valueQresults.put("userAnswer", currentQuestion.getUserAnswer());
            valueQresults.put("score", score);
            tempDB.insert("qresults", null, valueQresults);

            totalScore += currentQuestion.getCost();
        }
        double finalScore = 1.0*userScore/totalScore;
        long mark = Math.round(finalScore*5);
        valueResults.put("score", finalScore);
        valueResults.put("mark", mark);
        tempDB.update("results", valueResults, "_id = " + resultID, null);
        Log.d("Finish test", "User score: "+userScore+ " total score: "+totalScore);
        Intent intent = new Intent(this, TestResults.class);
        intent.putExtra("resultID", resultID);
        startActivity(intent);
    }

    void renderButtons() {
        //currentQuestion = questionsList.get(currentQuestionID);
        Log.d("renderButtons", currentQuestion.toString()+" "+currentQuestion.options.toString()+" "+currentQuestion.correctOptions[0]);
        Log.d("renderButtons", currentQuestion.getQuestionText()+" "+currentQuestion.getOption(0));
        lyOptions.removeAllViews();
        radioGroup.removeAllViews();
        Log.d("render-buttons", currentQuestion.getType()+" "+currentQuestion.getOptionsNumber()+" "+currentQuestion.options.toString());
        switch (currentQuestion.getType()) {
            case 0:
                questionPic.setVisibility(View.GONE);
                for (int i = 0; i < currentQuestion.getOptionsNumber(); i++) {
                    Log.d("render-buttons", "added "+i);
                    radioButtons[i].setText(currentQuestion.getOption(i));
                    radioGroup.addView(radioButtons[i]);
                }
                radioGroup.clearCheck();
                lyOptions.addView(radioGroup);
                break;
            case 1:
                break;
            case 2:
                break;
        }

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
        Cursor cursor = tempDB.query("questions", null, "testID = " + testID, null, null, null, "number");
        //cursor.moveToFirst();

            int ID;
            int type;
            int number;
            String questionText;
            int time;
            int cost;
            String pic;
            String tmpOption;
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
        if (cursor.moveToFirst()) {
            do {
                ID = cursor.getInt(columnID);
                type = cursor.getInt(columnType);
                number = cursor.getInt(columnNumber);
                questionText = cursor.getString(columnQuestionText);
                time = cursor.getInt(columnTime);
                cost = cursor.getInt(columnCost);

                correctOptions = Question.parseCorrects(cursor.getString(columnCorrectOptions));
                int j = 0;
                for (int i = columnOption1; i < columnOption1+10; i++) {
                    tmpOption = cursor.getString(i);

                    if (tmpOption != null) {
                        options.add(j, tmpOption);
                        Log.d("get"+i, cursor.getString(i)+"");
                        j++;
                    }
                    else
                        break;
                }
                Log.d("get", options.toString());
                questionsList.add(new Question(ID, testID, type, number, questionText, time, cost, "", correctOptions, options));
                options.clear();
            }
            while (cursor.moveToNext());
            for (Question x: questionsList) {
                Log.d("get-question-result", x + " " + x.getQuestionText());
            }
        }
        else
            return false;

        cursor.close();
        return true;
    }
}
