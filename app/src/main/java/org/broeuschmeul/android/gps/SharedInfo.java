package org.broeuschmeul.android.gps;

import org.broeuschmeul.android.gps.ui.MainActivity;

/**
 * Created by admin on 3/21/2018.
 */

public  class SharedInfo {
    public SharedInfo(){
        if (self == null){
            self = this;
        }
    }

    public static SharedInfo getSelf(){
        if (self == null){
            self = new SharedInfo();
        }
        return self;
    }


    private static SharedInfo self;
    public static MainActivity mainActivity;
    private double longitude;
    private double lattitude;
    private double altitude;
    private double speed;
    private double accuracy;
    private int satInUse;
    private int satInView;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getSatInUse() {
        return satInUse;
    }

    public void setSatInUse(int satInUse) {
        this.satInUse = satInUse;
    }

    public int getSatInView() {
        return satInView;
    }

    public void setSatInView(int satInView) {
        this.satInView = satInView;
    }
}
