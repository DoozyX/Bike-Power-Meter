package com.doozy.bikepowermeter;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Created by doozy on 25-Nov-17
 */

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    View myView;
    SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.settings_main, container, false);
        getActivity().setTitle(R.string.nav_settings);

        prefs = this.getActivity().getSharedPreferences("com.doozy.bikepowermeter", Context.MODE_PRIVATE);

        RadioGroup radioGroup = myView.findViewById(R.id.radioGroupSettingsUnit);
        String unit = prefs.getString("unit", "Unknown");
        if (unit.equals(getResources().getString(R.string.unit_metric))) {
            radioGroup.check(R.id.rbSettingsMetric);
        } else if (unit.equals(getResources().getString(R.string.unit_imperial))) {
            radioGroup.check(R.id.rbSettingsImperial);
        } else {
            Log.e("Unit", unit);
        }

        EditText editTextSettingsYourWeight = myView.findViewById(R.id.editTextSettingsYourWeight);
        String weight = prefs.getString("yourWeight", "Unknown");
        if (!weight.equals("Unknown")) {
            editTextSettingsYourWeight.setText(weight);
        } else {
            Log.e("BikeTiresSize", weight);
        }

        EditText editTextSettingsBikeWeight = myView.findViewById(R.id.editTextSettingsBikeWeight);
        weight = prefs.getString("bikeWeight", "Unknown");
        if (!weight.equals("Unknown")) {
            editTextSettingsBikeWeight.setText(weight);
        } else {
            Log.e("bikeWeight", weight);
        }


        Spinner spinner = myView.findViewById(R.id.spinnerSettingsBikeTireSize);
        spinner.setOnItemSelectedListener(this);

        String bikeType = prefs.getString("bikeTires", "Unknown");
        if (bikeType.equals(getResources().getString(R.string.mountain_bike_tires))) {
            spinner.setSelection(0);
        } else if (bikeType.equals(getResources().getString(R.string.road_racing_bike_tires))) {
            spinner.setSelection(1);
        } else {
            Log.e("bikeTires", bikeType);
        }

        return myView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences.Editor editor = prefs.edit();
        Log.e("adapterView.get", adapterView.getItemAtPosition(i).toString());
        editor.putString("bikeTires", adapterView.getItemAtPosition(i).toString());
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}