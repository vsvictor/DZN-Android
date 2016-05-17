package com.dzn.dzn.application;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dzn.dzn.application.Activities.AlarmsActivity;
import com.dzn.dzn.application.Activities.CreateSelfieActivity;
import com.dzn.dzn.application.Activities.EditListAlarmsActivity;
import com.dzn.dzn.application.Activities.NewEditActivity;
import com.dzn.dzn.application.Activities.SettingsActivity;
import com.dzn.dzn.application.Activities.StartActivity;
import com.dzn.dzn.application.Adapters.RecyclerViewAdapterMain;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

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

        dataBaseHelper = DataBaseHelper.getInstance(this);

        if (getListAlarm().size() == 0) {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);

        }

        initView();


    }

    @Override
    protected void onResume(){
        super.onResume();
        new AsyncTask<Void, Void, Void>(){
            private ArrayList<Alarm> list = new ArrayList<Alarm>();
            @Override
            protected Void doInBackground(Void... params) {
                list = getListAlarm();
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                recycleViewAdapter = new RecyclerViewAdapterMain(getListAlarm());
                recyclerViewMain.setAdapter(recycleViewAdapter);
            }
        }.execute();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvMainPrompt = (TextView) findViewById(R.id.tvMainPrompt);
        PFHandbookProTypeFaces.EXTRA_THIN.apply(tvMainPrompt);

        btnNew = (Button) findViewById(R.id.btn_new);
        PFHandbookProTypeFaces.REGULAR.apply(btnNew);

        //initialize recycler of alarms
        initRecyclerViewMain();
    }

    /**
     * Run New-Edit alarm activity
     * @param view
     */
    public void onNewEditAlarm(View view) {
        Intent intent = new Intent(MainActivity.this, NewEditActivity.class);
        startActivity(intent);
    }

    /**
     * Run Settings activity
     * @param view
     */
    public void onSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Run Alarms activity
     * @param view
     */
    public void onList(View view) {
        Intent intent = new Intent(MainActivity.this, EditListAlarmsActivity.class);
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
    }

    /**
     * This method is for tested
     * @return
     */
    private ArrayList<Alarm> getListAlarm() {
        if(dataBaseHelper == null) {
            Log.i(TAG, "DNHelper is null");
            dataBaseHelper = DataBaseHelper.getInstance(this);
        }

        ArrayList<Alarm> ar = dataBaseHelper.getAlarmList();
        if(ar == null) return new ArrayList<Alarm>();

        return ar;
    }

    public void onCreateSelfie(View view) {
        Intent intent = new Intent(MainActivity.this, CreateSelfieActivity.class);
        startActivity(intent);
    }

}
