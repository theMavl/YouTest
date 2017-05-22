package com.mavl.youtest.listAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mavl.youtest.R;
import com.mavl.youtest.objects.Option;
import com.mavl.youtest.objects.Question;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by mavl on 13.05.2017.
 */

public class OptionsListAdapter extends RecyclerView.Adapter<OptionsListAdapter.ViewHolder> {
    private ArrayList<Option> mDataset;
    private RadioButton lastCheckedRB = null;
    private Option lastCorrect;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView optionText;
        public RadioButton correct;
        public ViewHolder(View v) {
            super(v);
            optionText = (TextView)v.findViewById(R.id.optionText);
            correct = (RadioButton)v.findViewById(R.id.correct);
        }
    }

    public OptionsListAdapter(ArrayList<Option> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
        Log.d("piii", mDataset.toString());
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
    public void onBindViewHolder(OptionsListAdapter.ViewHolder holder, final int position) {
        final OptionsListAdapter.ViewHolder tmpHolder = holder;
        holder.optionText.setText(mDataset.get(position).getText());
        holder.correct.setChecked(mDataset.get(position).isCorrect());
        if (mDataset.get(position).isCorrect()) {
            lastCheckedRB = holder.correct;
            lastCorrect = mDataset.get(position);
        }
        holder.correct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                    lastCorrect.setCorrect(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = (RadioButton) compoundButton;
                mDataset.get(position).setCorrect(true);
                lastCorrect = mDataset.get(position);
            }
        });
        holder.optionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(mDataset.get(position), view);
                tmpHolder.optionText.setText(mDataset.get(position).getText());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showEditDialog(final Option option, View kost) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.password_request, null);
        final EditText text = (EditText)view.findViewById(R.id.pass);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
        text.setText(option.getText());
        //dialog.setTitle(context.getResources().getString(R.string.request_password));
        dialog.setView(view);
        final TextView tv = (TextView)kost;
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inText = text.getText().toString();
                        option.setText(inText);
                        tv.setText(inText);
                    }
                });

        dialog.create();
        dialog.show();
    }
}
