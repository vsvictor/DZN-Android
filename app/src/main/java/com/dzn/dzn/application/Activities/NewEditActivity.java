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

import com.dzn.dzn.application.Adapters.RecyclerViewAdapterDrum;
import com.dzn.dzn.application.Adapters.SpinnerRepeatAdapter;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

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

    private NumberPicker npHours;
    private NumberPicker npMinutes;

    private int iHours;
    private int iMinutes;

    private Alarm edAlarm;
    private int id = -1;
    private DataBaseHelper dataBaseHelper;

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

        //tested
        //Initialize section tested
        //initSectionTested();
    }

    /**
     * Tested
     */
    private void initSectionTested() {
        /**
        tvNewEditPastTime = (TextView) findViewById(R.id.tvNewEditPastTime);
        tvNewEditPastTime.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        tvNewEditLastTime = (TextView) findViewById(R.id.tvNewEditLastTime);
        tvNewEditLastTime.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        tvNewEditCurrentTime = (TextView) findViewById(R.id.tvNewEditCurrentTime);
        tvNewEditCurrentTime.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Black.ttf"));
        tvNewEditNextTime = (TextView) findViewById(R.id.tvNewEditNextTime);
        tvNewEditNextTime.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        tvNewEditAfterTime = (TextView) findViewById(R.id.tvNewEditAfterTime);
        tvNewEditAfterTime.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
         */
    }

    /**
     * Initialize section drum
     */
    private void initSectionDrum() {
        npHours = (NumberPicker) findViewById(R.id.npHours);
        npHours.setMinValue(0);
        npHours.setMaxValue(23);
        npMinutes = (NumberPicker) findViewById(R.id.npMinutes);
        npMinutes.setMaxValue(0);
        npMinutes.setMaxValue(59);
        npHours.setValue(iHours);
        npMinutes.setValue(iMinutes);
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
            d.setHours(npHours.getValue());
            d.setMinutes(npMinutes.getValue());
            //d.UTC(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, npHours.getValue(), npMinutes.getValue(),0);
            al.setTime(d);
            al.setMelody("aaa");
            al.setRepeat(5);
            al.setVibro(true);
            al.setSound(80);
            dataBaseHelper.addAlarm(al);
        }
        else{
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
