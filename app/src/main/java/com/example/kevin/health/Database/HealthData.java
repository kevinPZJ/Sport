package com.example.kevin.health.Database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 2017/4/17.
 */

public class HealthData implements Parcelable{

    private int userId;

    private int  step;

    private int  cal;

    private double distance;

    private String creatDate;

    private double sleepHour;

    private int sitHour;

    public HealthData() {
    }

    public HealthData(int userId, int step, int cal, double distance, String creatDate, double sleepHour, int sitHour) {
        this.userId = userId;
        this.step = step;
        this.cal = cal;
        this.distance = distance;
        this.creatDate = creatDate;
        this.sleepHour = sleepHour;
        this.sitHour = sitHour;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }

    public double getSleepHour() {
        return sleepHour;
    }

    public void setSleepHour(double sleepHour) {
        this.sleepHour = sleepHour;
    }

    public int getSitHour() {
        return sitHour;
    }

    public void setSitHour(int sitHour) {
        this.sitHour = sitHour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.step);
        dest.writeInt(this.cal);
        dest.writeDouble(this.distance);
        dest.writeString(this.creatDate);
        dest.writeDouble(this.sleepHour);
        dest.writeInt(this.sitHour);
    }

    protected HealthData(Parcel in) {
        this.userId = in.readInt();
        this.step = in.readInt();
        this.cal = in.readInt();
        this.distance = in.readDouble();
        this.creatDate = in.readString();
        this.sleepHour = in.readDouble();
        this.sitHour = in.readInt();
    }

    public static final Creator<HealthData> CREATOR = new Creator<HealthData>() {
        @Override
        public HealthData createFromParcel(Parcel source) {
            return new HealthData(source);
        }

        @Override
        public HealthData[] newArray(int size) {
            return new HealthData[size];
        }
    };
}
