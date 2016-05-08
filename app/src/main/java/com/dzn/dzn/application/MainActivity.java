package com.dzn.dzn.application;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dzn.dzn.application.Activities.NewEditActivity;
import com.dzn.dzn.application.Adapters.RecyclerViewAdapterMain;
import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.Utils.DataBaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView tvMainPrompt;
    private Button btnNew;

    private RecyclerView recyclerViewMain;
    private RecyclerViewAdapterMain recycleViewAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        dataBaseHelper = DataBaseHelper.getInstance(this);
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvMainPrompt = (TextView) findViewById(R.id.tvMainPrompt);
        tvMainPrompt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-ExtraThin.ttf"));

        btnNew = (Button) findViewById(R.id.btn_new);
        btnNew.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Regular.ttf"));

        //initialize recycler of alarms
        initRecyclerViewMain();
    }

    public void onNewEditAlarm(View view) {
        Intent intent = new Intent(MainActivity.this, NewEditActivity.class);
        startActivity(intent);
    }

    /**
     * Initialize recyclerView of alarms
     */
    private void initRecyclerViewMain() {
        recyclerViewMain = (RecyclerView) findViewById(R.id.recyclerViewMain);
        recyclerViewMain.setHasFixedSize(true);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerViewMain.setLayoutManager(recyclerLayoutManager);

        recycleViewAdapter = new RecyclerViewAdapterMain(getListAlarm());
        recyclerViewMain.setAdapter(recycleViewAdapter);
    }

    /**
     * This method is for tested
     * @return
     */
    private ArrayList<?> getListAlarm() {
        ArrayList<AlarmTest> list= new ArrayList<AlarmTest>();
        list.add(new AlarmTest());
        list.add(new AlarmTest("12", "00"));
        list.add(new AlarmTest("23", "30"));
        list.add(new AlarmTest("07", "45"));
        list.add(new AlarmTest("11", "20"));
        return list;
    }

}
