package com.dzn.dzn.application.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dzn.dzn.application.Adapters.RecyclerViewAdapterAlarms;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;

public class AlarmsActivity extends BaseActivity {
    private static final String TAG = "AlarmsActivity";

    private TextView tvAlarmsName;
    private TextView tvAlarmsReady;

    private Button btnAdd;

    private RecyclerView recyclerViewAlarms;
    private RecyclerViewAdapterAlarms recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        dataBaseHelper = DataBaseHelper.getInstance(this);

        //Initialize view elements
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
                recyclerViewAdapter = new RecyclerViewAdapterAlarms(AlarmsActivity.this, getListAlarm());
                recyclerViewAlarms.setAdapter(recyclerViewAdapter);
            }
        }.execute();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvAlarmsName = (TextView) findViewById(R.id.tvAlarmsName);
        PFHandbookProTypeFaces.THIN.apply(tvAlarmsName);

        tvAlarmsReady = (TextView) findViewById(R.id.tvAlarmsReady);
        PFHandbookProTypeFaces.THIN.apply(tvAlarmsReady);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        PFHandbookProTypeFaces.REGULAR.apply(btnAdd);

        initRecyclerView();
    }

    /**
     * Save all settings
     * @param view
     */
    public void saveAlarms(View view) {
        finish();
    }

    /**
     * Add new alarms
     * @param view
     */
    public void onAlarmAdd(View view) {
        Intent intent = new Intent(AlarmsActivity.this, NewEditActivity.class);
        startActivity(intent);
    }

    /**
     * Return list of alarms
     * @return
     */
    private ArrayList<Alarm> getListAlarm() {
        if(dataBaseHelper == null) {
            Log.i(TAG, "DBHelper is null");
            dataBaseHelper = DataBaseHelper.getInstance(this);
        }

        ArrayList<Alarm> ar = dataBaseHelper.getAlarmList();
        if(ar == null) return new ArrayList<Alarm>();

        return ar;
    }

    /**
     * Initialize recyclerView of Alarms
      */
    private void initRecyclerView() {
        recyclerViewAlarms = (RecyclerView) findViewById(R.id.recyclerAlarms);
        recyclerViewAlarms.setHasFixedSize(true);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerViewAlarms.setLayoutManager(recyclerLayoutManager);
    }

}
