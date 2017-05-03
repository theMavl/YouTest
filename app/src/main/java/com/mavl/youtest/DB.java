package com.mavl.youtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mavl on 27.03.2017.
 */

public class DB extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "YouTestData.db";
    private static final int DATABASE_VERSION = 2;
    private static final String QUESTIONS_TABLE = "questions";
    private static final String TESTS_TABLE = "tests";
    private static final String USERS_TABLE = "users";
    private static final String RESULTS_TABLE = "results";
    private static final String BY_QUESTION_RESULTS_TABLE = "qresults";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String s = "CREATE TABLE "+ USERS_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "displayName TEXT NOT NULL, " +
                "login TEXT NOT NULL, " +
                "pass TEXT," +
                "admin INTEGER NOT NULL DEFAULT 0, " +
                "teacher INTEGER NOT NULL DEFAULT 0)";
        sqLiteDatabase.execSQL(s);

        s = "CREATE TABLE "+ TESTS_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "shortName TEXT NOT NULL, " +
                "authorID INTEGER NOT NULL, " +
                "questionsNum INTEGER NOT NULL DEFAULT 0, " +
                "description TEXT NOT NULL, " +
                "time INTEGER NOT NULL DEFAULT 0, " +
                "random INTEGER NOT NULL DEFAULT 0, " +
                "mode INTEGER NOT NULL DEFAULT 0, " +
                "pass TEXT, FOREIGN KEY(authorID) REFERENCES users(_id))";
        sqLiteDatabase.execSQL(s);

         s = "CREATE TABLE "+ QUESTIONS_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "testID INTEGER NOT NULL," +
                "number INTEGER NOT NULL," +
                "type INTEGER NOT NULL DEFAULT 0, " +
                "questionText TEXT NOT NULL," +
                "time INTEGER NOT NULL DEFAULT 0, " +
                "cost INTEGER NOT NULL DEFAULT 1, pic TEXT, correctOptions TEXT NOT NULL, " +
                "option1 TEXT NOT NULL, " +
                "option2 TEXT, option3 TEXT, option4 TEXT, option5 TEXT, option6 TEXT, option7 TEXT, option8 TEXT, option9 TEXT, option10 TEXT, " +
                "FOREIGN KEY(testID) REFERENCES tests(_id))";
        sqLiteDatabase.execSQL(s);

        s = "CREATE TABLE "+ RESULTS_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userID INTEGER NOT NULL, " +
                "testID INTEGER NOT NULL, " +
                "score INTEGER, " +
                "mark INTEGER, " +
                "timeBegin TEXT NOT NULL, " +
                "timeFinish TEXT NOT NULL, " +
                "FOREIGN KEY(userID) REFERENCES users(_id)" +
                "FOREIGN KEY(testID) REFERENCES tests(_id))";
        sqLiteDatabase.execSQL(s);

        s = "CREATE TABLE "+ BY_QUESTION_RESULTS_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "resultID INTEGER NOT NULL, " +
                "questionID INTEGER NOT NULL, " +
                "userAnswer TEXT NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "FOREIGN KEY(resultID) REFERENCES results(_id)" +
                "FOREIGN KEY(questionID) REFERENCES questions(_id))";
        sqLiteDatabase.execSQL(s);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
