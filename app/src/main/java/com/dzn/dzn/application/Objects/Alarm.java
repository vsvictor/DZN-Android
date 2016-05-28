package com.dzn.dzn.application.Objects;


import java.io.Serializable;
import java.util.Arrays;
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
    private boolean turnOn;
    private boolean[] days = new boolean[7];

    private boolean isFacebook = false;
    private boolean isVkontakte = false;
    private boolean isTwitter = false;
    private boolean isInstagram = false;

    public Alarm () {
        this.visible = true;
    }

    public Alarm(Date date) {
        this();
        this.date = date;
        for(int i = 0; i<7; i++) days[i] = false;
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

    public boolean isTurnOn() {
        return turnOn;
    }

    public void setFacebook(boolean facebook) {
        isFacebook = facebook;
    }

    public boolean isFacebook() {
        return isFacebook;
    }

    public void setVkontakte(boolean vkontakte) {
        isVkontakte = vkontakte;
    }

    public boolean isVkontakte() {
        return isVkontakte;
    }

    public void setTwitter(boolean twitter) {
        isTwitter = twitter;
    }

    public boolean isTwitter() {
        return isTwitter;
    }

    public void setInstagram(boolean instagram) {
        isInstagram = instagram;
    }

    public boolean isInstagram() {
        return isInstagram;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTime(Date date) {
        this.date = date;
    }

    public void setTime(long time) {
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setTurnOn(boolean turnOn) {
        this.turnOn = turnOn;
    }

    public void setDay(int number, boolean value){
        this.days[number] = value;
    }
    public boolean isDayOn(int number){return this.days[number];}
    public boolean isAllDays(){return (days[0]&&days[1]&&days[2]&&days[3]&&days[4]&&days[5]&&days[6]);}
    public boolean isWokedDays(){
        return (days[1]&&days[2]&&days[3]&&days[4]&&days[5]&&!days[6]&&!days[0]);
    }
    public boolean isWeekEnd(){
        return (days[0]&&days[6]&&!days[1]&&!days[2]&&!days[3]&&!days[4]&&!days[5]);
    }
    public boolean isOne(){return (!days[0]&&!days[1]&&!days[2]&&!days[3]&&!days[4]&&!days[5]&&!days[6]);}

    /**
     * Set default settings
     */
    public void setDefault() {
        this.setMelody("default melody");
        this.setRepeat(5);
        this.setVibro(true);
        this.setSound(80);
        this.setTurnOn(true);
        for(int i = 0;i<7;i++) days[i] = false;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "ID=" + ID +
                ", date=" + date +
                ", repeat=" + repeat +
                ", sound=" + sound +
                ", melody='" + melody + '\'' +
                ", vibro=" + vibro +
                ", visible=" + visible +
                ", turnOn=" + turnOn +
                ", days=" + Arrays.toString(days) +
                ", isFacebook=" + isFacebook +
                ", isVkontakte=" + isVkontakte +
                ", isTwitter=" + isTwitter +
                ", isInstagram=" + isInstagram +
                '}';
    }
}
