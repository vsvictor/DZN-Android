package com.dzn.dzn.application.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by victor on 09.05.16.
 */
public class Settings {
    private static final String TAG = "Settings";
    private SharedPreferences settings;
    private Context context;

    private static Settings mInstance = null;

    private int locale;
    private int sound;
    private int interval;
    private int repeat;
    private boolean vibro;
    private boolean isSocial;
    private boolean addTime;
    private boolean addGeo;
    private String melody;
    private boolean isLocation;
    private boolean isFacebook;
    private boolean isVkontakte;
    private boolean isTwitter;
    private boolean isInstagram;

    private ArrayList<Social> list;

    private DataBaseHelper dbHelper;

    private static final String LOCALE = "locale";
    private static final String SOUND = "sound";
    private static final String INTERVAL = "interval";
    private static final String REPEAT = "repeat";
    private static final String VIBRO = "vibro";
    private static final String SOCIAL = "social";
    private static final String ADD_TIME = "addTime";
    private static final String ADD_GEO = "addGeo";
    private static final String MELODY = "melody";

    private static final String FACEBOOK = "facebook";
    private static final String VKONTAKTE = "vkontakte";
    private static final String TWITTER = "twitter";
    private static final String INSTAGRAM = "instagram";

    public static final String LOCALE_RU = "ru";
    public static final String LOCALE_EN = "en";
    public static final String IS_LOCATION = "isLocation";


    public Settings(Context context){
        this.context = context;
        settings = this.context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        list = new ArrayList<Social>();
        dbHelper = DataBaseHelper.getInstance(context);
    }

    public static Settings getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Settings(context);
        }
        return mInstance;
    }

    public synchronized void load(){
        locale = settings.getInt(LOCALE, 1);
        sound = settings.getInt(SOUND, 80);
        interval = settings.getInt(INTERVAL, 30);
        repeat = settings.getInt(REPEAT, 5);
        vibro = settings.getBoolean(VIBRO, true);
        isSocial = settings.getBoolean(SOCIAL, false);
        addTime = settings.getBoolean(ADD_TIME, false);
        addGeo = settings.getBoolean(ADD_GEO, false);
        melody = settings.getString(MELODY, "");
        isFacebook = settings.getBoolean(FACEBOOK, false);
        isVkontakte = settings.getBoolean(VKONTAKTE, false);
        isTwitter = settings.getBoolean(TWITTER, false);
        isInstagram = settings.getBoolean(INSTAGRAM, false);
        list = dbHelper.getSocial();
        isLocation = settings.getBoolean(IS_LOCATION,true);
    }

    public synchronized void save(){
        SharedPreferences.Editor ed = settings.edit();
        ed.putInt(LOCALE, locale);
        ed.putInt(SOUND, sound);
        ed.putInt(INTERVAL, interval);
        ed.putInt(REPEAT, repeat);
        ed.putBoolean(VIBRO, vibro);
        ed.putBoolean(SOCIAL, isSocial);
        ed.putBoolean(ADD_TIME, addTime);
        ed.putBoolean(ADD_GEO, addGeo);
        ed.putString(MELODY, melody);
        ed.putBoolean(FACEBOOK, isFacebook);
        ed.putBoolean(VKONTAKTE, isVkontakte);
        ed.putBoolean(TWITTER, isTwitter);
        ed.putBoolean(INSTAGRAM, isInstagram);
        ed.putBoolean(IS_LOCATION, isLocation);
        ed.commit();
        dbHelper.setSocial(list);
    }

    public void setLocale(int locale) {this.locale = locale;}
    public int getLocale(){return this.locale;}
    public void setSound(int sound) {this.sound = sound;}
    public int getSound(){return this.sound;}
    public void setInterval(int interval) {this.interval = interval;}
    public int getInterval(){return this.interval;}
    public void setRepeat(int repeat) {this.repeat = repeat;}
    public int getRepeat(){return this.repeat;}
    public void setVibro(boolean vibro) {this.vibro = vibro;}
    public boolean isVibro(){return this.vibro;}
    public void setSocial(boolean social) {this.isSocial = social;}
    public boolean isSocial(){return this.isSocial;}
    public void setAddTime(boolean addTime) {this.addTime = addTime;}
    public boolean isAddTime(){return this.addTime;}
    public void setAddGeo(boolean addGeo) {this.addGeo = addGeo;}
    public boolean isAddGeo(){return this.addGeo;}
    public String getMelody() {
        return melody;
    }
    public void setMelody(String melody) {
        this.melody = melody;
    }
    public void setIsLocation(boolean b){this.isLocation = b;}
    public boolean isLocation(){return this.isLocation;}
    public void setFacebook(boolean isFacebook) { this.isFacebook = isFacebook; }
    public boolean isFacebook() { return this.isFacebook; }

    public void setVkontakte(boolean isVkontakte) { this.isVkontakte = isVkontakte; }
    public boolean isVkontakte() { return this.isVkontakte; }

    public void setTwitter(boolean isTwitter) { this.isTwitter = isTwitter; }
    public boolean isTwitter() { return this.isTwitter; }

    public void setInstagram(boolean isInstagram) { this.isInstagram = isInstagram; }
    public boolean isInstagram() { return this.isInstagram; }

    @Override
    public String toString() {
        return "Settings{" +
                "locale=" + locale +
                ", sound=" + sound +
                ", interval=" + interval +
                ", repeat=" + repeat +
                ", vibro=" + vibro +
                ", isSocial=" + isSocial +
                ", addTime=" + addTime +
                ", addGeo=" + addGeo +
                ", melody='" + melody + '\'' +
                ", list=" + list +
                ", Facebook=" + isFacebook +
                ", Vkontakte=" + isVkontakte +
                ", Twitter=" + isTwitter +
                ", Instagram=" + isInstagram +
                '}';
    }

    public Context getContext() {
        return context;
    }

    /**
     * Return title of melody
     *
     * @return
     */
    public String getMelodyTitle() {
        if (melody != null && !melody.isEmpty()) {
            File file = new File(melody);
            String name = file.getName();
            Log.d(TAG, "File name: " + name);
            if (name.length() > 24) {
                return name.substring(0, 24);
            } else {
                return name;
            }
        }
        return "";
    }
}
