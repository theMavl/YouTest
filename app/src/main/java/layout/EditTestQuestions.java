package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mavl.youtest.R;

public class EditTestQuestions extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_test_questions, container, false);
        return view;
    }

    public static EditTestQuestions newInstance(int testID) {
        EditTestQuestions editTestQuestions = new EditTestQuestions();
        //Bundle args = new Bundle();
        //args.putInt("testID", testID);
        //editTestQuestions.setArguments(args);
        return editTestQuestions;
    }

}
