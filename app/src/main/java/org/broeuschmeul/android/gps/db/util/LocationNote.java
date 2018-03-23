package org.broeuschmeul.android.gps.db.util;

/**
 * Created by longtd on 3/22/2018.
 */

public class LocationNote {
    private long ID;
    private double longitude;
    private double latitude;
    private String time;
    private String note;

    public LocationNote(){

    }

    public LocationNote(String time, double latitude, double longitude, String note){
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lattitude) {
        this.latitude = lattitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
