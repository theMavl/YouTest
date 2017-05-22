package com.mavl.youtest.objects;

import android.database.Cursor;
import android.util.Log;

/**
 * Created by mavl on 27.03.2017.
 */

public class Test {
    private int ID = 0;
    private String shortName = "";
    private int authorID = 0;
    private int questionNum = 0;
    private String description = "";
    private int time = 0;
    private boolean random = false;
    private int mode = 0;
    private String pass = "";

    public Test() {
        this.ID = -2;
    }

    public Test(int ID, String shortName, int authorID, int questionNum, String description, int time, boolean random, int mode, String pass) {
        //this.ID = ID;
        this.shortName = shortName;
        this.authorID = authorID;
        this.questionNum = questionNum;
        this.description = description;
        this.time = time;
        this.random = random;
        this.mode = mode;
        this.pass = pass;
    }

    public Test (Cursor cursor) {
        Log.d("parse test", cursor.getCount()+" "+cursor.getColumnCount());
        cursor.moveToFirst();
            this.ID = cursor.getInt(cursor.getColumnIndex("_id"));
            this.shortName = cursor.getString(cursor.getColumnIndex("shortName"));
            this.authorID = cursor.getInt(cursor.getColumnIndex("authorID"));
            this.questionNum = cursor.getInt(cursor.getColumnIndex("questionsNum"));
            this.description = cursor.getString(cursor.getColumnIndex("description"));
            this.time = cursor.getInt(cursor.getColumnIndex("time"));
            this.random = (cursor.getInt(cursor.getColumnIndex("random"))) == 1;
            this.mode = cursor.getInt(cursor.getColumnIndex("mode"));
            this.pass = cursor.getString(cursor.getColumnIndex("pass"));
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
