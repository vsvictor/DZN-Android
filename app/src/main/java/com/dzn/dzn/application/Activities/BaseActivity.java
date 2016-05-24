package com.dzn.dzn.application.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.R;

import java.util.Locale;

/**
 * Created by victor on 23.05.16.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected Settings settings;
    protected Locale locale;
    protected Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSettings();
    }
    @Override
    protected void onResume(){
        super.onResume();
        applySettings();
    }
    @Override
    public void onBackPressed(){

        setResult(RESULT_CANCELED);
        finish();
    }
    protected void initSettings() {
        //Initialize settings
        config = new Configuration();
        settings = Settings.getInstance(this);
        settings.load();
        Log.d(TAG, settings.toString());
    }
    protected void applySettings(){
        //Initialize Locale
        settings.load();
        if (settings.getLocale() == 0) {
            locale = new Locale(Settings.LOCALE_EN);
        } else {
            locale = new Locale(Settings.LOCALE_RU);
        }
        Locale.setDefault(locale);

        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //refresh();
    }
}
