package com.dzn.dzn.application.Objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhenya on 09.05.2016.
 */
public class Alarm implements Serializable {

    private int ID;
    private Date time;
    private int repeat;
    private int sound;
    private String melody;
    private boolean vibro;

    public Alarm () {

    }

    public Alarm(Date date) {
        this.time = date;
    }

    public int getID() {
        return ID;
    }

    public Date getTime() {
        return time;
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

    public boolean isVibro() {
        return vibro;
    }

    public void setTime(Date time) {
        this.time = time;
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
}
