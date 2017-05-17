package layout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mavl.youtest.DB;
import com.mavl.youtest.DataBaseCommunication;
import com.mavl.youtest.R;
import com.mavl.youtest.listAdapters.EditQuestionsListAdapter;
import com.mavl.youtest.objects.Question;

import java.util.ArrayList;

public class EditTestQuestions extends Fragment {
    int testID;
    private RecyclerView ql;
    private RecyclerView.Adapter qlAdapter;
    private RecyclerView.LayoutManager qlManager;
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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_test_questions, container, false);
        db = DataBaseCommunication.db;
        SQLiteDatabase tempDB = db.getWritableDatabase();
        cursor = tempDB.query(DB.QUESTIONS_TABLE, null, "testID = " + testID, null, null, null, "number");
        ArrayList<Question> questions = new ArrayList<>();
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

        return view;
    }


    public static EditTestQuestions newInstance(int testID) {
        EditTestQuestions editTestQuestions = new EditTestQuestions();
        Bundle args = new Bundle();
        args.putInt("testID", testID);
        editTestQuestions.setArguments(args);
        return editTestQuestions;
    }

}
