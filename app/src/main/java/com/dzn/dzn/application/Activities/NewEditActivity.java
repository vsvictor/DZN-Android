package com.dzn.dzn.application.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dzn.dzn.application.Adapters.RecyclerViewAdapterDrum;
import com.dzn.dzn.application.Adapters.SpinnerRepeatAdapter;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;
import com.dzn.dzn.application.Widget.WheelRecycle;
import com.dzn.dzn.application.Widget.WheelView;
import com.dzn.dzn.application.Widget.adapters.NumericWheelAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewEditActivity extends AppCompatActivity {
    private static final String TAG = "NewEditActivity";

    private TextView tvNewEditBack;
    private TextView tvNewEditTune;
    private TextView tvNewEditReady;

    private TextView tvNewEditSetting;

    //Section Drum
    private RecyclerView recyclerViewDrum;
    private LinearLayoutManager recyclerLayoutManager;
    private RecyclerViewAdapterDrum recyclerViewAdapterDrum;

    //Section Repeat
    private TextView tvNewEditRepeat;
    private Spinner spinnerNewEditRepeat;
    private LinearLayout linearNewEditWeek;

    //Section Music
    private TextView tvNewEditMusic;
    private Spinner spinnerNewEditMusic;

    //Section Interval
    private TextView tvNewEditInterval;
    private Spinner spinnerNewEditInterval;

    //Section Social Network
    private TextView tvNewEditSocialNetwork;
    private Spinner spinnerNewEditSocialNetwork;

    private WheelView npHours;
    private WheelView npMinutes;

    private int iHours;
    private int iMinutes;

    private Alarm edAlarm;
    private int id = -1;
    private DataBaseHelper dataBaseHelper;
    private NumericWheelAdapter hAdapter;
    private NumericWheelAdapter mAdapter;
    private ToggleButton day1;
    private ToggleButton day2;
    private ToggleButton day3;
    private ToggleButton day4;
    private ToggleButton day5;
    private ToggleButton day6;
    private ToggleButton day7;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit);
        dataBaseHelper = DataBaseHelper.getInstance(getParent());
        Bundle b = getIntent().getExtras();
        if(b != null){
            id = b.getInt("idAlarm", -1);
            edAlarm = dataBaseHelper.getAlarm(id);
            if(edAlarm != null) {
                iHours = edAlarm.getDate().getHours();
                iMinutes = edAlarm.getDate().getMinutes();
            }
            else{
                Date d = new Date();
                iHours = d.getHours();
                iMinutes = d.getMinutes();
            }
        }
        else{
            Date d = new Date();
            iHours = d.getHours();
            iMinutes = d.getMinutes();
        }

        settings = Settings.getInstance(this);
        settings.load();

        initView();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvNewEditBack = (TextView) findViewById(R.id.tvNewEditBack);
        PFHandbookProTypeFaces.THIN.apply(tvNewEditBack);

        tvNewEditTune = (TextView) findViewById(R.id.tvNewEditTune);
        PFHandbookProTypeFaces.THIN.apply(tvNewEditTune);

        tvNewEditReady = (TextView) findViewById(R.id.tvNewEditReady);
        PFHandbookProTypeFaces.THIN.apply(tvNewEditReady);

        tvNewEditSetting = (TextView) findViewById(R.id.tvNewEditSetting);
        PFHandbookProTypeFaces.THIN.apply(tvNewEditSetting);

        //Initialize section Drum
        initSectionDrum();

        //Initialize section Repeat
        initSectionRepeat();

        //Initialize section Music
        initSectionMusic();

        //Initialize section Interval
        initSectionInterval();

        //Initialize section Social Network
        initSectionSocialNetwork();

    }

    /**
     * Initialize section drum
     */
    private void initSectionDrum() {
        hAdapter = new NumericWheelAdapter(this, 0,23, "%02d");
        hAdapter.setItemResource(R.layout.wheel_item_time);
        hAdapter.setItemTextResource(R.id.tvNumber);
        npHours = (WheelView) findViewById(R.id.npHours);
        npHours.setViewAdapter(hAdapter);
        npHours.setCyclic(true);
        npHours.setVisibleItems(5);

        mAdapter = new NumericWheelAdapter(this, 0,59, "%02d");
        mAdapter.setItemResource(R.layout.wheel_item_time);
        mAdapter.setItemTextResource(R.id.tvNumber);
        npMinutes = (WheelView) findViewById(R.id.npMinutes);
        npMinutes.setViewAdapter(mAdapter);
        npMinutes.setCyclic(true);
        npMinutes.setVisibleItems(5);
    }


    /**
     * Initialize section Repeat
     */
    private void initSectionRepeat() {
        tvNewEditRepeat = (TextView) findViewById(R.id.tvNewEditRepeat);
        tvNewEditRepeat.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Regular.ttf"));
        spinnerNewEditRepeat = (Spinner) findViewById(R.id.spinnerNewEditRepeat);

        SpinnerRepeatAdapter spinnerRepeatAdapter = new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner_repeat,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_repeat))
        );
        spinnerRepeatAdapter.setDropDownViewResource(R.layout.new_edit_spinner_drop_repeat);
        spinnerNewEditRepeat.setAdapter(spinnerRepeatAdapter);

        day1 = (ToggleButton) findViewById(R.id.toggleNewEditSunday);
        day2 = (ToggleButton) findViewById(R.id.toggleNewEditSunday);
        day3 = (ToggleButton) findViewById(R.id.toggleNewEditSunday);
        day4 = (ToggleButton) findViewById(R.id.toggleNewEditSunday);
        day5 = (ToggleButton) findViewById(R.id.toggleNewEditSunday);
        day6 = (ToggleButton) findViewById(R.id.toggleNewEditSunday);
        day7 = (ToggleButton) findViewById(R.id.toggleNewEditSunday);


        if(id > 0) {
            day1.setChecked(edAlarm.isDayOn(0));
            day2.setChecked(edAlarm.isDayOn(1));
            day3.setChecked(edAlarm.isDayOn(2));
            day4.setChecked(edAlarm.isDayOn(3));
            day5.setChecked(edAlarm.isDayOn(4));
            day6.setChecked(edAlarm.isDayOn(5));
            day7.setChecked(edAlarm.isDayOn(6));
        }
        if(edAlarm.isOne())spinnerNewEditRepeat.setSelection(0);
        else if(edAlarm.isAllDays()) spinnerNewEditRepeat.setSelection(1);
        else if(edAlarm.isWokedDays()) spinnerNewEditRepeat.setSelection(2);
        else if(edAlarm.isWeekEnd()) spinnerNewEditRepeat.setSelection(3);
        else spinnerNewEditRepeat.setSelection(4);



        linearNewEditWeek = (LinearLayout) findViewById(R.id.linearNewEditWeek);
        spinnerNewEditRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    linearNewEditWeek.setVisibility(View.VISIBLE);
                } else {
                    linearNewEditWeek.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Initialize section Music
     */
    private void initSectionMusic() {
        tvNewEditMusic = (TextView) findViewById(R.id.tvNewEditMusic);
        PFHandbookProTypeFaces.THIN.apply(tvNewEditMusic);
        spinnerNewEditMusic = (Spinner) findViewById(R.id.spinnerNewEditMusic);

        SpinnerRepeatAdapter adapter = new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_settings)));
        adapter.setDropDownViewResource(R.layout.new_edit_spinner_drop);

        spinnerNewEditMusic.setAdapter(adapter);
    }

    /**
     * Initialize section Interval
     */
    private void initSectionInterval() {
        tvNewEditInterval = (TextView) findViewById(R.id.tvNewEditInterval);
        PFHandbookProTypeFaces.THIN.apply(tvNewEditInterval);
        spinnerNewEditInterval = (Spinner) findViewById(R.id.spinnerNewEditInterval);

        SpinnerRepeatAdapter adapter = new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_settings)));
        adapter.setDropDownViewResource(R.layout.new_edit_spinner_drop);

        spinnerNewEditInterval.setAdapter(adapter);
    }

    /**
     * Initialize section Social Network
     */
    private void initSectionSocialNetwork() {
        tvNewEditSocialNetwork = (TextView) findViewById(R.id.tvNewEditSocialNetwork);
        PFHandbookProTypeFaces.THIN.apply(tvNewEditSocialNetwork);
        spinnerNewEditSocialNetwork = (Spinner) findViewById(R.id.spinnerNewEditSocialNetwork);

        SpinnerRepeatAdapter adapter = new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_settings)));
        adapter.setDropDownViewResource(R.layout.new_edit_spinner_drop);

        spinnerNewEditSocialNetwork.setAdapter(adapter);
    }

    /**
     * Save data to database
     */
    public void saveData(View view) {
        if(id == -1) {
            Alarm al = new Alarm();
            Date d = new Date();
            d.setHours(npHours.getCurrentItem());
            d.setMinutes(npMinutes.getCurrentItem());
            //d.UTC(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, npHours.getValue(), npMinutes.getValue(),0);
            al.setTime(d);
            al.setMelody("aaa");
            al.setRepeat(5);
            al.setVibro(true);
            al.setSound(80);
            al.setTurnOn(true);
            al.setDay(0,day1.isChecked());
            al.setDay(1,day2.isChecked());
            al.setDay(2,day3.isChecked());
            al.setDay(3,day4.isChecked());
            al.setDay(4,day5.isChecked());
            al.setDay(5,day6.isChecked());
            al.setDay(6,day7.isChecked());

            dataBaseHelper.addAlarm(al);
        }
        else{
            edAlarm.getDate().setHours(npHours.getCurrentItem());
            edAlarm.getDate().setMinutes(npMinutes.getCurrentItem());
            edAlarm.setDay(0,day1.isChecked());
            edAlarm.setDay(1,day2.isChecked());
            edAlarm.setDay(2,day3.isChecked());
            edAlarm.setDay(3,day4.isChecked());
            edAlarm.setDay(4,day5.isChecked());
            edAlarm.setDay(5,day6.isChecked());
            edAlarm.setDay(6,day7.isChecked());
            dataBaseHelper.updateAlarm(edAlarm);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Cancel - close activity
     * @param view
     */
    public void onCancel(View view) {
        finish();
    }
}
