package com.mavl.youtest.listAdapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mavl.youtest.R;

/**
 * Created by mavl on 17.04.2017.
 */

public class ResultQuestionsListAdapter extends CursorAdapter {

    public ResultQuestionsListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.question_result_label, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvUA = (TextView) view.findViewById(R.id.tvUA);
        TextView tvCA = (TextView) view.findViewById(R.id.tvCA);
        TextView tvQT = (TextView) view.findViewById(R.id.tvQT);
        TextView tvScore = (TextView) view.findViewById(R.id.tvScore);
        // Extract properties from cursor
        String ua = cursor.getString(cursor.getColumnIndex("userAnswer"));
        String ca = cursor.getString(cursor.getColumnIndex("correctOptions"));
        String qt = cursor.getString(cursor.getColumnIndex("questionText"));
        String score = cursor.getString(cursor.getColumnIndex("score"));
        String cost = cursor.getString(cursor.getColumnIndex("cost"));
        // Populate fields with extracted properties
        tvUA.setText(ua.equals("-1")? "нет ответа":ua);
        tvCA.setText(ca);
        tvQT.setText(qt);
        tvScore.setText(score+"/"+cost);
    }
}
