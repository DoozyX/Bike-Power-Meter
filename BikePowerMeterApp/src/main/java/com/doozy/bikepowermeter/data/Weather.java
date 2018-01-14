package com.doozy.bikepowermeter.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Immutable model class for a Weather.
 */

@Entity(tableName = "weather")
public class Weather {



    @PrimaryKey(autoGenerate = true)
    private long wid;

    long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    private double temperature;

    private double humidity;

    private double pressure;

    public void setDewPoint(double dewPoint) {
        this.dewPoint = dewPoint;
    }

    double getAirDensity() {
        return airDensity;
    }

    public void setAirDensity(double airDensity) {
        this.airDensity = airDensity;
    }

    private double dewPoint;

    private double airDensity;


    /**
     * Use this constructor to create a new Weather object.
     *
     * @param temperature       title of the task
     * @param humidity          description of the task
     * @param pressure          pressure
     */
    @Ignore
    public Weather(double temperature, double humidity, double pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        dewPoint = -1;
        airDensity = -1;
    }

    public Weather() {
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getRho() {
        if (airDensity == -1) {
            calculateRho();
        }
        return airDensity;
    }

    double getDewPoint() {
        if (dewPoint == -1) {
            calculateDewPoint();
        }
        return dewPoint;
    }

    public void calculateRho() {
        if (dewPoint == -1) {
            calculateDewPoint();
        }
        // Step 1: calculate the saturation water pressure at the dew point,
        // i.e., the vapor pressure in the air.  Use Herman Wobus' equation.
        double c0 = 0.99999683;
        double c1 = -0.90826951E-02;
        double c2 = 0.78736169E-04;
        double c3 = -0.61117958E-06;
        double c4 = 0.43884187E-08;
        double c5 = -0.29883885E-10;
        double c6 = 0.21874425E-12;
        double c7 = -0.17892321E-14;
        double c8 = 0.11112018E-16;
        double c9 = -0.30994571E-19;
        double p = c0 + dewPoint*(c1 + dewPoint*(c2 + dewPoint*(c3 + dewPoint*(c4 + dewPoint*(c5 + dewPoint*(c6 + dewPoint*(c7 + dewPoint*(c8 + dewPoint*(c9)))))))));
        double pSatMBar = 6.1078 / (Math.pow(p, 8));

        // Step 2: calculate the vapor pressure.
        double pvPascals = pSatMBar * 100.0;

        // Step 3: calculate the pressure of dry air, given the vapor
        // pressure and actual pressure.
        double pdPascals = (pressure*100) - pvPascals;

        // Step 4: calculate the air density, using the equation for
        // the density of a mixture of dry air and water vapor.
        airDensity = (pdPascals / (287.0531 * (temperature + 273.15))) +
                (pvPascals / (461.4964 * (temperature + 273.15)));
    }

    private void calculateDewPoint() {
        dewPoint = temperature - ((100 - humidity) / 5);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", dewPoint=" + dewPoint +
                ", airDensity=" + airDensity +
                '}';
    }
}
