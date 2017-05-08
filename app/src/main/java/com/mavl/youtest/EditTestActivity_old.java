package com.mavl.youtest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.mavl.youtest.objects.Test;

public class EditTestActivity_old extends AppCompatActivity {

    final int INSERT = 0;
    final int EDIT = 1;

    int testID;
    DB db;
    int mode;

    TextView tvTestID;
    TextView tvAuthor;
    EditText etName;
    EditText etDescr;
    Test thisQuestion;
    Switch swRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test_old);


        tvTestID = (TextView) findViewById(R.id.tvTestID);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        etName = (EditText) findViewById(R.id.etName);
        etDescr = (EditText) findViewById(R.id.etDescr);
        swRandom = (Switch) findViewById(R.id.swRandom);

        Intent intent = getIntent();
        db = DataBaseCommunication.db;
        testID = intent.getIntExtra("testID", -1);

        if (testID == -1)
            finish();
        if (testID == -2) {
            tvTestID.setText("New test");
            mode = INSERT;
        }
        else {
            tvTestID.setText("Test " + testID);
            mode = EDIT;
        }

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Cursor cursor;

        if (mode == INSERT) {
            thisQuestion = new Test();
    }

        if (mode == EDIT) {
            cursor = tempDB.query(DB.TESTS_TABLE, null, "_id = " + testID, null, null, null, null);
            Log.d("edit", cursor.getCount()+"");
            thisQuestion = new Test(cursor);
            etName.setText(thisQuestion.getShortName());
            etDescr.setText(thisQuestion.getDescription());
            swRandom.setChecked(thisQuestion.isRandom());
            cursor = tempDB.query(DB.USERS_TABLE, null, "_id = " + thisQuestion.getAuthorID(), null, null, null, null);
            if (cursor.getCount() > 0)
                tvAuthor.setText(cursor.getString(cursor.getColumnIndex("displayName")));
            else
                tvAuthor.setText("Undefined");
            cursor.close();
        }

    }
}
