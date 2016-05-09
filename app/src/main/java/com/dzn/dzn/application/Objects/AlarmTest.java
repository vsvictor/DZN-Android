package com.dzn.dzn.application.Objects;

/**
 * Created by zhenya on 06.05.2016.
 *
 * This class is for tested
 */
public class AlarmTest {

    private String hour;
    private String minute;
    private boolean visibleTime;

    public AlarmTest() {
        this.hour = "00";
        this.minute = "00";
        visibleTime = true;
    }

    public AlarmTest(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
        visibleTime = true;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getTime() {
        return hour + " : " + minute;
    }

    public void setVisibleTime(boolean b) {
        this.visibleTime = b;
    }

    public boolean getVisibleTime() {
        return  this.visibleTime;
    }
}
