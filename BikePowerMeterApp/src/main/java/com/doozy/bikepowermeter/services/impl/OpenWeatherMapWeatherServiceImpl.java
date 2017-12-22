package com.doozy.bikepowermeter.services.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.doozy.bikepowermeter.RequestQueueSingleton;
import com.doozy.bikepowermeter.data.Exceptions.RequestNotReadyException;
import com.doozy.bikepowermeter.data.Weather;
import com.doozy.bikepowermeter.services.WeatherService;
import org.json.JSONObject;

/**
 * Created by doozy on 19-Dec-17
 */

public class OpenWeatherMapWeatherServiceImpl implements WeatherService {
    private Context context;
    private final String APPID = "1da5fe427b460e0ccab79aad59805209";

    public OpenWeatherMapWeatherServiceImpl(Context context) {
        this.context = context;
    }

    private Weather weather;

    public void updateWeather(double lat, double lon) {
        // Start the queue
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=" + APPID;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    weather = OpenWeatherMapWeatherServiceImpl.parseJason(response.getJSONObject("main"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    @Override
    public double getTemperature() throws RequestNotReadyException {
         if (weather != null) {
            return weather.getTemperature();
        }

        throw new RequestNotReadyException();
    }

    @Override
    public double getHumidity() throws RequestNotReadyException {
        if (weather != null) {
            return weather.getHumidity();
        }

        throw new RequestNotReadyException();
    }

    @Override
    public double getPressure() throws RequestNotReadyException {
        if (weather != null) {
            return weather.getPressure();
        }
        throw new RequestNotReadyException();
    }

    public static  Weather parseJason(JSONObject JSONWeather) throws RequestNotReadyException {
        Weather weather = new Weather();
        try {
            weather.setTemperature(JSONWeather.getDouble("temp"));
            weather.setPressure(JSONWeather.getDouble("pressure"));
            weather.setHumidity(JSONWeather.getDouble("humidity"));
            return weather;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestNotReadyException();
        }
    }
}
