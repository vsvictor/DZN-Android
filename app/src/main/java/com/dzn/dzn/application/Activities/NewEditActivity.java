package com.dzn.dzn.application.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dzn.dzn.application.Adapters.SpinnerRepeatAdapter;
import com.dzn.dzn.application.Dialog.OpenDialogListener;
import com.dzn.dzn.application.Dialog.OpenFileDialog;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;
import com.dzn.dzn.application.Widget.WheelView;
import com.dzn.dzn.application.Widget.adapters.NumericWheelAdapter;

import java.util.Arrays;
import java.util.Date;

public class NewEditActivity extends BaseActivity {
    private static final String TAG = "NewEditActivity";

    private TextView tvNewEditBack;
    private TextView tvNewEditTune;
    private TextView tvNewEditReady;

    private TextView tvNewEditSetting;

    //Section Repeat
    private TextView tvNewEditRepeat;
    private Spinner spinnerNewEditRepeat;
    private LinearLayout linearNewEditWeek;

    //Section Music
    private TextView tvNewEditMusic;
    private Spinner spinnerNewEditMusic;
    private ToggleButton toggleNewEditMusic;

    //Section Interval
    private TextView tvNewEditInterval;
    private Spinner spinnerNewEditInterval;

    //Section Social Network
    private TextView tvNewEditSocialNetwork;
    private Spinner spinnerNewEditSocialNetwork;
    private ToggleButton toggleNewEditSocialNetwork;
    private LinearLayout llSocialNetwork;
    private ToggleButton toggleAlarmFacebook;
    private ToggleButton toggleAlarmVkontakte;
    private ToggleButton toggleAlarmTwitter;
    private ToggleButton toggleAlarmInstagram;

    private WheelView npHours;
    private WheelView npMinutes;

    private int iHours;
    private int iMinutes;

    private Alarm edAlarm;
    private int id = -1;
    private DataBaseHelper dataBaseHelper;
    private NumericWheelAdapter hAdapter;
    private NumericWheelAdapter mAdapter;
    private ToggleButton[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit);
        dataBaseHelper = DataBaseHelper.getInstance(getParent());
        days = new ToggleButton[7];
        Bundle b = getIntent().getExtras();

        if (b != null) {
            id = b.getInt("idAlarm", -1);
            edAlarm = dataBaseHelper.getAlarm(id);
            if (edAlarm != null) {
                iHours = edAlarm.getDate().getHours();
                iMinutes = edAlarm.getDate().getMinutes();
            } else {
                Date d = new Date();
                iHours = d.getHours();
                iMinutes = d.getMinutes();
            }
        } else {
            createAlarm();
            iHours = edAlarm.getDate().getHours();
            iMinutes = edAlarm.getDate().getMinutes();
        }

        //Initialize settings
        settings = Settings.getInstance(getApplication());
        settings.load();

        //Initialize view elements
        initView();
    }

    /**
     * Create new Alarm
     */
    private void createAlarm() {
        Alarm al = new Alarm();
        Date d = new Date();
        Log.d(TAG, "Current time: " + d.toString());
        al.setTime(d);
        al.setDefault();
        edAlarm = al;
        Log.d(TAG, "edAlarm: " + edAlarm.toString());
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
        hAdapter = new NumericWheelAdapter(this, 0, 23, "%02d");
        hAdapter.setItemResource(R.layout.wheel_item_time);
        hAdapter.setItemTextResource(R.id.tvNumber);
        npHours = (WheelView) findViewById(R.id.npHours);
        npHours.setViewAdapter(hAdapter);
        npHours.setCyclic(true);
        npHours.setVisibleItems(5);
        if (id > 0) {
            npHours.setCurrentItem(edAlarm.getDate().getHours());
        } else {
            Date d = new Date();
            npHours.setCurrentItem(d.getHours());
        }

        mAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
        mAdapter.setItemResource(R.layout.wheel_item_time);
        mAdapter.setItemTextResource(R.id.tvNumber);
        npMinutes = (WheelView) findViewById(R.id.npMinutes);
        npMinutes.setViewAdapter(mAdapter);
        npMinutes.setCyclic(true);
        npMinutes.setVisibleItems(5);
        if (id > 0) {
            npMinutes.setCurrentItem(edAlarm.getDate().getMinutes());
        } else {
            Date d = new Date();
            npMinutes.setCurrentItem(d.getMinutes());
        }
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

        days[0] = (ToggleButton) findViewById(R.id.toggleNewEditSunday);
        days[1] = (ToggleButton) findViewById(R.id.toggleNewEditMonday);
        days[2] = (ToggleButton) findViewById(R.id.toggleNewEditTuesday);
        days[3] = (ToggleButton) findViewById(R.id.toggleNewEditWednesday);
        days[4] = (ToggleButton) findViewById(R.id.toggleNewEditThursday);
        days[5] = (ToggleButton) findViewById(R.id.toggleNewEditFriday);
        days[6] = (ToggleButton) findViewById(R.id.toggleNewEditSaturday);


        if (id > 0) {
            for (int i = 0; i < 7; i++) {
                days[i].setChecked(edAlarm.isDayOn(i));
            }
        }
        if (id > 0) {
            if (edAlarm.isOne()) spinnerNewEditRepeat.setSelection(0);
            else if (edAlarm.isAllDays()) spinnerNewEditRepeat.setSelection(1);
            else if (edAlarm.isWokedDays()) spinnerNewEditRepeat.setSelection(2);
            else if (edAlarm.isWeekEnd()) spinnerNewEditRepeat.setSelection(3);
            else spinnerNewEditRepeat.setSelection(4);
        } else spinnerNewEditRepeat.setSelection(0);


        linearNewEditWeek = (LinearLayout) findViewById(R.id.linearNewEditWeek);
        spinnerNewEditRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (id > 0) {
                        for (int i = 0; i < 7; i++) {
                            edAlarm.setDay(i, false);
                            days[i].setChecked(false);
                        }
                    } else {
                        for (int i = 0; i < 7; i++) {
                            days[i].setChecked(false);
                        }
                    }
                    linearNewEditWeek.setVisibility(View.GONE);
                } else if (position == 1) {
                    for (int i = 0; i < 7; i++) {
                        edAlarm.setDay(i, true);
                        days[i].setChecked(true);
                    }
                    linearNewEditWeek.setVisibility(View.GONE);
                } else if (position == 2) {
                    for (int i = 0; i < 7; i++) {
                        edAlarm.setDay(i, true);
                        days[i].setChecked(true);
                    }
                    edAlarm.setDay(0, false);
                    edAlarm.setDay(6, false);
                    days[0].setChecked(false);
                    days[6].setChecked(false);
                    linearNewEditWeek.setVisibility(View.GONE);
                } else if (position == 3) {
                    for (int i = 0; i < 7; i++) {
                        edAlarm.setDay(i, false);
                        days[i].setChecked(false);
                    }
                    edAlarm.setDay(0, true);
                    edAlarm.setDay(6, true);
                    days[0].setChecked(true);
                    days[6].setChecked(true);
                    linearNewEditWeek.setVisibility(View.GONE);
                } else if (position == 4) {
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
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_music)));
        adapter.setDropDownViewResource(R.layout.new_edit_spinner_drop);

        spinnerNewEditMusic.setAdapter(adapter);
        if (edAlarm.getMelody() == null || edAlarm.getMelody().equals("")) {
            spinnerNewEditMusic.setSelection(0, false);
        } else {
            spinnerNewEditMusic.setSelection(1, false);
        }
        spinnerNewEditMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        edAlarm.setMelody("");
                        break;
                    case 1:
                        OpenFileDialog builder = new OpenFileDialog(NewEditActivity.this);
                        builder.setAccessDeniedMessage("Access denied");
                        builder.setFilter(OpenFileDialog.FILE_FILTER);
                        builder.setOpenDialogListener(new OpenDialogListener() {
                            @Override
                            public void OnSelectedFile(String fileName) {
                                Log.d(TAG, "Selected file: " + fileName);
                                if (fileName != null) {
                                    edAlarm.setMelody(fileName);
                                } else {
                                    Log.d(TAG, "Selected file: null");
                                    edAlarm.setMelody("");
                                    spinnerNewEditMusic.setSelection(0);
                                }
                            }
                        });
                        builder.show();
                        break;
                    default:
                        edAlarm.setMelody("");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toggleNewEditMusic = (ToggleButton) findViewById(R.id.toggleNewEditMusic);
        PFHandbookProTypeFaces.THIN.apply(toggleNewEditMusic);
        toggleNewEditMusic.setChecked(edAlarm.isVibro());
        toggleNewEditMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edAlarm.setVibro(isChecked);
            }
        });
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

        llSocialNetwork = (LinearLayout) findViewById(R.id.linearAlarmSN);

        spinnerNewEditSocialNetwork = (Spinner) findViewById(R.id.spinnerNewEditSocialNetwork);
        SpinnerRepeatAdapter adapter = new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_social_network)));
        adapter.setDropDownViewResource(R.layout.new_edit_spinner_drop);
        spinnerNewEditSocialNetwork.setAdapter(adapter);
        spinnerNewEditSocialNetwork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    llSocialNetwork.setVisibility(View.GONE);
                    edAlarm.setFacebook(settings.isFacebook());
                    edAlarm.setVkontakte(settings.isVkontakte());
                    edAlarm.setTwitter(settings.isTwitter());
                    edAlarm.setInstagram(settings.isInstagram());
                }
                if (position == 1) {
                    llSocialNetwork.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toggleAlarmFacebook = (ToggleButton) findViewById(R.id.toggleAlarmFacebook);
        toggleAlarmFacebook.setChecked(edAlarm.isFacebook());
        toggleAlarmFacebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edAlarm.setFacebook(isChecked);
            }
        });

        toggleAlarmVkontakte = (ToggleButton) findViewById(R.id.toggleAlarmVkontakte);
        toggleAlarmVkontakte.setChecked(edAlarm.isVkontakte());
        toggleAlarmVkontakte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edAlarm.setVkontakte(isChecked);
            }
        });

        toggleAlarmTwitter = (ToggleButton) findViewById(R.id.toggleAlarmTwitter);
        toggleAlarmTwitter.setChecked(edAlarm.isTwitter());
        toggleAlarmTwitter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edAlarm.setTwitter(isChecked);
            }
        });

        toggleAlarmInstagram = (ToggleButton) findViewById(R.id.toggleAlarmInstagram);
        toggleAlarmInstagram.setChecked(edAlarm.isInstagram());
        toggleAlarmInstagram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edAlarm.setInstagram(isChecked);
            }
        });

        toggleNewEditSocialNetwork = (ToggleButton) findViewById(R.id.toggleNewEditSocialNetwork);
        if (!edAlarm.isFacebook() && !edAlarm.isVkontakte() && !edAlarm.isTwitter() && !edAlarm.isInstagram()) {
            spinnerNewEditSocialNetwork.setSelection(0);
            toggleNewEditSocialNetwork.setChecked(false);
            llSocialNetwork.setVisibility(View.GONE);
        } else if (settings.isSocial()
                && (edAlarm.isFacebook() == settings.isFacebook())
                && (edAlarm.isVkontakte() == settings.isVkontakte())
                && (edAlarm.isTwitter() == settings.isTwitter())
                && (edAlarm.isInstagram() == settings.isInstagram())
                ) {
            spinnerNewEditSocialNetwork.setSelection(0);
            toggleNewEditSocialNetwork.setChecked(true);
        } else {
            spinnerNewEditSocialNetwork.setSelection(1);
            toggleNewEditSocialNetwork.setChecked(true);
            toggleAlarmFacebook.setChecked(edAlarm.isFacebook());
            toggleAlarmVkontakte.setChecked(edAlarm.isVkontakte());
            toggleAlarmTwitter.setChecked(edAlarm.isTwitter());
            toggleAlarmInstagram.setChecked(edAlarm.isInstagram());
            llSocialNetwork.setVisibility(View.VISIBLE);
        }

        toggleNewEditSocialNetwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Spinner: " + spinnerNewEditSocialNetwork.getSelectedItemPosition());
                if (isChecked && spinnerNewEditSocialNetwork.getSelectedItemPosition() == 0) {
                    edAlarm.setFacebook(settings.isFacebook());
                    edAlarm.setVkontakte(settings.isVkontakte());
                    edAlarm.setTwitter(settings.isTwitter());
                    edAlarm.setInstagram(settings.isInstagram());
                } else if (isChecked && spinnerNewEditSocialNetwork.getSelectedItemPosition() == 1) {
                    llSocialNetwork.setVisibility(View.VISIBLE);
                } else {
                    llSocialNetwork.setVisibility(View.GONE);
                    edAlarm.setFacebook(false);
                    edAlarm.setVkontakte(false);
                    edAlarm.setTwitter(false);
                    edAlarm.setInstagram(false);
                }
                toggleAlarmFacebook.setChecked(edAlarm.isFacebook());
                toggleAlarmVkontakte.setChecked(edAlarm.isFacebook());
                toggleAlarmTwitter.setChecked(edAlarm.isTwitter());
                toggleAlarmInstagram.setChecked(edAlarm.isInstagram());
                Log.d(TAG, "Settings: " + settings.toString());
                Log.d(TAG, "Alarm: " + edAlarm.toString());
            }
        });
    }

    /**
     * Save data to database
     */
    public void saveData(View view) {
        if (id == -1) {
            Alarm al = new Alarm();
            Date d = new Date();
            d.setHours(npHours.getCurrentItem());
            d.setMinutes(npMinutes.getCurrentItem());
            //d.UTC(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, npHours.getValue(), npMinutes.getValue(),0);
            al.setTime(d);
            al.setMelody("");
            al.setRepeat(5);
            al.setVibro(true);
            al.setSound(80);
            al.setTurnOn(true);
            for (int i = 0; i < 7; i++) {
                al.setDay(i, days[i].isChecked());
            }

            dataBaseHelper.addAlarm(al);
        } else {
            edAlarm.getDate().setHours(npHours.getCurrentItem());
            edAlarm.getDate().setMinutes(npMinutes.getCurrentItem());
            for (int i = 0; i < 7; i++) {
                edAlarm.setDay(i, days[i].isChecked());
            }
            dataBaseHelper.updateAlarm(edAlarm);
        }
        Log.d(TAG, "Alarm: " + edAlarm.toString());
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Cancel - close activity
     *
     * @param view
     */
    public void onCancel(View view) {
        finish();
    }
}
