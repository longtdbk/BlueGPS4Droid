package org.broeuschmeul.android.gps.db.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by longtd on 3/22/2018.
 */

public class GpsSqlHelper extends SQLiteOpenHelper {
    public static final String TABLE_LOCATION_NOTE = "location_note";
    public static final String DATABASE_NAME = "bluegps.db";
    private static final int DATABASE_VERSION = 1;

    // table Currency
    public static final String COLUMN_LOC_ID = "id";
    public static final String COLUMN_LOC_LONGITUDE = "longitude";
    public static final String COLUMN_LOC_LATITUDE = "latitude";
    public static final String COLUMN_LOC_NOTE = "note";
    public static final String COLUMN_LOC_TIME = "time";
    public SQLiteDatabase sqLiteDatabase;

    public GpsSqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TABLE_LOC_CREATE = "CREATE TABLE "
            + TABLE_LOCATION_NOTE + "("
            + COLUMN_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LOC_LONGITUDE + " real not null, "
            + COLUMN_LOC_LATITUDE + " real not null, "
            + COLUMN_LOC_NOTE + " text, "
            + COLUMN_LOC_TIME + " DATETIME default current_timestamp "
            + ");";

    @Override
    public void onCreate(SQLiteDatabase database) {
        sqLiteDatabase = database;
        database.execSQL(TABLE_LOC_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /*Log.w(MySQLiteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");*/
        //sqLiteDatabase = db;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_NOTE);
        onCreate(db);
    }
}
