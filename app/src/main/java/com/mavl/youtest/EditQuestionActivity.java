package com.mavl.youtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mavl.youtest.listAdapters.OptionsListAdapter;
import com.mavl.youtest.objects.Option;
import com.mavl.youtest.objects.Question;

import java.util.ArrayList;
import java.util.Collections;

import layout.EditTestQuestions;

public class EditQuestionActivity extends AppCompatActivity {

    RecyclerView options;
    ArrayList<Option> arrOptions;
    private RecyclerView.LayoutManager qlManager;
    OptionsListAdapter adapter;
    Intent intent;
    int questionN;
    Question thisQuestion;
    EditText etQuestionText;
    Spinner questionType;
    EditText questionCost;
    FloatingActionButton fab;
    boolean needToRefreshOptions = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etQuestionText = (EditText) findViewById(R.id.etQuestionText);
        questionType = (Spinner) findViewById(R.id.spQuestionType);
        questionCost = (EditText) findViewById(R.id.etQuestionCost);
        options = (RecyclerView) findViewById(R.id.rvOptions);
        fab = (FloatingActionButton) findViewById(R.id.addOptionFab);
        options.getRecycledViewPool().setMaxRecycledViews(0, 12);
        questionType.setEnabled(false);
        intent = getIntent();
        questionN = intent.getIntExtra("questionN", -1);
        if (questionN == -1)
            finish();

        thisQuestion = EditTestQuestions.questions.get(questionN);

        arrOptions = (ArrayList) (thisQuestion.options.clone());
        Log.d("CLONE", thisQuestion.options+" "+arrOptions);
        //while (arrOptions.size() < 10)
        //    arrOptions.add(new Option());

        if (arrOptions.size() > 9)
            fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrOptions.size() > 9) {
                    fab.hide();
                    return;
                }
                arrOptions.add(new Option("Новый вариант"));
                adapter.notifyItemInserted(arrOptions.size());
                if (arrOptions.size() > 9)
                    fab.hide();
            }
        });
        qlManager = new LinearLayoutManager(this);
        adapter = new OptionsListAdapter(arrOptions, this);
        options.setLayoutManager(qlManager);
        options.setHasFixedSize(true);
        options.setAdapter(adapter);
        etQuestionText.setText(thisQuestion.getQuestionText());
        questionCost.setText(thisQuestion.getCost()+"");

        options.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (needToRefreshOptions) {
                        Log.i("KEY", "REFRESH THIS STUFF");
                        adapter.notifyDataSetChanged();
                        needToRefreshOptions = false;
                    }
                }
                return false;
            }
        });


        ItemTouchHelper mIt = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                Collections.swap(arrOptions, fromPos, toPos);
                adapter.notifyItemMoved(fromPos, toPos);
                needToRefreshOptions = true;
                //adapter.notifyDataSetChanged();
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                Log.d("swipe", "removed "+pos+" "+arrOptions.get(pos).getText());
                final Option justCopy = arrOptions.get(pos);
                arrOptions.remove(pos);
                adapter.notifyItemRemoved(pos);
                adapter.notifyDataSetChanged();
                if (!fab.isShown())
                    fab.show();
                Snackbar snackbar = Snackbar
                        .make(toolbar, "Вариант удален", Snackbar.LENGTH_LONG);
                snackbar.setAction("Вернуть", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (arrOptions.size() > 9)
                        {
                            Toast.makeText(getApplicationContext(), "Свободного места нет!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("swipe", "restored "+pos+" "+justCopy.getText());
                        arrOptions.add(pos, justCopy);
                        adapter.notifyItemInserted(pos);
                        if (arrOptions.size() > 9)
                            fab.hide();
                    }
                });
                snackbar.show();
            }
        });
        mIt.attachToRecyclerView(options);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (needToRefreshOptions) {
                adapter.notifyDataSetChanged();
                needToRefreshOptions = false;
            }
        }
        return false;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            exitByBackKey();
            return true;
        }

        if (id == R.id.action_save_question) {
            // Save question
            if(saveQuestion()) {
                EditTestQuestions.qlAdapter.notifyDataSetChanged();
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean saveQuestion() {
        String text = etQuestionText.getText().toString();
        boolean foundCorrect = false;
        int cost;
        if (text.equals("")) {
            Toast.makeText(this, "Нет текста вопроса", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            cost = Integer.parseInt(questionCost.getText().toString());
        }
        catch (Exception e) {
            Toast.makeText(this, "Неправильное значение цены вопроса", Toast.LENGTH_LONG).show();
            return false;
        }
        View view;
        String tmpOptionText;
        EditText tmpOption;

        for (int i = 0; i < arrOptions.size(); i++) {
            if (arrOptions.get(i).getText() == null) {// || (arrOptions.get(i).getText().length() == 0))
                arrOptions.remove(i);
                continue;
            }
            if (arrOptions.get(i).isCorrect())
                foundCorrect = true;
        }
        if (arrOptions.size() < 2) {
            Toast.makeText(this, "Слишком мало вариантов ответа", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!foundCorrect) {
            Toast.makeText(this, "Не найдены правильные варианты", Toast.LENGTH_LONG).show();
            return false;
        }


        thisQuestion.options = arrOptions;
        thisQuestion.setQuestionText(text);
        thisQuestion.setCost(cost);
        EditTestQuestions.qlAdapter.notifyDataSetChanged();
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
                .setMessage("Вы действительно хотите выйти из редактора вопроса? Несохраненные изменения будут утеряны")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {}
                }).show();
    }
}

