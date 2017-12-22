package com.doozy.bikepowermeter.services;

import com.doozy.bikepowermeter.data.Exceptions.RequestNotReadyException;

/**
 * Created by doozy on 19-Dec-17
 */

public interface WeatherService {
    void updateWeather(double lat, double lon);
    double getTemperature() throws RequestNotReadyException;
    double getHumidity() throws RequestNotReadyException;
    double getPressure() throws RequestNotReadyException;
}
