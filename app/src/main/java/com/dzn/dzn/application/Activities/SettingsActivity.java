package com.dzn.dzn.application.Activities;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private TextView tvSettingsSectionAlarm;
    private TextView tvSettingsSectionSocialNetwork;
    private TextView tvSettingsVibro;
    private TextView tvSettingsUploadPhoto;
    private TextView tvSettingsRU;
    private TextView tvSettingsEN;

    private ToggleButton toggleSettingsVibro;
    private ToggleButton toggleSettingsUploadPhoto;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = new Settings(getApplicationContext());

        //Initialize view element
        initView();
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

        tvSettingsSectionSound = (TextView) findViewById(R.id.tvSettingsSectionSound);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsSectionSound);

        tvSettingsSectionAlarm = (TextView) findViewById(R.id.tvSettingsSectionAlarm);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsSectionAlarm);

        tvSettingsSectionSocialNetwork = (TextView) findViewById(R.id.tvSettingsSectionSocialNetwork);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsSectionSocialNetwork);

        tvSettingsVibro = (TextView) findViewById(R.id.tvSettingsVibro);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsVibro);

        toggleSettingsVibro = (ToggleButton) findViewById(R.id.toggleSettingsVibro);

        tvSettingsUploadPhoto = (TextView) findViewById(R.id.tvSettingsUploadPhoto);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsUploadPhoto);

        toggleSettingsUploadPhoto = (ToggleButton) findViewById(R.id.toggleSettingsUploadPhoto);
    }

    /**
     * Initialize section of Locale
     */
    private void initSectionLocale() {
        tvSettingsRU = (TextView) findViewById(R.id.tvSettingsRU);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsRU);
        tvSettingsRU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                tvSettingsEN.setSelected(!tvSettingsEN.isSelected());
                settings.setLocale(v.isSelected() ? 1 : 0);
                Log.d(TAG, "Locale: " + settings.getLocale());
            }
        });
        tvSettingsRU.setSelected(settings.getLocale() == 1 ? true : false);

        tvSettingsEN = (TextView) findViewById(R.id.tvSettingsEN);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsEN);
        tvSettingsEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                tvSettingsRU.setSelected(!tvSettingsRU.isSelected());
                settings.setLocale(v.isSelected() ? 0 : 1);
                Log.d(TAG, "Locale: " + settings.getLocale());
            }
        });
        tvSettingsRU.setSelected(settings.getLocale() == 0 ? true : false);
    }

    /**
     * Save all settings
     * @param view
     */
    public void saveSettings(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        //Save settings
        settings.save();
    }
}
