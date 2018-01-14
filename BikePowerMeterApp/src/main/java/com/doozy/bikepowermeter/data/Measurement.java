package com.doozy.bikepowermeter.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Immutable model class for a Measurements.
 */

@Entity
public class Measurement {
    @PrimaryKey(autoGenerate = true)
    long id;

    private double speed;
    private double power;

    public Measurement() {
    }

    @Ignore
    public Measurement(double speed, double power) {
        this.speed = speed;
        this.power = power;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double powers) {
        this.power = powers;
    }
}
