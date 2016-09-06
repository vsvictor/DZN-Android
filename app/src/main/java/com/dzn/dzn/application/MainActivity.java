package com.dzn.dzn.application;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzn.dzn.application.Activities.BaseActivity;
import com.dzn.dzn.application.Activities.CreateSelfieActivity;
import com.dzn.dzn.application.Activities.EditListAlarmsActivity;
import com.dzn.dzn.application.Activities.NewEditActivity;
import com.dzn.dzn.application.Activities.SettingsActivity;
import com.dzn.dzn.application.Activities.StartActivity;
import com.dzn.dzn.application.Adapters.RecyclerViewAdapterMain;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private ImageView ivCloseReclama;
    private DataBaseHelper dataBaseHelper;

    private AlarmManager alarmManager;
    private int counter = 0;
    private RelativeLayout rlReclama;
    private LinearLayout llAlarmData;
    private RelativeLayout llMainAlarm;
    private RelativeLayout rlBigReclama;
    private ImageView ivDznTitle;

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

        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        requestUserPermission.verifyStoragePermissions();


        llMainAlarm = (RelativeLayout)findViewById(R.id.llMainAlagm);

        rlReclama = (RelativeLayout) findViewById(R.id.rlReclama);
        rlReclama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlBigReclama.setVisibility(View.INVISIBLE);
                MainApplication.setReclama(true);

                String url = "http://big-funny.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        rlBigReclama = (RelativeLayout) findViewById(R.id.rlBigReclama);
        rlBigReclama.setVisibility(View.VISIBLE);
        ivDznTitle = (ImageView) findViewById(R.id.ivDznTitle);
        ivDznTitle.setVisibility(View.INVISIBLE);
        llAlarmData = (LinearLayout) findViewById(R.id.llAlarmData);
        llAlarmData.setVisibility(View.VISIBLE);
        ivCloseReclama = (ImageView) findViewById(R.id.ivCloseReclama);
        ivCloseReclama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                rlBigReclama.setVisibility(View.INVISIBLE);
                MainApplication.setReclama(true);
            }
        });
        dataBaseHelper = DataBaseHelper.getInstance(this);

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //Initialize view elements
        initView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        counter = 0;
        rlReclama.setVisibility(MainApplication.isReclama()?View.INVISIBLE:View.VISIBLE);
        rlReclama.setBackground(getResources().getDrawable(R.drawable.border));
        llAlarmData.setVisibility(View.VISIBLE);
        ivDznTitle.setVisibility(View.INVISIBLE);
        ivCloseReclama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                rlBigReclama.setVisibility(View.INVISIBLE);
                MainApplication.setReclama(true);
            }
        });

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
                //recycleViewAdapter = new RecyclerViewAdapterMain(list);
                recycleViewAdapter = new RecyclerViewAdapterMain(MainActivity.this, list, dataBaseHelper);
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
                    recycleViewAdapter.setOnCheckEmpty(new RecyclerViewAdapterMain.OnCheckEmpty() {
                        @Override
                        public void isEmpty(boolean isEmpty) {
                            if(isEmpty) startActivity(new Intent(MainActivity.this, StartActivity.class));
                        }
                    });
                }
            }
        }.execute();
    }
    @Override
    public void onBackPressed(){
        counter++;
        if(counter == 1){
            llMainAlarm.setBackgroundColor(Color.argb(224,255,0,0));
            //rlReclama.setBackgroundColor(Color.argb(255,208,136,136));
            rlReclama.setBackground(getResources().getDrawable(R.drawable.border));
            ivCloseReclama.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            rlBigReclama.setVisibility(View.VISIBLE);
            rlReclama.setVisibility(View.VISIBLE);
            llAlarmData.setVisibility(View.INVISIBLE);
            ivDznTitle.setVisibility(View.VISIBLE);
        }
        else if (counter > 1) {MainApplication.setReclama(false);super.onBackPressed();}
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
     * Run NewEdit activity
     * @param view
     */
    public void onNewEditActivity(View view) {
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
    }

    /**
     * Return list of Alarm
     *
     * @return
     */
    private ArrayList<Alarm> getListAlarm() {
        if(dataBaseHelper == null) {
            Log.i(TAG, "DNHelper is null");
            dataBaseHelper = DataBaseHelper.getInstance(this);
        }

        ArrayList<Alarm> ar = dataBaseHelper.getAlarmList();
        //ArrayList<Alarm> ar = dataBaseHelper.getAlarmList(true);
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

    @Override
    protected void onPause() {
        super.onPause();
        dataBaseHelper.close();
    }
}
