package com.doozy.bikepowermeter.settings;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 *  TODO: Description
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private SharedPreferences mSharedPreferences;
    private SettingsContract.View mSettingsView;


    public SettingsPresenter(@NonNull SettingsContract.View settingsView, SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mSettingsView = settingsView;
        mSettingsView.setPresenter(this);
    }

    @Override
    public void start() {
        //Unit
        String unit = getUnit();
        if (mSettingsView.isUnitMetric(unit)) {
            mSettingsView.showUnitMetric();
        } else if (mSettingsView.isUnitImperial(unit)){
            mSettingsView.showUnitImperial();
        } else {
            Log.e("unit", unit);
        }
        //Your Weight
        String weight = getYourWeight();
        if (!weight.equals("Unknown")) {
            mSettingsView.setYourWeight(weight);
        } else {
            Log.e("BikeTiresSize", weight);
        }
        //Bike Weight
        weight = getBikeWeight();
        if (!weight.equals("Unknown")) {
            mSettingsView.setBikeWeight(weight);
        } else {
            Log.e("bikeWeight", weight);
        }


        String bikeTiresType = getBikeTireType();
        if (mSettingsView.isBikeTireMountain(bikeTiresType)) {
            mSettingsView.showBikeTireMountain();
        } else if (mSettingsView.isBikeTireRoad(bikeTiresType)) {
            mSettingsView.showBikeTireRoad();
        } else {
            Log.e("bikeTires", bikeTiresType);
        }
    }

    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void saveUnit(String unit) {
        saveToSharedPreferences("unit", unit);
    }

    @Override
    public void saveBikeWeight(String weight) {
        saveToSharedPreferences("bikeWeight", weight);
    }

    @Override
    public void saveYourWeight(String weight) {
        saveToSharedPreferences("yourWeight", weight);
    }

    @Override
    public void setBikeTireSize(String bikeTireSize) {
        saveToSharedPreferences("bikeTires", bikeTireSize);
    }

    private String getUnit() {
        return mSharedPreferences.getString("unit", "Unknown");
    }

    private String getYourWeight() {
        return mSharedPreferences.getString("yourWeight", "Unknown");
    }

    private String getBikeTireType() {
        return mSharedPreferences.getString("bikeTires", "Unknown");
    }

    private String getBikeWeight() {
        return mSharedPreferences.getString("bikeWeight", "Unknown");
    }

}
