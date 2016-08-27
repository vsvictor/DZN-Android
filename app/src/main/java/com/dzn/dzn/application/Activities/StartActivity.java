package com.dzn.dzn.application.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzn.dzn.application.MainActivity;
import com.dzn.dzn.application.MainApplication;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;
import com.dzn.dzn.application.Widget.WheelView;
import com.dzn.dzn.application.Widget.adapters.NumericWheelAdapter;

import java.util.Calendar;
import java.util.Date;

public class StartActivity extends BaseActivity {
    private static final String TAG = "StartActivity";
    private MainApplication app;
    private TextView tvStartSet;
    private WheelView whHours;
    private WheelView whMinutes;
    private NumericWheelAdapter hAdapter;
    private NumericWheelAdapter mAdapter;
    private DataBaseHelper dataBaseHelper;
    private RelativeLayout llMainAlarm;
    private RelativeLayout rlReclama;
    private ImageView ivCloseReclama;
    private int counter = 0;
    private RelativeLayout rlBigReclama;
    private LinearLayout llAlarmData;
    private ImageView ivDznTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        app = (MainApplication) getApplication();
        dataBaseHelper = DataBaseHelper.getInstance(getParent());



        // Initialize view elements
        initView();

        llMainAlarm = (RelativeLayout)findViewById(R.id.llMainAlagm);
        rlReclama = (RelativeLayout) findViewById(R.id.rlReclama);
        ivDznTitle = (ImageView) findViewById(R.id.ivDznTitle);
        ivDznTitle.setVisibility(View.INVISIBLE);
        rlReclama.setBackground(getResources().getDrawable(R.drawable.border));
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
        llAlarmData = (LinearLayout) findViewById(R.id.llAlarmData);
        ivCloseReclama = (ImageView) findViewById(R.id.ivCloseReclama);
        ivCloseReclama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlBigReclama.setVisibility(View.INVISIBLE);
                MainApplication.setReclama(true);
                if (dataBaseHelper.getAlarmList(true).size() > 0) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        rlBigReclama.setVisibility(MainApplication.isReclama()?View.INVISIBLE:View.VISIBLE);
        ivDznTitle.setVisibility(MainApplication.isReclama()?View.INVISIBLE:View.VISIBLE);
        llAlarmData.setVisibility(View.VISIBLE);
        ivCloseReclama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlBigReclama.setVisibility(View.INVISIBLE);
                MainApplication.setReclama(true);
                if (dataBaseHelper.getAlarmList(true).size() > 0) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed(){

        counter++;
        if(counter == 1){
            llMainAlarm.setBackgroundColor(Color.argb(224,255,0,0));
            rlReclama.setBackground(getResources().getDrawable(R.drawable.border));
            rlBigReclama.setVisibility(View.VISIBLE);
            rlReclama.setVisibility(View.VISIBLE);
            llAlarmData.setVisibility(View.INVISIBLE);
            ivDznTitle.setVisibility(View.VISIBLE);
            ivCloseReclama.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
        else if (counter > 1) super.onBackPressed();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        Date d = new Date();
        tvStartSet = (TextView) findViewById(R.id.tvStartSet);
        PFHandbookProTypeFaces.EXTRA_THIN.apply(tvStartSet);
        whHours = (WheelView) findViewById(R.id.npHours);
        hAdapter = new NumericWheelAdapter(this, 0, 23, "%02d");
        hAdapter.setItemResource(R.layout.wheel_item_time);
        hAdapter.setItemTextResource(R.id.tvNumber);
        whHours.setViewAdapter(hAdapter);
        whHours.setCyclic(true);
        whHours.setVisibleItems(5);
        whHours.setCurrentItem(d.getHours());
        whMinutes = (WheelView) findViewById(R.id.npMinutes);
        mAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
        mAdapter.setItemResource(R.layout.wheel_item_time);
        mAdapter.setItemTextResource(R.id.tvNumber);
        whMinutes.setViewAdapter(mAdapter);
        whMinutes.setCyclic(true);
        whMinutes.setVisibleItems(5);
        whMinutes.setCurrentItem(d.getMinutes());
    }

    /**
     * Save Alarm and start Main Activity
     *
     * @param view
     */
    public void onStart(View view) {
        Alarm alarm = new Alarm();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, whHours.getCurrentItem());
        calendar.set(Calendar.MINUTE, whMinutes.getCurrentItem());
        alarm.setTime(calendar.getTime());
        alarm.setDefault();
        alarm.setSocial(settings);

        dataBaseHelper.addAlarm(alarm);

        setResult(RESULT_OK);
        Log.d(TAG, "Alarm: " + alarm.toString());
        finish();
    }

    /**
     * Run Alarms activity
     *
     * @param view
     */
    public void onList(View view) {
        Intent intent = new Intent(StartActivity.this, EditListAlarmsActivity.class);
        startActivity(intent);
    }

    /**
     * Run Settings activity
     *
     * @param view
     */
    public void onSettings(View view) {
        Intent intent = new Intent(StartActivity.this, SettingsActivity.class);
        intent.putExtra("sender", 1);
        startActivity(intent);
        finish();
    }
}
