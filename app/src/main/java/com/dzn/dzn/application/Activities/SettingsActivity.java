package com.dzn.dzn.application.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dzn.dzn.application.MainActivity;
import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private TextView tvSettingsTune;
    private TextView tvSettingsReady;
    private TextView tvSettingsSectionSound;
    private TextView tvSettingsSound;
    private TextView tvSettingsSectionAlarm;
    private TextView tvSettingsIntervalRepeat;
    private TextView tvSettingsSectionSocialNetwork;
    private TextView tvSettingsVibro;
    private TextView tvSettingsUploadPhoto;
    private TextView tvSettingsRU;
    private TextView tvSettingsEN;
    private TextView tvSettingsIntervalWakeUp;
    private TextView tvIntervalWakeUP;
    private TextView tvIntervalRepeat;

    private ToggleButton toggleSettingsVibro;
    private ToggleButton toggleSettingsUploadPhoto;

    private SeekBar sbSettingsSound;

    private Settings settings;

    private ImageButton ibSoundMin;
    private ImageButton ibSoundMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = Settings.getInstance(getParent());

        //Initialize view element
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        //Load settings
        settings.load();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        //Save settings
        settings.save();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvSettingsTune = (TextView) findViewById(R.id.tvSettingsTune);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsTune);

        tvSettingsReady = (TextView) findViewById(R.id.tvSettingsReady);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsReady);

        //Initialize section of Locale
        initSectionLocale();

        //Initialize section of Sound
        initSectionSound();

        //initialize section of Alarm
        initSectionAlarm();

        //Initialize section of Social Network
        initSectionSocialNetwork();
    }

    /**
     * Initialize section of Locale
     */
    private void initSectionLocale() {
        Log.d(TAG, "locale: " + settings.getLocale());

        tvSettingsRU = (TextView) findViewById(R.id.tvSettingsRU);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsRU);
        tvSettingsEN = (TextView) findViewById(R.id.tvSettingsEN);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsEN);

        if (settings.getLocale() == 1) {
            tvSettingsRU.setSelected(true);
        } else {
            tvSettingsEN.setSelected(true);
        }

        tvSettingsRU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
                tvSettingsEN.setSelected(false);
                settings.setLocale(1);
                settings.save();
                Log.d(TAG, "Locale: " + settings.getLocale());

                Locale locale = new Locale("ru");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, null);

                MainActivity activity = (MainActivity) settings.getContext();
                activity.recreate();
                recreate();
            }
        });

        tvSettingsEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
                tvSettingsRU.setSelected(false);
                settings.setLocale(0);
                settings.save();
                Log.d(TAG, "Locale: " + settings.getLocale());

                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, null);

                MainActivity activity = (MainActivity) settings.getContext();
                activity.recreate();
                recreate();
            }
        });
    }

    /**
     * Initialize section of sound
     */
    private void initSectionSound() {
        tvSettingsSectionSound = (TextView) findViewById(R.id.tvSettingsSectionSound);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsSectionSound);

        tvSettingsSound = (TextView) findViewById(R.id.tvSettingsSound);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsSound);

        sbSettingsSound = (SeekBar) findViewById(R.id.sbSettingsSound);
        sbSettingsSound.setProgress(settings.getSound());
        sbSettingsSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                settings.setSound(progress);
                settings.save();
                Log.d(TAG, "Sound is: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ibSoundMin = (ImageButton) findViewById(R.id.idSettingsSoundOff);
        ibSoundMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbSettingsSound.setProgress(0);
            }
        });

        ibSoundMax = (ImageButton) findViewById(R.id.idSettingsSoundOn);
        ibSoundMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbSettingsSound.setProgress(100);
            }
        });

    }

    /**
     * Initialize section of alarm
     */
    private void initSectionAlarm() {
        tvSettingsSectionAlarm = (TextView) findViewById(R.id.tvSettingsSectionAlarm);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsSectionAlarm);

        tvSettingsIntervalWakeUp = (TextView) findViewById(R.id.tvSettingsIntervalWakeUp);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsIntervalWakeUp);

        tvSettingsIntervalRepeat = (TextView) findViewById(R.id.tvSettingsIntervalRepeat);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsIntervalRepeat);

        tvSettingsVibro = (TextView) findViewById(R.id.tvSettingsVibro);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsVibro);

        toggleSettingsVibro = (ToggleButton) findViewById(R.id.toggleSettingsVibro);
        toggleSettingsVibro.setChecked(settings.isVibro());
        toggleSettingsVibro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setVibro(isChecked);
                settings.save();
            }
        });

        tvIntervalWakeUP = (TextView) findViewById(R.id.tvIntervalWakeUP);
        PFHandbookProTypeFaces.THIN.apply(tvIntervalWakeUP);
        setTvIntervalWakeUp();

        tvIntervalRepeat = (TextView) findViewById(R.id.tvIntervalRepeat);
        PFHandbookProTypeFaces.THIN.apply(tvIntervalRepeat);
        setTvIntervalRepeat();
    }

    /**
     * Initialize section of Social Network
     */
    private void initSectionSocialNetwork() {
        tvSettingsSectionSocialNetwork = (TextView) findViewById(R.id.tvSettingsSectionSocialNetwork);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsSectionSocialNetwork);

        tvSettingsUploadPhoto = (TextView) findViewById(R.id.tvSettingsUploadPhoto);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsUploadPhoto);

        toggleSettingsUploadPhoto = (ToggleButton) findViewById(R.id.toggleSettingsUploadPhoto);
        toggleSettingsUploadPhoto.setChecked(settings.isSocial());
        toggleSettingsUploadPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSocial(isChecked);
                settings.save();
            }
        });
    }

    /**
     * Save all settings
     *
     * @param view
     */
    public void saveSettings(View view) {
        settings.save();
        finish();
    }

    /**
     * Run Alarms activity
     *
     * @param view
     */
    public void onList(View view) {
        Intent intent = new Intent(SettingsActivity.this, AlarmsActivity.class);
        startActivity(intent);
    }

    /**
     * Decrease interval wakeup
     * @param view
     */
    public void decreaseWakeUp(View view) {
        int interval = settings.getInterval();
        interval--;
        settings.setInterval(interval);
        setTvIntervalWakeUp();
        Log.d(TAG, "Interval wakeup: " + interval);
    }

    /**
     * Increase interval wakeup
     * @param view
     */
    public void increaseWakeUp(View view) {
        int interval = settings.getInterval();
        interval++;
        settings.setInterval(interval);
        setTvIntervalWakeUp();
        Log.d(TAG, "Interval wakeup: " + interval);
    }

    /**
     * Decrease interval repeat
     * @param view
     */
    public void decreaseRepeatInterval(View view) {
        int interval = settings.getRepeat();
        interval--;
        settings.setRepeat(interval);
        setTvIntervalRepeat();
        Log.d(TAG, "Interval repeat: " + interval);
    }

    /**
     * Increase interval repeat
     * @param view
     */
    public void increaseRepeatInterval(View view) {
        int interval = settings.getRepeat();
        interval++;
        settings.setRepeat(interval);
        setTvIntervalRepeat();
        Log.d(TAG, "Interval repeat: " + interval);
    }

    /**
     * Show interval wakeup
     */
    private void setTvIntervalWakeUp() {
        tvIntervalWakeUP.setText(settings.getInterval() + " " + getResources().getString(R.string.settings_activity_minutes));
    }

    /**
     * Show interval repeat
     */
    private void setTvIntervalRepeat() {
        tvIntervalRepeat.setText(settings.getRepeat() + " " + getResources().getString(R.string.settings_activity_minutes));
    }
}
