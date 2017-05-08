package layout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mavl.youtest.DB;
import com.mavl.youtest.DataBaseCommunication;
import com.mavl.youtest.EditTestActivity;
import com.mavl.youtest.R;
import com.mavl.youtest.objects.Test;

public class EditTestParams extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_test_params, container, false);

        tvTestID = (TextView) view.findViewById(R.id.tvTestID);
        tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
        etName = (EditText) view.findViewById(R.id.etName);
        etDescr = (EditText) view.findViewById(R.id.etDescr);
        swRandom = (Switch) view.findViewById(R.id.swRandom);

        db = DataBaseCommunication.db;

        if (testID == -1)
            onDestroy();
        if (testID == -2) {
            tvTestID.setText("New test");
            mode = INSERT;
        } else {
            tvTestID.setText("Test " + testID);
            mode = EDIT;
        }

        SQLiteDatabase tempDB = db.getWritableDatabase();
        Cursor cursor;

        if (mode == INSERT) {
            thisQuestion = new Test();
        }

        final LinearLayout lyPass = (LinearLayout)(view.findViewById(R.id.lyPass));
        Switch swPass = (Switch) view.findViewById(R.id.swPass);

        if (mode == EDIT) {
            cursor = tempDB.query(DB.TESTS_TABLE, null, "_id = " + testID, null, null, null, null);
            cursor.moveToFirst();
            String pass = cursor.getString(cursor.getColumnIndex("pass"));
            Log.d("edit", cursor.getCount() + "");
            thisQuestion = new Test(cursor);
            etName.setText(thisQuestion.getShortName());
            etDescr.setText(thisQuestion.getDescription());
            swRandom.setChecked(thisQuestion.isRandom());
            cursor = tempDB.query(DB.USERS_TABLE, null, "_id = " + thisQuestion.getAuthorID(), null, null, null, null);
            if (cursor.getCount() > 0)
                tvAuthor.setText(cursor.getString(cursor.getColumnIndex("displayName")));
            else
                tvAuthor.setText("Undefined");

            if (pass == null) {
                lyPass.setVisibility(View.GONE);
                swRandom.setChecked(false);
            }
            else {
                ((TextView) view.findViewById(R.id.etPass)).setText(pass);
                lyPass.setVisibility(View.VISIBLE);
                swPass.setChecked(true);

            }
            cursor.close();
        }



        swPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lyPass.setVisibility(isChecked? View.VISIBLE: View.GONE);
            }
        });


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testID = getArguments().getInt("testID", -1);

    }

    public static EditTestParams newInstance(int testID) {
        EditTestParams editTestParams = new EditTestParams();
        Bundle args = new Bundle();
        args.putInt("testID", testID);
        editTestParams.setArguments(args);
        return editTestParams;
    }
}
