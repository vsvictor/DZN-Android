package com.dzn.dzn.application;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dzn.dzn.application.Activities.NewEditActivity;
import com.dzn.dzn.application.Utils.DataBaseHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView tvMainPrompt;
    private TextView tvHourOne;
    private TextView tvColonOne;
    private TextView tvMinuteOne;
    private TextView tvHourTwo;
    private TextView tvColonTwo;
    private TextView tvMinuteTwo;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        dataBaseHelper = DataBaseHelper.getInstance(this);
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        TextView tvMainPrompt = (TextView) findViewById(R.id.tvMainPrompt);
        tvMainPrompt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-ExtraThin.ttf"));

        TextView tvHourOne = (TextView) findViewById(R.id.tvHourOne);
        TextView tvMinuteOne = (TextView) findViewById(R.id.tvMinuteOne);
        tvHourOne.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Medium.ttf"));
        tvMinuteOne.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Medium.ttf"));

        TextView tvHourTwo = (TextView) findViewById(R.id.tvHourTwo);
        TextView tvMinuteTwo = (TextView) findViewById(R.id.tvMinuteTwo);
        tvHourTwo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Medium.ttf"));
        tvMinuteTwo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Medium.ttf"));

        TextView tvColonOne = (TextView) findViewById(R.id.tvColonOne);
        TextView tvColonTwo = (TextView) findViewById(R.id.tvColonTwo);
        tvColonOne.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));
        tvColonTwo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Thin.ttf"));

        Button btnNew = (Button) findViewById(R.id.btn_new);
        btnNew.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PFHandbookPro-Regular.ttf"));
    }

    public void onSettings(View view) {
        Intent intent = new Intent(MainActivity.this, NewEditActivity.class);
        startActivity(intent);
    }

}
