package com.doozy.bikepowermeter.home;

import android.content.SharedPreferences;
import android.icu.util.Measure;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.widget.Chronometer;

import com.doozy.bikepowermeter.data.AppDatabase;
import com.doozy.bikepowermeter.data.Measurement;
import com.doozy.bikepowermeter.data.Ride;
import com.doozy.bikepowermeter.data.Weather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * TODO: Description
 */

public class HomePresenter implements HomeContract.Presenter {
    private SharedPreferences mSharedPreferences;
    private HomeContract.View mHomeView;
    private AppDatabase mDatabase;

    private Chronometer mChronometer;
    private long timeWhenStopped = 0;
    private List<Integer> speeds;
    private HomeContract.Position mPosition;
    private Ride mRide;
    private Measurement mMeasurement;
    private Weather mWeather;
    private boolean paused = false;
    private SharedPreferences.Editor editor;

    public HomePresenter(@NonNull HomeContract.View homeView, SharedPreferences sharedPreferences, AppDatabase database) {
        mSharedPreferences = sharedPreferences;
        mHomeView = homeView;
        mMeasurement = new Measurement();
        speeds = new ArrayList<Integer>();
        mChronometer= mHomeView.getChmDuration();
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chronometerTick();
            }
        });
        mWeather = new Weather(); //TODO: Delete
        mRide=new Ride();
        mDatabase = database;
        mHomeView.setPresenter(this);
        editor = mSharedPreferences.edit();

    }

    private void chronometerTick() {
        mHomeView.setArcPower((int) Math.abs((Math.sin(((int) (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000)) * 13)));


        String allSpeeds = mSharedPreferences.getString(mRide.getId()+"","0");
        String[] parts = allSpeeds.split(" ");
        int currentSpeed=Integer.parseInt(parts[parts.length]);
        mHomeView.setArcSpeed(currentSpeed);
        mMeasurement.setSpeed(currentSpeed);
    }
    public void start() {

    }

    @Override
    public void setPosition(HomeContract.Position position) {
        if (position == HomeContract.Position.RELAXED) {
            mPosition = HomeContract.Position.RELAXED;
        } else if (position == HomeContract.Position.AGGRESSIVE) {
            mPosition = HomeContract.Position.AGGRESSIVE;
        } else  {
            mPosition = HomeContract.Position.AERODYNAMIC;
        }
    }

    @Override
    public void startRide() {
        mHomeView.showPauseStopLayout();
        mHomeView.hideStartButton();

        mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        mChronometer.start();

        mRide = new Ride();
        mRide.setStartDate(Calendar.getInstance().getTime());
        editor.putString("thisRide",mRide.getId()+"");
        editor.putString(mRide.getId()+"canWrite","true");
        editor.apply();


    }

    @Override
    public void pauseOrContinueRide() {
        if (paused) {
            mHomeView.setContinueButton();
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            mChronometer.start();
            paused = false;
        } else {
            mHomeView.setPauseButton();
            timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
            paused = true;
        }

    }

    @Override
    public void stopRide() {

        mHomeView.hidePauseStopLayout();
        mHomeView.showStartButton();
//        mRide.calculateAndSetAverages(); TODO::Fix when no measurements
        mRide.setEndDate(Calendar.getInstance().getTime());
        mRide.setWeather(mWeather);

        mDatabase.rideDao().insertRide(mRide);

        mChronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        mChronometer.stop();


        editor.remove(mRide.getId()+"canWrite");
        editor.putString(mRide.getId()+"canWrite","false");
        String tmp = mSharedPreferences.getString("thisRide","0");
        String[] parts = tmp.split(" ");
        for (String s : parts) {
            speeds.add(Integer.parseInt(s));
        }
        editor.remove("thisRide");
        editor.apply();

    }
}
