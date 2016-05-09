package com.dzn.dzn.application.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.AlarmTest;

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

    //Table Public
    private static final String TBL_PUBLIC = "public";
    private static final String COL_OWNER = "owner";
    private static final String COL_SOCIAL = "social";

    //Table Social
    private static final String TBL_SOCIAL = "social";
    private static final String COL_NAME = "name";

    //String Social Network name
    private static final String FB = "FB";
    private static final String VK = "VK";
    private static final String TW = "TW";
    private static final String IS = "IS";

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
                + COL_VIBRO + "INTEGER NOT NULL DEFAULT 1)";

        String CREATE_TBL_PUBLIC = "CREATE TABLE " + TBL_PUBLIC
                + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_OWNER + " INTEGER NOT NULL, "
                + COL_SOCIAL + " INTEGER NOT NULL)";

        String CREATE_TBL_SOCIAL = "CREATE TABLE " + TBL_SOCIAL
                + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_NAME + " TEXT NOT NULL)";

        db.execSQL(CREATE_TBL_ALARMS);
        db.execSQL(CREATE_TBL_PUBLIC);
        db.execSQL(CREATE_TBL_SOCIAL);

        //Add social content
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ") VALUES ('" + FB + "')");
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ") VALUES ('" + VK + "')");
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ") VALUES ('" + TW + "')");
        db.execSQL("INSERT INTO " + TBL_SOCIAL + "(" + COL_NAME + ") VALUES ('" + IS + "')");

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
     * @param alarm
     */
    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TIME, alarm.getDate().getTime());
        contentValues.put(COL_REPEAT, alarm.getRepeat());
        contentValues.put(COL_SOUND, alarm.getSound());
        contentValues.put(COL_MELODY, alarm.getMelody());
        contentValues.put(COL_VIBRO, alarm.isVibro());

        db.insert(TBL_ALARMS, null, contentValues);
    }

    /**
     * Add to table Public
     * @param owner
     * @param social
     */
    public void addPublic(int owner, int social) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_OWNER, owner);
        contentValues.put(COL_SOCIAL, social);

        db.insert(TBL_PUBLIC, null, contentValues);
    }

    /**
     * Return list of alarms
     * @return
     */
    public ArrayList<Alarm> getAlarmList() {
        ArrayList<Alarm> list = new ArrayList<>();
        String strQuery = "SELECT * FROM " + TBL_ALARMS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(strQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setID(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                alarm.setTime(cursor.getInt(cursor.getColumnIndex(COL_TIME)));
                alarm.setRepeat(cursor.getInt(cursor.getColumnIndex(COL_REPEAT)));
                alarm.setSound(cursor.getInt(cursor.getColumnIndex(COL_SOUND)));
                alarm.setMelody(cursor.getString(cursor.getColumnIndex(COL_MELODY)));
                alarm.setVibro(cursor.getInt(cursor.getColumnIndex(COL_VIBRO)));
                list.add(alarm);
            } while (cursor.moveToNext());
        }

        return list;
    }

}
