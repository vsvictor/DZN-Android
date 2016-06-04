package com.dzn.dzn.application;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dzn.dzn.application.Activities.BaseActivity;
import com.dzn.dzn.application.Activities.CreateSelfieActivity;
import com.dzn.dzn.application.Activities.EditListAlarmsActivity;
import com.dzn.dzn.application.Activities.NewEditActivity;
import com.dzn.dzn.application.Activities.SettingsActivity;
import com.dzn.dzn.application.Activities.StartActivity;
import com.dzn.dzn.application.Adapters.RecyclerViewAdapterMain;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private final static String CLASS_LABEL = "MainActivity";
    private static final int START_ACTIVITY = 1;

    private PowerManager.WakeLock mWakeLock;

    private TextView tvMainPrompt;
    private Button btnNew;

    private RecyclerView recyclerViewMain;
    private RecyclerViewAdapterMain recycleViewAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private DataBaseHelper dataBaseHelper;

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, CLASS_LABEL);
        mWakeLock.acquire();

        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km .newKeyguardLock(CLASS_LABEL);
        kl.disableKeyguard();

        setContentView(R.layout.activity_main);

        dataBaseHelper = DataBaseHelper.getInstance(this);

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //Initialize view elements
        initView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        settings.load();
        new AsyncTask<Void, Void, Void>(){
            private ArrayList<Alarm> list = new ArrayList<Alarm>();
            @Override
            protected Void doInBackground(Void... params) {
                list = getListAlarm();
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                clearAllAlarms();
                if(list.isEmpty()){
                    runFirstActivity();
                }
                recycleViewAdapter = new RecyclerViewAdapterMain(list);
                recyclerViewMain.setAdapter(recycleViewAdapter);
                int counter = 1;
                for(Alarm alarm : list){
                    Date d = alarm.getDate();
                    Date today = Calendar.getInstance().getTime();
                    today.setHours(d.getHours());
                    today.setMinutes(d.getMinutes());
                    today.setSeconds(0);
                    if((today.getTime()>System.currentTimeMillis())&& alarm.isTurnOn()) {
                        Intent intent = new Intent(MainActivity.this, CreateSelfieActivity.class);
                        intent.putExtra("id", alarm.getID());
                        intent.putExtra("counter", counter);
                        intent.putExtra("time", today.getTime());
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, counter, intent, PendingIntent.FLAG_ONE_SHOT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, today.getTime(), pendingIntent);
                        counter++;
                    }
                }
            }
        }.execute();
    }

    private void clearAllAlarms() {
        ArrayList<Alarm> allList = dataBaseHelper.getAlarmList();
        int counter = 1;
        for(Alarm alarm : allList){
            Date d = alarm.getDate();
            Date today = Calendar.getInstance().getTime();
            today.setHours(d.getHours());
            today.setMinutes(d.getMinutes());
            today.setSeconds(0);
//            if((today.getTime()>System.currentTimeMillis())&& alarm.isTurnOn()) {
                Intent intent = new Intent(MainActivity.this, CreateSelfieActivity.class);
                intent.putExtra("counter", counter);
                intent.putExtra("time", today.getTime());
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, counter, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(pendingIntent);
                counter++;
///            }
        }
    }

    /**
     * Initialize settings and locale
     */

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
        intent.putExtra("sender", 0);
        startActivity(intent);
        finish();
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

        //ArrayList<Alarm> ar = dataBaseHelper.getAlarmList();
        ArrayList<Alarm> ar = dataBaseHelper.getAlarmList(true);
        if(ar == null) return new ArrayList<Alarm>();

        return ar;
    }

    public void onCreateSelfie(View view) {
        Intent intent = new Intent(MainActivity.this, CreateSelfieActivity.class);
        startActivity(intent);
    }

    /**
     * Check list of alarms is empty and run StartActivity
     */
    private void runFirstActivity() {
        if (getListAlarm().size() == 0) {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivityForResult(intent, START_ACTIVITY);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == START_ACTIVITY && resultCode == RESULT_CANCELED){
            finish();
        }
    }
}
