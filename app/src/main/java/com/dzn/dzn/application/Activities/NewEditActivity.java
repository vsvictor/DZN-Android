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
import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.R;

import java.util.ArrayList;
import java.util.Arrays;

public class NewEditActivity extends AppCompatActivity {
    private static final String TAG = "NewEditActivity";

    private TextView tvNewEditBack;
    private TextView tvNewEditTune;
    private TextView tvNewEditReady;

    //private TextView tvNewEditPastTime;
    //private TextView tvNewEditLastTime;
    //private TextView tvNewEditCurrentTime;
    //private TextView tvNewEditNextTime;
    //private TextView tvNewEditAfterTime;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit);

        initView();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvNewEditBack = (TextView) findViewById(R.id.tvNewEditBack);
        tvNewEditBack.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        tvNewEditTune = (TextView) findViewById(R.id.tvNewEditTune);
        tvNewEditTune.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        tvNewEditReady = (TextView) findViewById(R.id.tvNewEditReady);
        tvNewEditReady.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));

        //Initialize section tested
        //initSectionTested();

        tvNewEditSetting = (TextView) findViewById(R.id.tvNewEditSetting);
        tvNewEditSetting.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));

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

        recyclerViewAdapterDrum = new RecyclerViewAdapterDrum(getListAlarm());
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
     * This method is for tested
     *
     * @return
     */
    private ArrayList<AlarmTest> getListAlarm() {
        ArrayList<AlarmTest> list = new ArrayList<AlarmTest>();
        //list.add(new AlarmTest());
        list.add(new AlarmTest("12", "00"));
        list.add(new AlarmTest("23", "30"));
        list.add(new AlarmTest("07", "45"));
        list.add(new AlarmTest("11", "20"));
        list.add(new AlarmTest("14", "30"));
        list.add(new AlarmTest("15", "45"));
        list.add(new AlarmTest("17", "25"));
        list.add(new AlarmTest("18", "10"));
        list.add(new AlarmTest("19", "35"));
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
        tvNewEditMusic.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
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
        tvNewEditInterval.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
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
        tvNewEditSocialNetwork.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        spinnerNewEditSocialNetwork = (Spinner) findViewById(R.id.spinnerNewEditSocialNetwork);

        SpinnerRepeatAdapter adapter = new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_settings)));
        adapter.setDropDownViewResource(R.layout.new_edit_spinner_drop);

        spinnerNewEditSocialNetwork.setAdapter(adapter);
    }
}
