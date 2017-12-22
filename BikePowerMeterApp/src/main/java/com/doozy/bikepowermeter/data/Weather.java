package com.doozy.bikepowermeter.data;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by doozy on 20-Dec-17
 */

public class Weather {
    private double temperature;
    private double humidity;
    private double pressure;
    private double dewPoint;
    private double airDensity;

    public Weather(double temperature, double humidity, double pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
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
        calculateRho();
        return airDensity;
    }

    public double getDewPoint() {
        calculateDewPoint();
        return dewPoint;
    }

    public void calculateRho() {
        calculateDewPoint();
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
        double psat_mbar = 6.1078 / (Math.pow(p, 8));

        // Step 2: calculate the vapor pressure.
        double pv_pascals = psat_mbar * 100.0;

        // Step 3: calculate the pressure of dry air, given the vapor
        // pressure and actual pressure.
        double pd_pascals = (pressure*100) - pv_pascals;

        // Step 4: calculate the air density, using the equation for
        // the density of a mixture of dry air and water vapor.
        airDensity = (pd_pascals / (287.0531 * (temperature + 273.15))) +
                (pv_pascals / (461.4964 * (temperature + 273.15)));
    }

    public void calculateDewPoint() {
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
