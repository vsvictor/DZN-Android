package com.dzn.dzn.application.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dzn.dzn.application.R;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    /**
     * Initialize view elements
     */
    private void initView() {

    }

}
