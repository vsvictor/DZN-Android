package com.dzn.dzn.application.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.AlarmTest;
import com.dzn.dzn.application.Objects.Social;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhenya on 03.05.2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "dzndzn";
    private static DataBaseHelper mInstance = null;
    private Context context;

    //Table Alarms
    private static final String TBL_ALARMS = "Alarms";
    private static final String COL_ID = "ID";
    private static final String COL_TIME = "time";
    private static final String COL_REPEAT = "repeat";
    private static final String COL_SOUND = "sound";
    private static final String COL_MELODY = "melody";
    private static final String COL_VIBRO = "vibro";
    private static final String COL_TURN_ON = "turnon";
    private static final String COL_FB = "facebook";
    private static final String COL_VK = "vkontakte";
    private static final String COL_TW = "twitter";
    private static final String COL_IS = "instagram";

    //Table Public
    private static final String TBL_PUBLIC = "public";
    private static final String COL_OWNER = "owner";
    private static final String COL_SOCIAL = "social";

    //Table Social
    private static final String TBL_SOCIAL = "social";
    private static final String COL_NAME = "name";
    private static final String COL_SHORT_NAME = "shortName";
    private static final String COL_LOGIN = "login";
    private static final String COL_PASSWORD = "password";

    private static final String TBL_DAYS = "days";
    private static final String COL_ALARM_ID = "alarm_id";
    private static final String COL_1 = "day1";
    private static final String COL_2 = "day2";
    private static final String COL_3 = "day3";
    private static final String COL_4 = "day4";
    private static final String COL_5 = "day5";
    private static final String COL_6 = "day6";
    private static final String COL_7 = "day7";

    //String Social Network name
    private static final String FB = "FB";
    private static final String VK = "VK";
    private static final String TW = "TW";
    private static final String IS = "IS";

    private static final String FB_NAME = "Facebook";
    private static final String VK_NAME = "VKontakte";
    private static final String TW_NAME = "Twitter";
    private static final String IS_NAME = "Instagramm";

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    public static DataBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TBL_ALARMS = "CREATE TABLE " + TBL_ALARMS
                + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_TIME + " INTEGER NOT NULL, "
                + COL_REPEAT + " INTEGER NOT NULL DEFAULT 5, "
                + COL_SOUND + " INTEGER NOT NULL DEFAULT 80, "
                + COL_MELODY + " TEXT NOT NULL, "
                + COL_VIBRO + " INTEGER NOT NULL DEFAULT 1, "
                + COL_FB + " INTEGER NOT NULL DEFAULT 0, "
                + COL_VK + " INTEGER NOT NULL DEFAULT 0, "
                + COL_TW + " INTEGER NOT NULL DEFAULT 0, "
                + COL_IS + " INTEGER NOT NULL DEFAULT 0, "
                + COL_TURN_ON + " INTEGER NOT NULL DEFAULT 1)";

        String CREATE_TBL_PUBLIC = "CREATE TABLE " + TBL_PUBLIC
                + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_OWNER + " INTEGER NOT NULL, "
                + COL_SOCIAL + " INTEGER NOT NULL)";

        String CREATE_TBL_SOCIAL = "CREATE TABLE " + TBL_SOCIAL
                + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_SHORT_NAME + " TEXT NOT NULL, "
                + COL_LOGIN + " TEXT, "
                + COL_PASSWORD + " TEXT)";

        String CREATE_TBL_DAYS = "CREATE TABLE " + TBL_DAYS
                + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_ALARM_ID + " INTEGER NOT NULL, "
                + COL_1 + " INTEGER NOT NULL, "
                + COL_2 + " INTEGER NOT NULL, "
                + COL_3 + " INTEGER NOT NULL, "
                + COL_4 + " INTEGER NOT NULL, "
                + COL_5 + " INTEGER NOT NULL, "
                + COL_6 + " INTEGER NOT NULL, "
                + COL_7 + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TBL_ALARMS);
        db.execSQL(CREATE_TBL_PUBLIC);
        db.execSQL(CREATE_TBL_SOCIAL);
        db.execSQL(CREATE_TBL_DAYS);

        //Add social content
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ", " + COL_SHORT_NAME + ") VALUES ('" + FB_NAME + "', '" + FB + "')");
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ", " + COL_SHORT_NAME + ") VALUES ('" + TW_NAME + "', '" + TW + "')");
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ", " + COL_SHORT_NAME + ") VALUES ('" + VK_NAME + "', '" + VK + "')");
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ", " + COL_SHORT_NAME + ") VALUES ('" + IS_NAME + "', '" + IS + "')");

        Log.d(TAG, "onCreate finish");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ALARMS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PUBLIC);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_SOCIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_DAYS);
        onCreate(db);
    }

    /**
     * Add to table Alarms
     *
     * @param alarm
     */
    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (alarm.getID() != 0) {
            contentValues.put(COL_ID, alarm.getID());
        }
        contentValues.put(COL_TIME, alarm.getDate().getTime());
        contentValues.put(COL_REPEAT, alarm.getRepeat());
        contentValues.put(COL_SOUND, alarm.getSound());
        contentValues.put(COL_MELODY, alarm.getMelody());

        contentValues.put(COL_VIBRO, alarm.isVibro() ? 1 : 0);
        contentValues.put(COL_FB, alarm.isFacebook() ? 1 : 0);
        contentValues.put(COL_VK, alarm.isVkontakte() ? 1 : 0);
        contentValues.put(COL_TW, alarm.isTwitter() ? 1 : 0);
        contentValues.put(COL_IS, alarm.isInstagram() ? 1 : 0);
        contentValues.put(COL_TURN_ON, alarm.isTurnOn() ? 1 : 0);

        //db.insert(TBL_ALARMS, null, contentValues);
        db.insertWithOnConflict(TBL_ALARMS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        ContentValues days = new ContentValues();
        days.put(COL_ALARM_ID, alarm.getID());
        days.put(COL_1, alarm.isDayOn(0)?1:0);
        days.put(COL_2, alarm.isDayOn(1)?1:0);
        days.put(COL_3, alarm.isDayOn(2)?1:0);
        days.put(COL_4, alarm.isDayOn(3)?1:0);
        days.put(COL_5, alarm.isDayOn(4)?1:0);
        days.put(COL_6, alarm.isDayOn(5)?1:0);
        days.put(COL_7, alarm.isDayOn(6)?1:0);
        db.insertWithOnConflict(TBL_DAYS, null, days, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public void updateAlarm(Alarm alarm) {
        String[] args = {String.valueOf(alarm.getID())};
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long l = -1;

        contentValues.put(COL_TIME, alarm.getDate().getTime());
        contentValues.put(COL_REPEAT, alarm.getRepeat());
        contentValues.put(COL_SOUND, alarm.getSound());
        contentValues.put(COL_MELODY, alarm.getMelody());
        contentValues.put(COL_VIBRO, alarm.isVibro() ? 1 : 0);
        contentValues.put(COL_FB, alarm.isFacebook() ? 1 : 0);
        contentValues.put(COL_VK, alarm.isVkontakte() ? 1 : 0);
        contentValues.put(COL_TW, alarm.isTwitter() ? 1 : 0);
        contentValues.put(COL_IS, alarm.isInstagram() ? 1 : 0);
        contentValues.put(COL_TURN_ON, alarm.isTurnOn() ? 1 : 0);

        //db.insert(TBL_ALARMS, null, contentValues);
        db.update(TBL_ALARMS, contentValues, COL_ID + "=?", args);

        ContentValues days = new ContentValues();
        days.put(COL_ALARM_ID, alarm.getID());
        days.put(COL_1, alarm.isDayOn(0)?1:0);
        days.put(COL_2, alarm.isDayOn(1)?1:0);
        days.put(COL_3, alarm.isDayOn(2)?1:0);
        days.put(COL_4, alarm.isDayOn(3)?1:0);
        days.put(COL_5, alarm.isDayOn(4)?1:0);
        days.put(COL_6, alarm.isDayOn(5)?1:0);
        days.put(COL_7, alarm.isDayOn(6)?1:0);
        int count = db.update(TBL_DAYS, days, COL_ALARM_ID + "=?", args);
        if(count == 0) {
            l = db.insert(TBL_DAYS,null,days);
        }

        db.close();
    }

    /**
     * Remove Alarm
     *
     * @param alarm
     */
    public void removeAlarm(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String[] args = {String.valueOf(alarm.getID())};
            db.delete(TBL_ALARMS, COL_ID + "=?", args);
            db.delete(TBL_DAYS, COL_ALARM_ID + "=?", args);
        } finally {
            db.close();
        }
    }

    /**
     * Add to table Public
     *
     * @param owner
     * @param social
     * @the table
     */
    public void addPublic(int owner, int social) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_OWNER, owner);
        contentValues.put(COL_SOCIAL, social);

        db.insert(TBL_PUBLIC, null, contentValues);

        db.close();
    }

    /**
     * Return list of alarms
     *
     * @return
     */
    public synchronized ArrayList<Alarm> getAlarmList() {
        ArrayList<Alarm> list = new ArrayList<Alarm>();
        //String strQuery = "SELECT * FROM " + TBL_ALARMS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TBL_ALARMS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setID(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                alarm.setTime(cursor.getLong(cursor.getColumnIndex(COL_TIME)));
                alarm.setRepeat(cursor.getInt(cursor.getColumnIndex(COL_REPEAT)));
                alarm.setSound(cursor.getInt(cursor.getColumnIndex(COL_SOUND)));
                alarm.setMelody(cursor.getString(cursor.getColumnIndex(COL_MELODY)));
                alarm.setVibro(true);

                alarm.setTurnOn(cursor.getInt(cursor.getColumnIndex(COL_TURN_ON)) == 1);
                alarm.setFacebook(cursor.getInt(cursor.getColumnIndex(COL_FB)) == 1);
                alarm.setVkontakte(cursor.getInt(cursor.getColumnIndex(COL_VK)) == 1);
                alarm.setTwitter(cursor.getInt(cursor.getColumnIndex(COL_TW)) == 1);
                alarm.setInstagram(cursor.getInt(cursor.getColumnIndex(COL_IS)) == 1);

                String[] args = {String.valueOf(alarm.getID())};
                Cursor dd = db.query(TBL_DAYS,null,COL_ALARM_ID+"=?",args,null,null,null);
                if(dd.moveToFirst()){
                    alarm.setDay(0,dd.getInt(dd.getColumnIndex(COL_1))==1);
                    alarm.setDay(1,dd.getInt(dd.getColumnIndex(COL_2))==1);
                    alarm.setDay(2,dd.getInt(dd.getColumnIndex(COL_3))==1);
                    alarm.setDay(3,dd.getInt(dd.getColumnIndex(COL_4))==1);
                    alarm.setDay(4,dd.getInt(dd.getColumnIndex(COL_5))==1);
                    alarm.setDay(5,dd.getInt(dd.getColumnIndex(COL_6))==1);
                    alarm.setDay(6,dd.getInt(dd.getColumnIndex(COL_7))==1);
                }

                list.add(alarm);
            } while (cursor.moveToNext());
        }

        db.close();
        return list;
    }

    /**
     * Return list of alarms where filter set to turnOn property
     * @param turnOn
     * @return
     */
    public synchronized ArrayList<Alarm> getAlarmList(boolean turnOn) {
        ArrayList<Alarm> list = this.getAlarmList();

        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Alarm alarm = (Alarm) iterator.next();
            if (!alarm.isTurnOn()) {
                iterator.remove();
            }
        }

        return list;
    }

    public synchronized Alarm getAlarm(int id) {
        Alarm alarm = null;
        SQLiteDatabase db = getReadableDatabase();
        String[] args = {String.valueOf(id)};

        Cursor cursor = db.query(TBL_ALARMS, null, COL_ID + "=?", args, null, null, null);
        if (cursor.moveToFirst()) {
            alarm = new Alarm();
            alarm.setID(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            alarm.setTime(cursor.getLong(cursor.getColumnIndex(COL_TIME)));
            alarm.setRepeat(cursor.getInt(cursor.getColumnIndex(COL_REPEAT)));
            alarm.setSound(cursor.getInt(cursor.getColumnIndex(COL_SOUND)));
            alarm.setMelody(cursor.getString(cursor.getColumnIndex(COL_MELODY)));
            alarm.setVibro(true);

            alarm.setTurnOn(cursor.getInt(cursor.getColumnIndex(COL_TURN_ON)) == 1);
            alarm.setFacebook(cursor.getInt(cursor.getColumnIndex(COL_FB)) == 1);
            alarm.setVkontakte(cursor.getInt(cursor.getColumnIndex(COL_VK)) == 1);
            alarm.setTwitter(cursor.getInt(cursor.getColumnIndex(COL_TW)) == 1);
            alarm.setInstagram(cursor.getInt(cursor.getColumnIndex(COL_IS)) == 1);

            Cursor dd = db.query(TBL_DAYS,null,COL_ALARM_ID+"=?",args,null,null,null);
            if(dd.moveToFirst()){
                alarm.setDay(0,dd.getInt(dd.getColumnIndex(COL_1))==1);
                alarm.setDay(1,dd.getInt(dd.getColumnIndex(COL_2))==1);
                alarm.setDay(2,dd.getInt(dd.getColumnIndex(COL_3))==1);
                alarm.setDay(3,dd.getInt(dd.getColumnIndex(COL_4))==1);
                alarm.setDay(4,dd.getInt(dd.getColumnIndex(COL_5))==1);
                alarm.setDay(5,dd.getInt(dd.getColumnIndex(COL_6))==1);
                alarm.setDay(6,dd.getInt(dd.getColumnIndex(COL_7))==1);
            }
        }

        db.close();
        return alarm;
    }

    public synchronized ArrayList<Social> getSocial() {
        ArrayList<Social> list = new ArrayList<Social>();
        String strQuery = "SELECT * FROM " + TBL_SOCIAL;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Social social = new Social();
                social.setID(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                social.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                social.setShortName(cursor.getString(cursor.getColumnIndex(COL_SHORT_NAME)));
                social.setLogin(cursor.getString(cursor.getColumnIndex(COL_LOGIN)));
                social.setPassword(cursor.getString(cursor.getColumnIndex(COL_PASSWORD)));
                list.add(social);
            } while (cursor.moveToNext());
        }

        db.close();
        return list;
    }

    public synchronized void setSocial(ArrayList<Social> list) {
        SQLiteDatabase db = getReadableDatabase();
        for (Social social : list) {
            ContentValues val = new ContentValues();
            if (social.getName() != null) val.put(COL_NAME, social.getName());
            if (social.getShortName() != null) val.put(COL_SHORT_NAME, social.getShortName());
            if (social.getLogin() != null) val.put(COL_LOGIN, social.getLogin());
            if (social.getPassword() != null) val.put(COL_PASSWORD, social.getPassword());
            if (val.size() > 0) {
                String[] args = {String.valueOf(social.getID())};
                db.update(TBL_SOCIAL, val, "ID=?", args);
            }
        }

        db.close();
    }
}
