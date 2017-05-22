package layout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mavl.youtest.DB;
import com.mavl.youtest.DataBaseCommunication;
import com.mavl.youtest.EditQuestionActivity;
import com.mavl.youtest.EditTestActivity;
import com.mavl.youtest.R;
import com.mavl.youtest.listAdapters.EditQuestionsListAdapter;
import com.mavl.youtest.objects.Question;

import java.util.ArrayList;
import java.util.Collections;

public class EditTestQuestions extends Fragment {
    int testID;
    private static RecyclerView ql;
    public static RecyclerView.Adapter qlAdapter;
    private RecyclerView.LayoutManager qlManager;
    public static ArrayList<Question> questions;
    static Context thisShit;
    CoordinatorLayout qLy;
    DB db;
    Cursor cursor;

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testID = getArguments().getInt("testID", -1);
        thisShit = getContext();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_test_questions, container, false);
        qLy = (CoordinatorLayout)view.findViewById(R.id.qLy);

        db = DataBaseCommunication.db;
        SQLiteDatabase tempDB = db.getWritableDatabase();
        cursor = tempDB.query(DB.QUESTIONS_TABLE, null, "testID = " + testID, null, null, null, "number");
        questions = new ArrayList<>();
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
        {
            do {
                questions.add(new Question(cursor));
            } while (cursor.moveToNext());
        }
        ql = (RecyclerView) view.findViewById(R.id.ql);
        qlManager = new LinearLayoutManager(getActivity());
        ql.setLayoutManager(qlManager);
        qlAdapter = new EditQuestionsListAdapter(questions);
        ql.setAdapter(qlAdapter);

        ItemTouchHelper mIt = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                qlAdapter.notifyItemMoved(fromPos, toPos);
                Collections.swap(questions, fromPos, toPos);
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                Log.d("swipe", "removed "+pos+" "+questions.get(pos).getQuestionText());
                final Question justCopy = questions.get(pos);
                questions.remove(pos);
                qlAdapter.notifyItemRemoved(pos);
                Snackbar snackbar = Snackbar
                        .make(EditTestActivity.appBarLayout, "Question deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("swipe", "restored "+pos+" "+justCopy.getQuestionText());
                        questions.add(pos, justCopy);
                        qlAdapter.notifyItemInserted(pos);
                    }
                });
                snackbar.show();
            }

        });
        mIt.attachToRecyclerView(ql);


        return view;
    }


    public static EditTestQuestions newInstance(int testID) {
        EditTestQuestions editTestQuestions = new EditTestQuestions();
        Bundle args = new Bundle();
        args.putInt("testID", testID);
        editTestQuestions.setArguments(args);
        return editTestQuestions;
    }

    public static class ShitOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = ql.indexOfChild(v);
            Intent intent = new Intent(thisShit, EditQuestionActivity.class);
            intent.putExtra("questionN", itemPosition);
            thisShit.startActivity(intent);
        }
    }

}

