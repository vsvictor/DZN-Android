package com.dzn.dzn.application.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhenya on 09.05.2016.
 */
public class TimeHelper {
    private static final String TAG = "TimeHelper";

    public static Calendar calendar = Calendar.getInstance();

    public static int hourOfDay(Date timestamp) {
        calendar.setTime(timestamp);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int month(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int day(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}
