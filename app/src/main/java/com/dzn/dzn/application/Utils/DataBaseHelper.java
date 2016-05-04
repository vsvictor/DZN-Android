package com.dzn.dzn.application.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhenya on 03.05.2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "dataBaseHelper";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ALARMS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PUBLIC);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_SOCIAL);
        onCreate(db);
    }
}
