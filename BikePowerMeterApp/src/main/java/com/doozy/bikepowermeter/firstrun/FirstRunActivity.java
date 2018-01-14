package com.doozy.bikepowermeter.firstrun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.doozy.bikepowermeter.R;
import com.doozy.bikepowermeter.home.MainActivity;

public class FirstRunActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);
    }

    public void sendMessage(View view) {
        SharedPreferences prefs = getSharedPreferences("com.doozy.bikepowermeter", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstRun", false);

        RadioGroup radioGroupUnit = findViewById(R.id.radioGroupFirstRunUnit);
        int selectedUnitId = radioGroupUnit.getCheckedRadioButtonId();
        if (selectedUnitId == R.id.rbFirstRunMetric)  {
            editor.putInt("unit", 0);
        } else {
            editor.putInt("unit", 1);
        }

        EditText weight = findViewById(R.id.editTextFirstRunRiderWeight);
        editor.putInt("riderWeight", Integer.parseInt(weight.getText().toString()));

        weight = findViewById(R.id.editTextFirstRunBikeWeight);
        editor.putInt("bikeWeight", Integer.parseInt(weight.getText().toString()));

        editor.putInt("bikeTires", 0);

        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
