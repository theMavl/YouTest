package com.mavl.youtest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IntegerRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mavl.youtest.objects.Question;
import com.mavl.youtest.objects.Test;

import java.util.ArrayList;

import layout.EditTestParams;
import layout.EditTestQuestions;

public class EditTestActivity extends AppCompatActivity {

    int testID;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static AppBarLayout appBarLayout;
    public static CoordinatorLayout main_content;
    FloatingActionButton fab;
    public static Activity fa;
    EditTestParams etp;
    public static ArrayList<String> questionsToDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        fa = this;
        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        main_content = (CoordinatorLayout)findViewById(R.id.main_content);
        fab = (FloatingActionButton)findViewById(R.id.addFab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = EditTestQuestions.questions.size();
                EditTestQuestions.questions.add(new Question(testID, n));
                Intent intent = new Intent(getApplicationContext(), EditQuestionActivity.class);
                intent.putExtra("questionN", n);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        testID = intent.getIntExtra("testID", -1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.addView(new TextView(this));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("PAGE-SELECTED", position+"");
                switch (position) {
                    case 0:
                        fab.hide();
                        Log.e("PAGE-SELECTED", "HIDE FAB");
                        break;
                    case 1:
                        fab.show();
                        Log.e("PAGE-SELECTED", "SHOW FAB");
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) { }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_test, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void exitByBackKey() {
        final AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Вы действительно хотите выйти из редактора теста? Несохраненные изменения будут утеряны")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {}
                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_save) {
            // Save test
            if (saveTest()) {
                Toast.makeText(getApplicationContext(), "Тест сохранен", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Сохранение не удалось. Проверьте все заново.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean saveTest() {
        Log.i("save test", "start saving");
        if (!etp.saveValuesToObject())
            return false;
        if (EditTestQuestions.questions.size() == 0)
            return false;
        Question.syncNumbersAndPos(EditTestQuestions.questions);


        DB db = DataBaseCommunication.db;
        SQLiteDatabase tempDB = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Test test = EditTestParams.thisTest;
        cv.put("shortName", test.getShortName());
        cv.put("description", test.getDescription());
        cv.put("authorID", test.getAuthorID());
        cv.put("time", test.getTime());
        cv.put("random", test.isRandom());
        cv.put("pass", test.getPass());
        cv.put("questionsNum", EditTestQuestions.questions.size());
        long tTestID = test.getID();
        if (test.getID() == -2)
            tTestID = tempDB.insert(DB.TESTS_TABLE, null, cv);
        else
            tempDB.update(DB.TESTS_TABLE, cv, "_id = "+test.getID(), null);
        cv.clear();
        for (Question x: EditTestQuestions.questions) {
            cv.put("testID", tTestID);
            cv.put("questionText", x.getQuestionText());
            cv.put("time", x.getTime());
            cv.put("correctOptions", x.getCorrects());
            cv.put("cost", x.getCost());
            cv.put("number", x.getNumber());
            for (int i = 0; i < x.options.size(); i++) {
                cv.put("option"+(i+1), x.options.get(i).getText());
            }
            if (x.getID() == -2)
                tempDB.insert(DB.QUESTIONS_TABLE, null, cv);
            else
                tempDB.update(DB.QUESTIONS_TABLE, cv, "_id = "+x.getID(), null);
            cv.clear();
        }
        tempDB.delete(DB.QUESTIONS_TABLE, "_id = ?", questionsToDelete.toArray(new String[questionsToDelete.size()]));
        /*Cursor c = tempDB.query(DB.RESULTS_TABLE, new String[]{"_id"}, "testID = "+tTestID, null, null, null, null);
        if (c.getCount() > 0) {
            String[] ttt = new String[c.getCount()];
            int j = 0;
            c.moveToFirst();
            do {
                ttt[j] = c.getInt(0) + "";
            } while (c.moveToNext());
            tempDB.delete(DB.BY_QUESTION_RESULTS_TABLE, "resultID = ?", ttt);
            tempDB.delete(DB.RESULTS_TABLE, "_id = ?", ttt);
        }
        c.close();*/
        tempDB.delete(DB.RESULTS_TABLE, "testID = "+tTestID, null);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    etp = EditTestParams.newInstance(testID);
                    return etp;
                case 1:

                    return EditTestQuestions.newInstance(testID);
                default:
                    return new EditTestQuestions();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.test_params_tab);
                case 1:
                    return getResources().getString(R.string.questions_tab);
            }
            return null;
        }
    }
}
