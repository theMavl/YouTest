package com.mavl.youtest.listAdapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mavl.youtest.R;

import java.util.Locale;

public class QuickAdapter extends CursorAdapter {

    public QuickAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.test_label, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvShortName = (TextView) view.findViewById(R.id.shortName);
        TextView tvDescription = (TextView) view.findViewById(R.id.description);
        TextView tvQN = (TextView) view.findViewById(R.id.tvQN);
        // Extract properties from cursor
        String shortName = cursor.getString(cursor.getColumnIndexOrThrow("shortName"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String qn = cursor.getString(cursor.getColumnIndexOrThrow("questionsNum"));
        // Populate fields with extracted properties
        tvShortName.setText(shortName);
        tvDescription.setText(description);
        tvQN.setText(qn+ " " + questionEnding(Integer.parseInt(qn)));
    }
    @NonNull
    private String questionEnding(int i) {
        String lang = Locale.getDefault().getLanguage();
        Log.d("q-E", lang);
        if (lang.equals("ru")) {
            if (i > 9)
                i = i % 10;
            if (i == 1)
                return "вопрос";
            if ((i > 1) && (i < 5))
                return "вопроса";
            else
                return "вопросов";
        }
        else
            if (i == 1) return "question";
            else return "questions";
    }
}

