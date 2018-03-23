package org.broeuschmeul.android.gps.db.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static org.broeuschmeul.android.gps.db.util.GpsSqlHelper.COLUMN_LOC_TIME;

/**
 * Created by longtd on 3/22/2018.
 */

public class LocNoteController {
    private SQLiteDatabase database;
    private GpsSqlHelper dbHelper;
    private String [] allColumns = {
            GpsSqlHelper.COLUMN_LOC_ID,
            COLUMN_LOC_TIME,
            GpsSqlHelper.COLUMN_LOC_LONGITUDE,
            GpsSqlHelper.COLUMN_LOC_LATITUDE,
            GpsSqlHelper.COLUMN_LOC_NOTE
    };
    public LocNoteController(Context context) {
        dbHelper = new GpsSqlHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    // createNetwork
    public LocationNote createLocationNote(double longitude, double latitude,String note ) {
        ContentValues values = new ContentValues();

        values.put(GpsSqlHelper.COLUMN_LOC_LONGITUDE, longitude);
        values.put(GpsSqlHelper.COLUMN_LOC_LATITUDE, latitude);
        values.put(GpsSqlHelper.COLUMN_LOC_NOTE, note);

        long newRowID  = database.insert(GpsSqlHelper.TABLE_LOCATION_NOTE, null,
                    values);
        String selectQuery = GpsSqlHelper.COLUMN_LOC_ID + " = " + newRowID ;
        Cursor cursor = database.query(GpsSqlHelper.TABLE_LOCATION_NOTE,
                    allColumns, selectQuery, null,
                    null, null, null);
        //}else {
        //    database.update(GpsSqlHelper.TABLE_NETWORK, values, selectQuery, null);
        //}

        cursor.moveToFirst();
        LocationNote newLocationNote = cursorToLocNote(cursor);
        cursor.close();
        return newLocationNote;
    }

    public String updateLocationNote(long rowID, double longitude, double latitude,String note){
        String selectQuery = GpsSqlHelper.COLUMN_LOC_ID + " = " + rowID ;

        ContentValues values = new ContentValues();
        values.put(GpsSqlHelper.COLUMN_LOC_LONGITUDE, longitude);
        values.put(GpsSqlHelper.COLUMN_LOC_LATITUDE, latitude);
        values.put(GpsSqlHelper.COLUMN_LOC_NOTE, note);

        String result = "Updated Successfully";
        try {
            database.update(GpsSqlHelper.TABLE_LOCATION_NOTE, values, selectQuery, null);
        }catch(Exception ex){
            result = "Error on Update, Please Check";
        }
        return result;
    }

    public String updateLocationNote(long rowID,String note){
        String selectQuery = GpsSqlHelper.COLUMN_LOC_ID + " = " + rowID ;

        ContentValues values = new ContentValues();
        values.put(GpsSqlHelper.COLUMN_LOC_NOTE, note);
        String result = "Updated Successfully";
        try {
            database.update(GpsSqlHelper.TABLE_LOCATION_NOTE, values, selectQuery, null);
        }catch(Exception ex){
            result = "Error on Update, Please Check";
        }
        return result;

    }


    public LocationNote createLocationNote(LocationNote locNote){
        LocationNote locationNote = new LocationNote();
        try {
            locationNote = createLocationNote(locNote.getLongitude(), locNote.getLatitude(), locNote.getNote());
        }catch(Exception ex){

        }
        return locationNote;
    }

    public String deleteLocationNote(LocationNote locNote) {
        long id = locNote.getID();
        String result = "Deleted Successfully";
        try {
            database.delete(GpsSqlHelper.TABLE_LOCATION_NOTE, GpsSqlHelper.COLUMN_LOC_ID
                    + " = " + id, null);
        }catch(Exception ex){
            result = "Error on Delete, Please Check";
        }
        return result;
    }

    public String deleteLocationNote(long id) {
        //System.out.println("Network deleted with id: " + id);
        String result = "Delete Successfully";
        try {
            database.delete(GpsSqlHelper.TABLE_LOCATION_NOTE, GpsSqlHelper.COLUMN_LOC_ID
                    + " = " + id, null);
        }catch(Exception ex){
            result = "Error on Delete, Please Check";
        }
        return result;
    }

    public List<LocationNote> getAllLocationNotes() {
        List<LocationNote> locNotes = new ArrayList<LocationNote>();

        Cursor cursor = database.query(GpsSqlHelper.TABLE_LOCATION_NOTE,
                allColumns, null, null, null, null, COLUMN_LOC_TIME +" DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationNote locNote = cursorToLocNote(cursor);
            locNotes.add(locNote);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return locNotes;
    }

    private LocationNote cursorToLocNote(Cursor cursor) {
        LocationNote locationNote = new LocationNote();
        locationNote.setID(cursor.getInt(1));
        locationNote.setTime(cursor.getString(2));
        locationNote.setLongitude(cursor.getDouble(3));
        locationNote.setLatitude(cursor.getDouble(4));
        locationNote.setNote(cursor.getString(5));
        return locationNote;

    }
}
