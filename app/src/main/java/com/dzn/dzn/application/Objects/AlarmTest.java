package com.dzn.dzn.application.Objects;

/**
 * Created by zhenya on 06.05.2016.
 *
 * This class is for tested
 */
public class AlarmTest {
    String hour;
    String minute;

    public AlarmTest() {
        this.hour = "00";
        this.minute = "00";
    }

    public AlarmTest(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }
}
