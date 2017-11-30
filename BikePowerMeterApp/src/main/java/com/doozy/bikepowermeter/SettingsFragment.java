package com.doozy.bikepowermeter;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
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
        SharedPreferences.Editor editor = prefs.edit();

        RadioGroup radioGroup = myView.findViewById(R.id.radioGroupSettingsUnit);
        String unit = prefs.getString("unit", "Unknown");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                prefs = getActivity().getSharedPreferences("com.doozy.bikepowermeter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                RadioButton selectedUnit;

                RadioGroup radioGroup = myView.findViewById(R.id.radioGroupSettingsUnit);
                int selectedUnitId = radioGroup.getCheckedRadioButtonId();
                selectedUnit = myView.findViewById(selectedUnitId);
                editor.putString("unit", selectedUnit.getText().toString());

                editor.apply();
            }

        });
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

        editTextSettingsYourWeight.addTextChangedListener(new TextWatcher() {
            SharedPreferences.Editor editor = prefs.edit();
            private EditText weight;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                weight = getActivity().findViewById(R.id.editTextSettingsYourWeight);
                editor.putString("yourWeight", weight.getText().toString());
                editor.apply();
            }
        });

        EditText editTextSettingsBikeWeight = myView.findViewById(R.id.editTextSettingsBikeWeight);
        weight = prefs.getString("bikeWeight", "Unknown");
        if (!weight.equals("Unknown")) {
            editTextSettingsBikeWeight.setText(weight);
        } else {
            Log.e("bikeWeight", weight);
        }
        editTextSettingsBikeWeight.addTextChangedListener(new TextWatcher() {
            SharedPreferences.Editor editor = prefs.edit();
            private EditText weight;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                weight = getActivity().findViewById(R.id.editTextSettingsBikeWeight);
                editor.putString("bikeWeight", weight.getText().toString());
                editor.apply();
            }
        });


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

        editor.apply();

        return myView;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("bikeTires", adapterView.getItemAtPosition(i).toString());
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}