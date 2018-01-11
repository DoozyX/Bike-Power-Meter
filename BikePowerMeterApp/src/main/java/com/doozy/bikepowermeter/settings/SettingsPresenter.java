package com.doozy.bikepowermeter.settings;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Listens to user actions from the UI ({@link SettingsFragment}), retrieves the data and updates
 * the UI as required.
 */
public class SettingsPresenter implements SettingsContract.Presenter {
    /**
     * Data where this presenter will read from
     */
    private SharedPreferences mSharedPreferences;
    /**
     * View witch the presenter will control
     */
    private SettingsContract.View mSettingsView;

    /**
     * Constructor for the presenter where the view is set to this presenter
     *
     * @param settingsView view witch this presenter will control
     * @param sharedPreferences Data where the presenter will read from
     */
    SettingsPresenter(@NonNull SettingsContract.View settingsView, SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mSettingsView = settingsView;
        mSettingsView.setPresenter(this);
    }

    /**
     * Called on creation of the presenter and the values are read from data
     * and screen is updated based on them
     */
    @Override
    public void start() {
        //Save Measurement Unit
        String unit = getUnit();
        if (mSettingsView.isUnitMetric(unit)) {
            mSettingsView.showUnitMetric();
        } else if (mSettingsView.isUnitImperial(unit)){
            mSettingsView.showUnitImperial();
        } else {
            Log.e("unit", unit);
        }
        //Save Your Weight
        String weight = getYourWeight();
        if (!weight.equals("Unknown")) {
            mSettingsView.setYourWeight(weight);
        } else {
            Log.e("BikeTiresSize", weight);
        }
        //Save Bike Weight
        weight = getBikeWeight();
        if (!weight.equals("Unknown")) {
            mSettingsView.setBikeWeight(weight);
        } else {
            Log.e("bikeWeight", weight);
        }

        //Save Bike Tire type
        String bikeTiresType = getBikeTireType();
        if (mSettingsView.isBikeTireMountain(bikeTiresType)) {
            mSettingsView.showBikeTireMountain();
        } else if (mSettingsView.isBikeTireRoad(bikeTiresType)) {
            mSettingsView.showBikeTireRoad();
        } else {
            Log.e("bikeTires", bikeTiresType);
        }
    }


    /**
     * Helper function to save the data to shared preferences
     * @param key key for the data
     * @param value value of the data
     */
    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Updates current unit in the data
     * @param unit unit to be updated
     */
    @Override
    public void saveUnit(String unit) {
        saveToSharedPreferences("unit", unit);
    }

    /**
     * Updates current Bike Weight to the data
     * @param weight weight to be updated
     */
    @Override
    public void saveBikeWeight(String weight) {
        saveToSharedPreferences("bikeWeight", weight);
    }

    /**
     * Updates current Your Weight to the data
     * @param weight weight to be updated
     */
    @Override
    public void saveYourWeight(String weight) {
        saveToSharedPreferences("yourWeight", weight);
    }


    /**
     * Updates Bike Tire Type
     *
     * @param bikeTireSize new bike tire type
     */
    @Override
    public void setBikeTireSize(String bikeTireSize) {
        saveToSharedPreferences("bikeTires", bikeTireSize);
    }

    /**
     * @return Returns the unit from the data
     */
    private String getUnit() {
        return mSharedPreferences.getString("unit", "Unknown");
    }

    /**
     * @return Returns Your Weight from the data
     */
    private String getYourWeight() {
        return mSharedPreferences.getString("yourWeight", "Unknown");
    }

    /**
     * @return Returns Bike Tire Type from the data
     */
    private String getBikeTireType() {
        return mSharedPreferences.getString("bikeTires", "Unknown");
    }

    /**
     * @return Returns Bike Weight from the data
     */
    private String getBikeWeight() {
        return mSharedPreferences.getString("bikeWeight", "Unknown");
    }

}
