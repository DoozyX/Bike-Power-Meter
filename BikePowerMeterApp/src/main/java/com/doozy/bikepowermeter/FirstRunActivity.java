package com.doozy.bikepowermeter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FirstRunActivity extends Activity {

    private RadioGroup radioGroupUnit;
    private RadioButton selectedUnit;
    private EditText weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);
    }

    public void sendMessage(View view) {
        SharedPreferences prefs = getSharedPreferences("com.doozy.bikepowermeter", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstRun", false);

        radioGroupUnit = findViewById(R.id.radioGroupFirstRunUnit);
        int selectedUnitId = radioGroupUnit.getCheckedRadioButtonId();
        selectedUnit = findViewById(selectedUnitId);
        editor.putString("unit", selectedUnit.getText().toString());

        weight = findViewById(R.id.editTextFirstRunYourWeight);
        editor.putString("yourWeight", weight.getText().toString());

        weight = findViewById(R.id.editTextFirstRunBikeWeight);
        editor.putString("bikeWeight", weight.getText().toString());

        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
