package com.dzn.dzn.application.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.dzn.dzn.application.MainActivity;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.DateTimeOperator;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.Calendar;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "StartActivity";

    private TextView tvStartSet;
    private NumberPicker npStartHours;
    private NumberPicker npStartMinutes;

    private DataBaseHelper dataBaseHelper;

    private int iHour;
    private int iMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        dataBaseHelper = DataBaseHelper.getInstance(getParent());

        initView();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvStartSet = (TextView) findViewById(R.id.tvStartSet);
        PFHandbookProTypeFaces.EXTRA_THIN.apply(tvStartSet);


        npStartHours = (NumberPicker) findViewById(R.id.npStartHours);
        npStartHours.setMinValue(0);
        npStartHours.setMaxValue(23);

        npStartMinutes = (NumberPicker) findViewById(R.id.npStartMinutes);
        npStartMinutes.setMinValue(0);
        npStartMinutes.setMaxValue(59);

        Calendar calendar = Calendar.getInstance();
        //iHour = calendar.get(Calendar.HOUR_OF_DAY);
        //iMinute = calendar.get(Calendar.MINUTE);
        //Log.d(TAG, "Time: " + iHour + ":" + iMinute);

        //Log.d(TAG, "Time: " + DateTimeOperator.dateToTimeString(calendar.getTime()));

        npStartHours.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        npStartMinutes.setValue(calendar.get(Calendar.MINUTE));
    }

    /**
     * Save Alarm and start Main Activity
     * @param view
     */
    public void onStart(View view) {
        Alarm alarm = new Alarm();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, npStartHours.getValue());
        calendar.set(Calendar.MINUTE, npStartMinutes.getValue());
        alarm.setTime(calendar.getTime());
        alarm.setDefault();

        dataBaseHelper.addAlarm(alarm);

        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Run Alarms activity
     * @param view
     */
    public void onList(View view) {
        Intent intent = new Intent(StartActivity.this, EditListAlarmsActivity.class);
        startActivity(intent);
    }

    /**
     * Run Settings activity
     * @param view
     */
    public void onSettings(View view) {
        Intent intent = new Intent(StartActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
