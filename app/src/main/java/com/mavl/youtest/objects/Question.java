package com.mavl.youtest.objects;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mavl.youtest.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mavl on 27.03.2017.
 */

public class Question {
    int ID;
    int testID;
    int type;
    int number;
    String questionText;
    int time;
    int cost;
    String pic;
    String userAnswer;
    public int[] correctOptions;
    public ArrayList<String> options = new ArrayList<>();

    Question() {}

    public Question(Cursor cursor) {
        String tmpOption;
        int columnID = cursor.getColumnIndex("_id");
        int columnNumber = cursor.getColumnIndex("number");
        int columnType = cursor.getColumnIndex("type");
        int columnQuestionText = cursor.getColumnIndex("questionText");
        int columnTime = cursor.getColumnIndex("time");
        int columnCost = cursor.getColumnIndex("cost");
        int columnCorrectOptions = cursor.getColumnIndex("correctOptions");
        int columnOption1 = cursor.getColumnIndex("option1");

        this.ID = cursor.getInt(columnID);
        this.type = cursor.getInt(columnType);
        this.number = cursor.getInt(columnNumber);
        this.questionText = cursor.getString(columnQuestionText);
        this.time = cursor.getInt(columnTime);
        this.cost = cursor.getInt(columnCost);

        this.correctOptions = Question.parseCorrects(cursor.getString(columnCorrectOptions));
        int j = 0;
        for (int i = columnOption1; i < columnOption1+10; i++) {
            tmpOption = cursor.getString(i);

            if (tmpOption != null) {
                this.options.add(j, tmpOption);
                j++;
            }
            else
                break;
        }
    }

    public Question(int ID, int testID, int type, int number, String questionText, int time, int cost, String pic, int[] correctOptions, ArrayList<String> options) {
        this.ID = ID;
        this.testID = testID;
        this.type = type;
        this.number = number;
        this.questionText = questionText;
        this.time = time;
        this.cost = cost;
        this.pic = pic;
        this.correctOptions = correctOptions;
        this.options = (ArrayList<String>)options.clone();
        Log.d("new Question", this.options.toString() +" "+this.options.size()+" "+this);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int[] getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(int[] correctOptions) {
        this.correctOptions = correctOptions;
    }

    public String getOption(int id) {
        return options.get(id);
    }

    public void setOption(int id, String newText) {
        this.options.set(id, newText);
    }

    public int getOptionsNumber() { return options.size(); }

    public boolean addOption(String optionText) {
        if (this.options.size() > 9)
            return false;
        options.add(optionText);
        return true;
    }

    public static int[] parseCorrects(String line) {
        int[] corrects = new int[10];
        String[] arrLine = line.split(";");
        for (int i = 0; i < arrLine.length; i++) {
            corrects[i] = Integer.parseInt(arrLine[i]);
        }
        return corrects;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getTypeString(Context context) {
        switch (type) {
            case 0:
                return context.getResources().getString(R.string.radioactive_question);
            case 1:
                return context.getResources().getString(R.string.multi_question);
            case 2:
                return context.getResources().getString(R.string.writing_question);
        }
        return "";
    }

    public String getUserAnswer() {
        return this.userAnswer;
    }

    public int checkUserAnswer() {
        String[] answer = userAnswer.split(";");
        switch (this.type) {
            case 0:
                int a = Integer.parseInt(answer[0]);
                if (a == correctOptions[0])
                    return this.cost;
                break;
        }
        return 0;
    }
}
