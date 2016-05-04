package com.dzn.dzn.application.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.dzn.dzn.application.Adapters.SpinnerRepeatAdapter;
import com.dzn.dzn.application.R;

import org.w3c.dom.Text;

import java.util.Arrays;

public class NewEditActivity extends AppCompatActivity {
    private static final String TAG = "NewEditActivity";

    private TextView tvNewEditBack;
    private TextView tvNewEditTune;
    private TextView tvNewEditReady;

    private TextView tvNewEditPastTime;
    private TextView tvNewEditLastTime;
    private TextView tvNewEditCurrentTime;
    private TextView tvNewEditNextTime;
    private TextView tvNewEditAfterTime;

    private TextView tvNewEditSetting;

    private TextView tvNewEditRepeat;
    private Spinner spinnerNewEditRepeat;
    private TextView tvNewEditMusic;
    private Spinner spinnerNewEditMusic;
    private TextView tvNewEditInterval;
    private Spinner spinnerNewEditInterval;
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

        tvNewEditSetting = (TextView) findViewById(R.id.tvNewEditSetting);
        tvNewEditSetting.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));

        tvNewEditRepeat = (TextView) findViewById(R.id.tvNewEditRepeat);
        tvNewEditRepeat.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Regular.ttf"));
        spinnerNewEditRepeat = (Spinner) findViewById(R.id.spinnerNewEditRepeat);
        spinnerNewEditRepeat.setAdapter( new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_repeat))));

        tvNewEditMusic = (TextView) findViewById(R.id.tvNewEditMusic);
        tvNewEditMusic.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        spinnerNewEditMusic = (Spinner) findViewById(R.id.spinnerNewEditMusic);
        spinnerNewEditMusic.setAdapter( new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_settings))));

        tvNewEditInterval = (TextView) findViewById(R.id.tvNewEditInterval);
        tvNewEditInterval.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        spinnerNewEditInterval = (Spinner) findViewById(R.id.spinnerNewEditInterval);
        spinnerNewEditInterval.setAdapter( new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_settings))));

        tvNewEditSocialNetwork = (TextView) findViewById(R.id.tvNewEditSocialNetwork);
        tvNewEditSocialNetwork.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        spinnerNewEditSocialNetwork = (Spinner) findViewById(R.id.spinnerNewEditSocialNetwork);
        spinnerNewEditSocialNetwork.setAdapter( new SpinnerRepeatAdapter(
                getApplicationContext(),
                R.layout.new_edit_spinner,
                Arrays.asList(getResources().getStringArray(R.array.new_edit_activity_spinner_settings))));

    }
}
