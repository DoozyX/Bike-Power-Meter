package com.doozy.bikepowermeter.home;

import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.widget.Chronometer;

import java.util.Date;
import java.util.Objects;

/**
 * TODO: Description
 */

public class HomePresenter implements HomeContract.Presenter {
    private SharedPreferences mSharedPreferences;
    private HomeContract.View mHomeView;

    private Chronometer mChronometer;
    private long timeWhenStopped = 0;
    private HomeContract.Position mPosition;

    private boolean paused = false;

    public HomePresenter(@NonNull HomeContract.View homeView, SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mHomeView = homeView;
        mChronometer= mHomeView.getChmDuration();
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chronometerTick();
            }
        });
        mHomeView.setPresenter(this);
    }

    private void chronometerTick() {
        mHomeView.setArcPower((int) Math.abs((Math.sin(((int) (SystemClock.elapsedRealtime() - mChronometer.getBase()) / 1000)) * 13)));
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

        //TODO: Save Measurement
        getTheInformations();

        mChronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        mChronometer.stop();
    }

    //TODO: Change
    public void getTheInformations() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //Ovde sakam da go zemam measurement od MainActivity od nego da procitam average speed i power i ko ke gi zacuvam da go ispraznam
        Date date = new Date();
        String duration = String.valueOf(mChronometer.getBase());
        String speed = "10";
        if (!Objects.equals(mSharedPreferences.getString("infoItems1", ""), "")) {
            String tmp = mSharedPreferences.getString("infoItems1", "");
            tmp += date + "--" + speed + "--" + duration + "!!";
            editor.putString("infoItems1", tmp);
            editor.apply();
        } else {
            editor.putString("infoItems1", date + "--" + speed + "--" + duration + "!!");
            editor.apply();
        }
        //Toast.makeText(view.getContext(),sharedPreferences.getString("infoItems1","").toString(), Toast.LENGTH_LONG).show();
    }
}
