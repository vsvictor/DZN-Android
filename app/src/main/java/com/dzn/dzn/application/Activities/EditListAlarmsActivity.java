package com.dzn.dzn.application.Activities;

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

import com.dzn.dzn.application.Adapters.RecyclerViewAdapterAlarms;
import com.dzn.dzn.application.Adapters.RecyclerViewAdapterMain;
import com.dzn.dzn.application.Adapters.RecyclerViewAdapterOnOff;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;

public class EditListAlarmsActivity extends BaseActivity {
    private static final String TAG = "EditListAlarmsActivity";
    private TextView tvAlarmsName;
    private TextView tvAlarmsEdit;
    private Button btnAdd;
    private RecyclerView recyclerViewAlarms;
    private LinearLayoutManager recyclerLayoutManager;
    private RecyclerViewAdapterOnOff recyclerViewAdapter;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_on_off);
        dataBaseHelper = DataBaseHelper.getInstance(this);
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
                recyclerViewAdapter = new RecyclerViewAdapterOnOff(getListAlarm(), dataBaseHelper);
                recyclerViewAlarms.setAdapter(recyclerViewAdapter);
            }
        }.execute();
    }


    private void initView() {
        tvAlarmsName = (TextView) findViewById(R.id.tvAlarmsName);
        PFHandbookProTypeFaces.THIN.apply(tvAlarmsName);

        tvAlarmsEdit = (TextView) findViewById(R.id.tvAlarmsReady);
        PFHandbookProTypeFaces.THIN.apply(tvAlarmsEdit);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        PFHandbookProTypeFaces.REGULAR.apply(btnAdd);

        initRecyclerView();
    }
    private void initRecyclerView() {
        recyclerViewAlarms = (RecyclerView) findViewById(R.id.recyclerAlarms);
        recyclerViewAlarms.setHasFixedSize(true);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerViewAlarms.setLayoutManager(recyclerLayoutManager);
    }
    private ArrayList<Alarm> getListAlarm() {
        if(dataBaseHelper == null) {
            Log.i(TAG, "DBHelper is null");
            dataBaseHelper = DataBaseHelper.getInstance(this);
        }

        ArrayList<Alarm> ar = dataBaseHelper.getAlarmList();
        if(ar == null) return new ArrayList<Alarm>();

        return ar;
    }

    public void editAlarm(View view) {
        Intent intent = new Intent(EditListAlarmsActivity.this, AlarmsActivity.class);
        startActivity(intent);
    }
    public void onAlarmAdd(View view) {
        Intent intent = new Intent(EditListAlarmsActivity.this, NewEditActivity.class);
        startActivity(intent);
    }
    public void onClose(View view) {
        finish();
    }


}
