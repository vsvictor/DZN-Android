package com.dzn.dzn.application.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by victor on 11.05.16.
 */
public class DateTimeOperator {
    public static String dateToTimeString(Date aDate) {
        String aFormat = "HH:mm";
        if (aDate == null)
            return "";
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        String stringDate = simpledateformat.format(aDate);
        return stringDate;
    }
}
