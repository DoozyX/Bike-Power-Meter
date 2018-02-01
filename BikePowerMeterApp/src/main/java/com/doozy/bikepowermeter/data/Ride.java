package com.doozy.bikepowermeter.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Immutable model class for a Bike Rides.
 */

@Entity(tableName = "rides")
public class Ride {
    @PrimaryKey(autoGenerate = true)
    private long id;


    private String mStartDate;

    private String mEndDate;
    private int mDuration;

    private int mAveragePower;
    private int mAverageSpeed;

    @Ignore
    private List<Measurement> mMeasurements;

    @Embedded
    private Weather mWeather;

    public Ride() {
        mMeasurements = new ArrayList<>();
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getAveragePower() {
        return mAveragePower;
    }

    public int getAverageSpeed() {
        return mAverageSpeed;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        this.mWeather = weather;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAveragePower(int averagePower) {
        this.mAveragePower = averagePower;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.mAverageSpeed = averageSpeed;
    }


    public void addMeasurement(Measurement measurement) {
        mMeasurements.add(measurement);
    }

    public void calculateAndSetAverages() {
        int sumPower = 0, sumSpeed = 0, n = mMeasurements.size();
        if (n == 0) {
            mAveragePower = 0;
            mAverageSpeed = 0;
            return;
        }

        for (Measurement m: mMeasurements) {
            sumPower += m.getPower();
            sumSpeed += m.getSpeed();
        }
        mAveragePower = sumPower / n;
        mAverageSpeed = sumSpeed / n;
    }

    @Override
    public String toString() {
        return String.format("Avg. Power  Avg. Speed   Duration \n\t %10s %15s %13s", mAveragePower ,mAverageSpeed, mDuration);
    }
}
