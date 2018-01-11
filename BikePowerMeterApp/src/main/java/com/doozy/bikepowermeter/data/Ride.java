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

@Entity
public class Ride {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @Embedded
    private Date startDate;
    @Embedded
    private Date endDate;
    private int duration;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAveragePower(int averagePower) {
        this.averagePower = averagePower;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    private int averagePower;
    private int averageSpeed;

    @Ignore //TODO::Change
    private List<Measurement> measurements;

    @Embedded
    private Weather weather;

    public Ride() {
        measurements = new ArrayList<>();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAveragePower() {
        return averagePower;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }

    public void calculateAndSetAverages() {
        int sumPower = 0, sumSpeed = 0, n = measurements.size();
        for (Measurement m: measurements) {
            sumPower += m.getPower();
            sumSpeed += m.getSpeed();
        }
        averagePower = sumPower / n;
        averageSpeed = sumSpeed / n;
    }
}
