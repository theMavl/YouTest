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
    boolean isRemovedFromListInEditor;
    //public boolean[] correctOptions = new boolean[10];
    public ArrayList<Option> options = new ArrayList<>();

    public Question() {}

    public Question(Cursor cursor) {
        Option tmpOption;
        String option;
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

        boolean[] correctOptions = Question.parseCorrects(cursor.getString(columnCorrectOptions));

        int j = 0;
        for (int i = columnOption1; i < columnOption1+10; i++) {
            option = cursor.getString(i);
            if (option != null) {
                tmpOption = new Option(cursor.getString(i), correctOptions[j+1]);
                this.options.add(j, tmpOption);
                j++;
            }
            else
                break;
        }
    }

    public Question(int ID, int testID, int type, int number, String questionText, int time, int cost, String pic, /*boolean[] correctOptions, */ArrayList<String> options) {
        this.ID = ID;
        this.testID = testID;
        this.type = type;
        this.number = number;
        this.questionText = questionText;
        this.time = time;
        this.cost = cost;
        this.pic = pic;
        //this.correctOptions = correctOptions;
        this.options = (ArrayList<Option>)options.clone();
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

    public ArrayList<String> getOptionsLabels() {
        ArrayList<String> r = new ArrayList<String>();
        for (Option x: options)
            r.add(x.getText());
        return r;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    /*public boolean[] getCorrectOptions() {
        return correctOptions;
    }*/

    public boolean isRemovedFromListInEditor() {
        return isRemovedFromListInEditor;
    }

    public void setRemovedFromListInEditor(boolean removedFromListInEditor) {
        isRemovedFromListInEditor = removedFromListInEditor;
    }

    /*public void setCorrectOptions(boolean[] correctOptions) {
        this.correctOptions = correctOptions;
    }*/

    public Option getOption(int id) {
        return options.get(id);
    }

    public void setOption(int id, Option newOption) {
        this.options.set(id, newOption);
    }

    public int getOptionsNumber() { return options.size(); }

    public boolean addOption(String optionText) {
        if (this.options.size() > 9)
            return false;
        options.add(new Option(optionText));
        return true;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static boolean[] parseCorrects(String line) {
        boolean[] corrects = new boolean[11];
        String[] arrLine = line.split("\t");
        int n;
        for (int i = 0; i < arrLine.length; i++) {
            n = Integer.parseInt(arrLine[i]);
            corrects[n] = true;
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

    public static void syncNumbersAndPos(ArrayList<Question> qInput) {
        for (int i = 0; i < qInput.size(); i++)
            qInput.get(i).number = i;
    }

    public int checkUserAnswer() {
        if (userAnswer == null)
            return 0;
        String[] answer = userAnswer.split("\t");
        switch (this.type) {
            case 0:
                int a = Integer.parseInt(answer[0]);
                if (options.get(a-1).isCorrect())
                    return this.cost;
                break;
            case 1:
                boolean[] inAnsw = new boolean[10];

                break;
            case 2:
                break;
        }
        return 0;
    }
}
