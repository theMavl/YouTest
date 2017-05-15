package com.mavl.youtest.listAdapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mavl.youtest.R;

/**
 * Created by student2 on 15.05.17.
 */

public class StatisticListAdapter extends CursorAdapter {

    public StatisticListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.result_label, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView testName = (TextView) view.findViewById(R.id.testName);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView time = (TextView) view.findViewById(R.id.time);
        // Extract properties from cursor
        String sTestName = cursor.getString(cursor.getColumnIndexOrThrow("shortName"));
        String sUserName = cursor.getString(cursor.getColumnIndexOrThrow("displayName"));
        String sTime = cursor.getString(cursor.getColumnIndexOrThrow("timeBegin"));
        // Populate fields with extracted properties
        testName.setText(sTestName);
        userName.setText(sUserName);
        time.setText(sTime);
    }
}
