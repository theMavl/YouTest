package com.mavl.youtest;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.mavl.youtest.listAdapters.OptionsListAdapter;
import com.mavl.youtest.objects.Option;
import com.mavl.youtest.objects.Question;

import java.util.ArrayList;
import java.util.Collections;

public class EditQuestionActivity extends AppCompatActivity {

    RecyclerView options;
    ArrayList<Option> arrOptions;
    OptionsListAdapter adapter;
    Intent intent;
    int questionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        intent = getIntent();
        questionID = intent.getIntExtra("questionID", -1);

        Log.d("qID", questionID+"");

        arrOptions = new ArrayList<>();
        adapter = new OptionsListAdapter(arrOptions);
        options = (RecyclerView) findViewById(R.id.options);
        options.setAdapter(adapter);

        ItemTouchHelper mIt = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                adapter.notifyItemMoved(fromPos, toPos);
                Collections.swap(arrOptions, fromPos, toPos);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                Log.d("swipe", "removed "+pos+" "+arrOptions.get(pos).getText());
                final Option justCopy = arrOptions.get(pos);
                arrOptions.remove(pos);
                adapter.notifyItemRemoved(pos);
                Snackbar snackbar = Snackbar
                        .make(EditTestActivity.appBarLayout, "Option deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("swipe", "restored "+pos+" "+justCopy.getText());
                        arrOptions.add(pos, justCopy);
                        adapter.notifyItemInserted(pos);
                    }
                });
                snackbar.show();
            }
        });
        mIt.attachToRecyclerView(options);
    }
}

