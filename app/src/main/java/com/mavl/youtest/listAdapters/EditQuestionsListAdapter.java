package com.mavl.youtest.listAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mavl.youtest.R;
import com.mavl.youtest.objects.Question;
import java.util.ArrayList;

import layout.EditTestQuestions;

/**
 * Created by mavl on 13.05.2017.
 */

public class EditQuestionsListAdapter extends RecyclerView.Adapter<EditQuestionsListAdapter.ViewHolder> {
    private ArrayList<Question> mDataset;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvText;
        public TextView tvCost;
        public TextView tvNumOpt;
        public LinearLayout theLabel;
        public ViewHolder(View v) {
            super(v);
            tvText = (TextView)v.findViewById(R.id.tvText);
            tvCost = (TextView)v.findViewById(R.id.tvCost);
            tvNumOpt = (TextView)v.findViewById(R.id.tvNumOpt);
            theLabel = (LinearLayout)v.findViewById(R.id.theLabel);
        }
    }

    public EditQuestionsListAdapter(ArrayList<Question> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public EditQuestionsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.question_label, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EditQuestionsListAdapter.ViewHolder holder, int position) {
        holder.tvText.setText(mDataset.get(position).getQuestionText());
        holder.tvCost.setText(mDataset.get(position).getCost()+"");
        holder.tvNumOpt.setText(mDataset.get(position).getOptionsNumber()+"");
        holder.theLabel.setOnClickListener(new EditTestQuestions.ShitOnClickListener());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
