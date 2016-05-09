package com.dzn.dzn.application.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;

import java.util.ArrayList;

/**
 * Created by victor on 09.05.16.
 */
public class Settings {
    private SharedPreferences settings;
    private Context context;

    private int locale;
    private int sound;
    private int interval;
    private int repeat;
    private boolean vibro;
    private boolean isSocial;
    private boolean addTime;
    private boolean addGeo;
    private String melody;

    private ArrayList<Social> list;

    private DataBaseHelper dbHelper;

    public Settings(Context context){
        this.context = context;
        settings = this.context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        list = new ArrayList<Social>();
        dbHelper = DataBaseHelper.getInstance(context);
    }

    public void load(){
        locale = settings.getInt("locale", 1);
        sound = settings.getInt("sound", 80);
        interval = settings.getInt("interval", 30);
        repeat = settings.getInt("repeat", 5);
        vibro = settings.getBoolean("vibro", true);
        isSocial = settings.getBoolean("social", true);
        addTime = settings.getBoolean("addTime", false);
        addGeo = settings.getBoolean("addGeo", false);
        list = dbHelper.getSocial();
    }

    public void save(){
        SharedPreferences.Editor ed = settings.edit();
        ed.putInt("locale", locale);
        ed.putInt("sound", sound);
        ed.putInt("interval", interval);
        ed.putInt("repeat", repeat);
        ed.putBoolean("vibro", vibro);
        ed.putBoolean("social", isSocial);
        ed.putBoolean("addTime", addTime);
        ed.putBoolean("addGeo", addGeo);
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

}
