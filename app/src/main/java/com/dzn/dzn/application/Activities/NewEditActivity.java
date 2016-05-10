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

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit);

        dataBaseHelper = DataBaseHelper.getInstance(getParent());

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
        recyclerViewDrum = (RecyclerView) findViewById(R.id.recyclerViewDrum);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerViewDrum.setLayoutManager(recyclerLayoutManager);

        recyclerViewAdapterDrum = new RecyclerViewAdapterDrum(getAlarmsList());
        recyclerViewDrum.setAdapter(recyclerViewAdapterDrum);

        recyclerViewDrum.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //debug
                int posFirst = recyclerLayoutManager.findFirstVisibleItemPosition();
                int posLast = recyclerLayoutManager.findLastVisibleItemPosition();
                Log.d(TAG, "Drum first pos: " + posFirst + " / Drum Last pos: " + posLast);
                Log.d(TAG, "Drum item count: " + recyclerViewAdapterDrum.getItemCount());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "DX: " + dx + " / DY: " + dy);
                int posFirst = recyclerLayoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "Scrolled first position: " + posFirst);

                //change view elements
                changeView(recyclerView, posFirst);
            }

            /**
             * Change view elements of recycler
             * @param recyclerView
             * @param posFirst
             */
            private void changeView(RecyclerView recyclerView, int posFirst) {
                //Set first view
                LinearLayout linearLayoutFirst = (LinearLayout) recyclerLayoutManager.findViewByPosition(posFirst);
                TextView tvTimeFirst = (TextView) linearLayoutFirst.findViewById(R.id.tvTime);
                View drumUpDividerFirst = linearLayoutFirst.findViewById(R.id.drumUpDivider);
                View drumDownDividerFirst = linearLayoutFirst.findViewById(R.id.drumDownDivider);
                drumUpDividerFirst.setVisibility(View.GONE);
                drumDownDividerFirst.setVisibility(View.GONE);
                tvTimeFirst.setTextSize(24);
                tvTimeFirst.setTypeface(Typeface.createFromAsset(recyclerView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                tvTimeFirst.setTextColor(recyclerView.getResources().getColor(R.color.colorNewEditPastLastTime));

                //Set second view
                LinearLayout linearLayoutSecond = (LinearLayout) recyclerLayoutManager.findViewByPosition(posFirst + 1);
                TextView tvTimeSecond = (TextView) linearLayoutSecond.findViewById(R.id.tvTime);
                View drumUpDividerSecond = linearLayoutSecond.findViewById(R.id.drumUpDivider);
                View drumDownDividerSecond = linearLayoutSecond.findViewById(R.id.drumDownDivider);
                drumUpDividerSecond.setVisibility(View.GONE);
                drumDownDividerSecond.setVisibility(View.GONE);
                tvTimeSecond.setTextSize(36);
                tvTimeSecond.setTypeface(Typeface.createFromAsset(recyclerView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                tvTimeSecond.setTextColor(recyclerView.getResources().getColor(R.color.colorNewEditPastLastTime));

                //Set third view
                LinearLayout linearLayoutThird = (LinearLayout) recyclerLayoutManager.findViewByPosition(posFirst + 2);
                TextView tvTimeThird = (TextView) linearLayoutThird.findViewById(R.id.tvTime);
                View drumUpDividerThird = linearLayoutThird.findViewById(R.id.drumUpDivider);
                View drumDownDividerThird = linearLayoutThird.findViewById(R.id.drumDownDivider);
                drumDownDividerThird.setVisibility(View.VISIBLE);
                drumUpDividerThird.setVisibility(View.VISIBLE);
                tvTimeThird.setTextSize(46);
                tvTimeThird.setTypeface(Typeface.createFromAsset(recyclerView.getContext().getAssets(), "fonts/PFHandbookPro-Black.ttf"));
                tvTimeThird.setTextColor(recyclerView.getResources().getColor(R.color.colorWhite));

                //Set fourth view
                LinearLayout linearLayoutFourth = (LinearLayout) recyclerLayoutManager.findViewByPosition(posFirst + 3);
                TextView tvTimeFourth = (TextView) linearLayoutFourth.findViewById(R.id.tvTime);
                View drumUpDividerFourth = linearLayoutFourth.findViewById(R.id.drumUpDivider);
                View drumDownDividerFourth = linearLayoutFourth.findViewById(R.id.drumDownDivider);
                drumUpDividerFourth.setVisibility(View.GONE);
                drumDownDividerFourth.setVisibility(View.GONE);
                tvTimeFourth.setTextSize(36);
                tvTimeFourth.setTypeface(Typeface.createFromAsset(recyclerView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                tvTimeFourth.setTextColor(recyclerView.getResources().getColor(R.color.colorNewEditPastLastTime));

                //Set fifth view
                LinearLayout linearLayoutFifth = (LinearLayout) recyclerLayoutManager.findViewByPosition(posFirst + 4);
                TextView tvTimeFifth = (TextView) linearLayoutFifth.findViewById(R.id.tvTime);
                View drumUpDividerFifth = linearLayoutFifth.findViewById(R.id.drumUpDivider);
                View drumDownDividerFifth = linearLayoutFifth.findViewById(R.id.drumDownDivider);
                drumUpDividerFifth.setVisibility(View.GONE);
                drumDownDividerFifth.setVisibility(View.GONE);
                tvTimeFifth.setTextSize(24);
                tvTimeFifth.setTypeface(Typeface.createFromAsset(recyclerView.getContext().getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
                tvTimeFifth.setTextColor(recyclerView.getResources().getColor(R.color.colorNewEditPastLastTime));
            }
        });
    }

    /**
     * Create new Alarm with to increase 2 hours
     * @return
     */
    private Alarm getNewAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Alarm newAlarm = new Alarm(calendar.getTime());
        return newAlarm;
    }

    /**
     * Get list of Alarms since database
     * @return
     */
    private ArrayList<Alarm> getAlarmsList() {
        ArrayList<Alarm> list = dataBaseHelper.getAlarmList();
        Log.d(TAG, "size of alarms list: " + list.size());
        list.add(getNewAlarm());
        Log.d(TAG, "size of alarms list: " + list.size());
        return list;
    }

    /**
     * This method is for tested
     *
     * @return
     */
    private ArrayList<AlarmTest> getListAlarm() {
        ArrayList<AlarmTest> list = new ArrayList<AlarmTest>();


        list.add(new AlarmTest());
        //list.add(new AlarmTest("12", "00"));
        //list.add(new AlarmTest("23", "30"));
        //list.add(new AlarmTest("07", "45"));
        //list.add(new AlarmTest("11", "20"));
        //list.add(new AlarmTest("14", "30"));
        //list.add(new AlarmTest("15", "45"));
        //list.add(new AlarmTest("17", "25"));
        //list.add(new AlarmTest("18", "10"));
        //list.add(new AlarmTest("19", "35"));
        return list;
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
