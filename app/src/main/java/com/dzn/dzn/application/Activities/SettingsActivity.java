package com.dzn.dzn.application.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dzn.dzn.application.Dialog.OpenDialogListener;
import com.dzn.dzn.application.Dialog.OpenFileDialog;
import com.dzn.dzn.application.MainActivity;
import com.dzn.dzn.application.MainApplication;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class SettingsActivity extends BaseActivity {
    private static final String TAG = "SettingsActivity";

    private TextView tvSettingsTune;
    private TextView tvSettingsReady;
    private TextView tvSettingsSectionSound;
    private TextView tvSettingsSound;
    private TextView tvNameMelody;
    private TextView tvSettingsSectionAlarm;
    private TextView tvSettingsIntervalRepeat;
    private TextView tvSettingsSectionSocialNetwork;
    private TextView tvSettingsVibro;
    private TextView tvSettingsLocation;
    private TextView tvSettingsUploadPhoto;
    private TextView tvSettingsRU;
    private TextView tvSettingsEN;
    private TextView tvSettingsIntervalWakeUp;
    private TextView tvIntervalWakeUP;
    private TextView tvIntervalRepeat;

    private ToggleButton toggleSettingsVibro;
    private ToggleButton toggleSettingsLocation;
    private ToggleButton toggleSettingsUploadPhoto;

    //Section Social network
    private LinearLayout llSocialNetwork;
    private TextView tvSettingsFacebook;
    private TextView tvSettingsVkontakte;
    private TextView tvSettingsTwitter;
    private TextView tvSettingsInstagram;
    private ToggleButton toggleSettingsFacebook;
    private ToggleButton toggleSettingsVkontakte;
    private ToggleButton toggleSettingsTwitter;
    private ToggleButton toggleSettingsInstagram;

    private SeekBar sbSettingsSound;

    private ImageButton ibSoundMin;
    private ImageButton ibSoundMax;

    private LinearLayout llChoiceMelody;

    private int sender = 0;
    private CallbackManager callbackManager;
    private boolean ch;
    private MainApplication app;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        app = (MainApplication) getApplication();

        Bundle b = getIntent().getExtras();
        if(b != null) sender = b.getInt("sender", 0);
        //Initialize view element
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        settings.save();
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if(!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.d(TAG, "VK login success");
            }
            @Override
            public void onError(VKError error) {
                Log.d(TAG, "VK login error: " + error.errorMessage);
            }
        }));
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

                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
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

                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
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

        tvNameMelody = (TextView) findViewById(R.id.tvNameMelody);
        PFHandbookProTypeFaces.THIN.apply(tvNameMelody);
        Log.d(TAG, "Settings melody: " + settings.getMelody());
        Log.d(TAG, "Settings title of melody: " + settings.getMelodyTitle());
        if (settings != null && !settings.getMelodyTitle().isEmpty()) {
            Log.d(TAG, "Settings title of melody not null");
            tvNameMelody.setText(settings.getMelodyTitle());
        }

        llChoiceMelody = (LinearLayout) findViewById(R.id.llChoiceMelody);
        llChoiceMelody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileDialog builder = new OpenFileDialog(SettingsActivity.this);
                builder.setAccessDeniedMessage("Access denied");
                builder.setFilter(OpenFileDialog.FILE_FILTER);
                builder.setOpenDialogListener(new OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String fileName) {
                        Log.d(TAG, "Selected file: " + fileName);
                        settings.setMelody(fileName);
                        if (!settings.getMelodyTitle().isEmpty()) {
                            tvNameMelody.setText(settings.getMelodyTitle());
                        }
                        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                            mMediaPlayer.stop();
                            mMediaPlayer.release();
                            mMediaPlayer = null;
                        }

                    }
                    @Override
                    public void OnSelectFile(String filename) {
                        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        Log.i(TAG, "Selected file:"+filename);
                        Uri uri = Uri.parse(filename);
                        if (mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
                        if(mMediaPlayer.isPlaying()){
                            mMediaPlayer.stop();
                            mMediaPlayer.release();
                            mMediaPlayer = new MediaPlayer();
                        }
                        float vol = ((float)settings.getSound())/100;
                        Log.i(TAG, "!!!!!!!!!Volue:"+vol);
                        try {
                            mMediaPlayer.setDataSource(SettingsActivity.this, uri);
                            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                                mMediaPlayer.setVolume(vol, vol);
                                mMediaPlayer.setLooping(true);
                                mMediaPlayer.prepare();
                                mMediaPlayer.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e){

                        }
                    }
                });
                builder.show();
            }
        });

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

        //button of decrease sound
        ibSoundMin = (ImageButton) findViewById(R.id.idSettingsSoundOff);
        ibSoundMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sound = settings.getSound();
                if (sound > 0) {
                    sound--;
                    settings.setSound(sound);
                    sbSettingsSound.setProgress(sound);
                }
            }
        });

        //button of increase sound
        ibSoundMax = (ImageButton) findViewById(R.id.idSettingsSoundOn);
        ibSoundMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sound = settings.getSound();
                if (sound < 100) {
                    sound++;
                    settings.setSound(sound);
                    sbSettingsSound.setProgress(sound);
                }
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

        //menu item of vibration
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

        //menu item of location
        tvSettingsLocation = (TextView) findViewById(R.id.tvSettingsLocation);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsLocation);

        toggleSettingsLocation = (ToggleButton) findViewById(R.id.toggleSettingsLocation);
        toggleSettingsLocation.setChecked(settings.isLocation());
        toggleSettingsLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setIsLocation(isChecked);
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

        tvSettingsFacebook = (TextView) findViewById(R.id.tvSettingsFacebook);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsFacebook);
        tvSettingsVkontakte = (TextView) findViewById(R.id.tvSettingsVkontakte);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsVkontakte);
        tvSettingsTwitter = (TextView) findViewById(R.id.tvSettingsTwitter);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsTwitter);
        tvSettingsInstagram = (TextView) findViewById(R.id.tvSettingsInstagram);
        PFHandbookProTypeFaces.THIN.apply(tvSettingsInstagram);

        llSocialNetwork = (LinearLayout) findViewById(R.id.linearSettingsSN);
        toggleSettingsFacebook = (ToggleButton) findViewById(R.id.toggleSettingsFacebook);
        toggleSettingsFacebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFacebook(isChecked);
                if(isChecked && ch) {
                    callbackManager = CallbackManager.Factory.create();
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            AccessToken token = loginResult.getAccessToken();
                            AccessToken.setCurrentAccessToken(token);
                        }

                        @Override
                        public void onCancel() {
                            Log.i(TAG, "Cancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.i(TAG, "Error");
                        }
                    });
                    LoginManager.getInstance().logInWithReadPermissions(SettingsActivity.this, Arrays.asList("public_profile"));
                }
            }
        });
        ch = false;
        toggleSettingsFacebook.setChecked(settings.isFacebook());
        ch = true;

        toggleSettingsVkontakte = (ToggleButton) findViewById(R.id.toggleSettingsVkontakte);
        toggleSettingsVkontakte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setVkontakte(isChecked);
                if(isChecked && ch){
                    if (!VKSdk.wakeUpSession(SettingsActivity.this)) {
                        Log.d(TAG, "VK authorize");
                        VKSdk.login(SettingsActivity.this, app.getVKScope());
                    }
                }
            }
        });
        ch = false;
        toggleSettingsVkontakte.setChecked(settings.isVkontakte());
        ch = true;

        toggleSettingsTwitter = (ToggleButton) findViewById(R.id.toggleSettingsTwitter);
        toggleSettingsTwitter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setTwitter(isChecked);
                if(isChecked && ch) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.twitter.android");
                    if(intent == null){
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id="+"com.twitter.android"));
                        startActivity(intent);
                    }
                }
            }
        });

        ch = false;
        toggleSettingsTwitter.setChecked(settings.isTwitter());
        ch=true;

        toggleSettingsInstagram = (ToggleButton) findViewById(R.id.toggleSettingsInstagram);
        toggleSettingsInstagram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setInstagram(isChecked);
                if(isChecked && ch) {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                    if(intent == null){
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
                        startActivity(intent);
                    }
                }
            }
        });
        ch = false;
        toggleSettingsInstagram.setChecked(settings.isInstagram());
        ch = true;

        toggleSettingsUploadPhoto = (ToggleButton) findViewById(R.id.toggleSettingsUploadPhoto);
        toggleSettingsUploadPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSocial(isChecked);

                if (isChecked) {
                    llSocialNetwork.setVisibility(View.VISIBLE);
                    setCheckedSocialNetwork();
                } else {
                    llSocialNetwork.setVisibility(View.GONE);
                    settings.setFacebook(false);
                    settings.setVkontakte(false);
                    settings.setTwitter(false);
                    settings.setInstagram(false);
                    setCheckedSocialNetwork();
                }
                settings.save();
            }
        });
        toggleSettingsUploadPhoto.setChecked(settings.isSocial());
    }

    private void setCheckedSocialNetwork() {
        ch = false;
        toggleSettingsFacebook.setChecked(settings.isFacebook());
        toggleSettingsVkontakte.setChecked(settings.isVkontakte());
        toggleSettingsTwitter.setChecked(settings.isTwitter());
        toggleSettingsInstagram.setChecked(settings.isInstagram());
        ch = true;
    }

    /**
     * Check installed application
     *
     * @param str
     * @return
     */
    private boolean isAppInstalled(String str) {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            Log.d(TAG, str + " installed: true");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, str + " installed: false");
            return false;
        }
    }

    /**
     * Save all settings
     *
     * @param view
     */
    public void saveSettings(View view) {
        settings.save();
        Log.d(TAG, "Settings: " + settings.toString());
        //applySettings();
        Intent intent = new Intent(this, MainActivity.class);
        //if(sender == 0) intent.setClass(this, MainActivity.class);
        //else intent.setClass(this, StartActivity.class);
        startActivity(intent);
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
        if (interval > 1) {
            interval--;
        }
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
        if (interval < 59) {
            interval++;
        }
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
        if (interval > 1) {
            interval--;
        }
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
        if (interval < 59) {
            interval++;
        }
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
