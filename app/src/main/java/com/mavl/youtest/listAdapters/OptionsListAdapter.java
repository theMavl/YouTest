package com.mavl.youtest.listAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mavl.youtest.R;
import com.mavl.youtest.objects.Option;
import com.mavl.youtest.objects.Question;

import java.util.ArrayList;

/**
 * Created by mavl on 13.05.2017.
 */

public class OptionsListAdapter extends RecyclerView.Adapter<OptionsListAdapter.ViewHolder> {
    private ArrayList<Option> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText optionText;
        public CheckBox correct;
        public ViewHolder(View v) {
            super(v);
            optionText = (EditText)v.findViewById(R.id.optionText);
            correct = (CheckBox)v.findViewById(R.id.correct);
        }
    }

    public OptionsListAdapter(ArrayList<Option> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public OptionsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.option_label, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(OptionsListAdapter.ViewHolder holder, int position) {
        holder.optionText.setText(mDataset.get(position).getText());
        holder.correct.setChecked(mDataset.get(position).isCorrect());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
