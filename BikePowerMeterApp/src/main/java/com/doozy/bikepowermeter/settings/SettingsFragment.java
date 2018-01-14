package com.doozy.bikepowermeter.settings;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.doozy.bikepowermeter.R;

import java.util.Locale;


/**
 * Main UI for the Settings screen.
 */
public class SettingsFragment extends Fragment implements SettingsContract.View {

    /**
     * Reference to the presenter of this view
     */
    private SettingsContract.Presenter mPresenter;


    /**
     * All components of this view
     */
    private RadioGroup unitRadioGroup;

    private EditText editTextSettingsRiderWeight;

    private EditText editTextSettingsBikeWeight;

    private Spinner spinner;


    /**
     * Android method called on creation
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.settings);
    }

    /**
     * Android method called on creating of the view
     *
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState savedInstanceState
     * @return current view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View myView = inflater.inflate(R.layout.settings_main, container, false);

        //Load the components of the view into the variables and make listeners so they can be updated automatically
        unitRadioGroup = myView.findViewById(R.id.radioGroupSettingsUnit);

        unitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSettingsMetric) {
                    mPresenter.saveUnit(0);
                } else {
                    mPresenter.saveUnit(1);
                }
            }
        });

        editTextSettingsRiderWeight = myView.findViewById(R.id.editTextSettingsRiderWeight);

        editTextSettingsRiderWeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String weight = editTextSettingsRiderWeight.getText().toString();
                mPresenter.saveRiderWeight(Integer.parseInt(weight));
            }
        });

        editTextSettingsBikeWeight = myView.findViewById(R.id.editTextSettingsBikeWeight);

        editTextSettingsBikeWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String weight = editTextSettingsBikeWeight.getText().toString();
                mPresenter.saveBikeWeight(Integer.parseInt(weight));
            }
        });


        spinner = myView.findViewById(R.id.spinnerSettingsBikeTireSize);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.setBikeTireSize(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences prefs = this.getActivity().getSharedPreferences("com.doozy.bikepowermeter", Context.MODE_PRIVATE);

        new SettingsPresenter(this, prefs);

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    /**
     * Sets the presenter of this view
     *
     * @param presenter Presenter to be set on this view
     */
    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Checks the metric radio button
     */
    @Override
    public void showUnitMetric() {
        unitRadioGroup.check(R.id.rbSettingsMetric);
    }

    /**
     * Checks the imperial radio button
     */
    @Override
    public void showUnitImperial() {
        unitRadioGroup.check(R.id.rbSettingsImperial);
    }

    /**
     * Sets Your Weight field to the value
     *
     * @param weight Weight of the person
     */
    @Override
    public void setRiderWeight(int weight) {
        editTextSettingsRiderWeight.setText(String.format(Locale.ENGLISH,"%d", weight));
    }

    /**
     * Sets Bike Weight field to the value
     *
     * @param weight Weight of the bike
     */
    @Override
    public void setBikeWeight(int weight) {
        editTextSettingsBikeWeight.setText(String.format(Locale.ENGLISH,"%d", weight));
    }

    /**
     * Selects the Mountain Bike Tires from the combo box
     */
    @Override
    public void showBikeTireMountain() {
        spinner.setSelection(0);
    }

    /**
     * Selects the Road Bike Tires from the combo box
     */
    @Override
    public void showBikeTireRoad() {
        spinner.setSelection(1);
    }

    /**
     * Returns if given unit is metric unit
     *
     * @param unit Given unit to be tested
     * @return Boolean result of the test
     */
    @Override
    public boolean isUnitMetric(int unit) {
        return unit == 0;
    }

    /**
     * Returns if given unit is imperial unit
     *
     * @param unit Given unit to be tested
     * @return Boolean result of the test
     */
    @Override
    public boolean isUnitImperial(int unit) {
        return !isUnitMetric(unit);
    }

    /**
     * Returns if given string is Mountain bike tires
     *
     * @param bikeTireType Given bike tire to be tested
     * @return Boolean result of the test
     */
    @Override
    public boolean isBikeTireMountain(int bikeTireType) {
        return bikeTireType == 0;
    }

    /**
     * Returns if given string is Road bike tires
     *
     * @param bikeTireType Given bike tire to be tested
     * @return Boolean result of the test
     */
    @Override
    public boolean isBikeTireRoad(int bikeTireType) {
        return !isBikeTireMountain(bikeTireType);
    }
}