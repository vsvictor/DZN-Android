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
                + COL_VIBRO + "INTEGER NOT NULL DEFAULT 1, "
                + COL_TURN_ON + "INTEGER NOT NULL DEFAULT 1)";

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
        db.execSQL(CREATE_TBL_ALARMS);
        db.execSQL(CREATE_TBL_PUBLIC);
        db.execSQL(CREATE_TBL_SOCIAL);

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
//        contentValues.put(COL_VIBRO, alarm.isVibro()?1:0);
        contentValues.put(COL_TURN_ON, alarm.isTurnOn() ? 1 : 0);

        //db.insert(TBL_ALARMS, null, contentValues);
        db.insertWithOnConflict(TBL_ALARMS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public void updateAlarm(Alarm alarm) {
        String[] args = {String.valueOf(alarm.getID())};
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TIME, alarm.getDate().getTime());
        contentValues.put(COL_REPEAT, alarm.getRepeat());
        contentValues.put(COL_SOUND, alarm.getSound());
        contentValues.put(COL_MELODY, alarm.getMelody());
//        contentValues.put(COL_VIBRO, alarm.isVibro()?1:0);
        contentValues.put(COL_TURN_ON, alarm.isTurnOn() ? 1 : 0);

        //db.insert(TBL_ALARMS, null, contentValues);
        db.update(TBL_ALARMS, contentValues, COL_ID + "=?", args);

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
                if (cursor.getInt(cursor.getColumnIndex(COL_TURN_ON)) == 1) {
                    alarm.setTurnOn(true);
                } else {
                    alarm.setTurnOn(false);
                }
                list.add(alarm);
            } while (cursor.moveToNext());
        }

        db.close();
        return list;
    }

    public synchronized Alarm getAlarm(int id) {
        Alarm alarm = null;
        //String strQuery = "SELECT * FROM " + TBL_ALARMS;
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
            if (cursor.getInt(cursor.getColumnIndex(COL_TURN_ON)) == 1) {
                alarm.setTurnOn(true);
            } else {
                alarm.setTurnOn(false);
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
