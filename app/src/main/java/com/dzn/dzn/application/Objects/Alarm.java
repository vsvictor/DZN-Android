package com.dzn.dzn.application.Objects;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhenya on 09.05.2016.
 */
public class Alarm implements Serializable {

    private int ID;
    private Date date;
    private int repeat;
    private int sound;
    private String melody;
    private boolean vibro;
    private boolean visible;

    public Alarm () {
        this.visible = true;
    }

    public Alarm(Date date) {
        this();
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public Date getDate() {
        return date;
    }

    public int getRepeat() {
        return repeat;
    }

    public int getSound() {
        return sound;
    }

    public String getMelody() {
        return melody;
    }

    public String getTime() {
        String time = "00:00";
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        }
        return time;
    }

    public boolean isVibro() {
        return vibro;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTime(Date date) {
        this.date = date;
    }

    public void setTime(int time) {
        this.date = new Date(time);
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public void setMelody(String melody) {
        this.melody = melody;
    }

    public void setVibro(boolean vibro) {
        this.vibro = vibro;
    }

    public void setVibro(int vibro) {
        this.vibro = (vibro == 1) ? true : false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
