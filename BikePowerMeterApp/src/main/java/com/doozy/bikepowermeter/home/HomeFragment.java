package com.doozy.bikepowermeter.home;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.doozy.bikepowermeter.R;
import com.doozy.bikepowermeter.data.AppDatabase;
import com.github.lzyzsd.circleprogress.ArcProgress;

/**
 * TODO: Description
 */

public class HomeFragment extends Fragment implements HomeContract.View, LocationListener {
    View myView;

    static final int REQUEST_LOCATION = 1;
    private HomeContract.Presenter mPresenter;

    ArcProgress arcProgressHomePower;

    Button btnStart;
    Button btnPauseContinue;
    Button btnStop;
    LinearLayout layoutPauseStop;

    RadioGroup rgPosition;
    RadioButton rbRelaxed;
    RadioButton rbAggressive;
    RadioButton rbAerodynamic;

    TextView tvSpeed;
    Chronometer chmDuration;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_main, container, false);
        getActivity().setTitle(R.string.nav_home);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.onLocationChanged(null);
        }


        arcProgressHomePower = myView.findViewById(R.id.arcProgressHomePower);

        rgPosition = myView.findViewById(R.id.radioGroupPositon);
        rgPosition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbRelaxed.getId()) {
                    mPresenter.setPosition(HomeContract.Position.RELAXED);
                } else if (checkedId == rbAggressive.getId()) {
                    mPresenter.setPosition(HomeContract.Position.AGGRESSIVE);
                } else {
                    mPresenter.setPosition(HomeContract.Position.AERODYNAMIC);
                }
            }
        });
        rbRelaxed = myView.findViewById(R.id.rbRelaxed);
        rbAggressive = myView.findViewById(R.id.rbAgressive);
        rbAerodynamic = myView.findViewById(R.id.rbAerodynamic);

        tvSpeed = myView.findViewById(R.id.textViewHomeSpeed);
        chmDuration = myView.findViewById(R.id.chronometerHomeDuration);

        btnStart = myView.findViewById(R.id.btnHomeStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startRide();
            }
        });
        btnPauseContinue = myView.findViewById(R.id.btnHomePauseContinue);
        btnPauseContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPresenter.pauseOrContinueRide();
            }
        });
        btnStop = myView.findViewById(R.id.btnHomeStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.stopRide();
            }
        });
        layoutPauseStop = myView.findViewById(R.id.linearLayoutHomeBottomButtons);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("com.doozy.bikepowermeter", Context.MODE_PRIVATE);
        new HomePresenter(this, prefs, AppDatabase.getAppDatabase(getActivity().getApplicationContext()));

        return myView;
    }

    public void hideStartButton() {
        btnStart.setVisibility(View.INVISIBLE);
    }

    public void showStartButton() {
        btnStart.setVisibility(View.VISIBLE);
    }

    public void showPauseStopLayout() {
        layoutPauseStop.setVisibility(View.VISIBLE);
    }

    public void hidePauseStopLayout() {
        layoutPauseStop.setVisibility(View.INVISIBLE);
    }

    public void setArcPower(int power) {
        arcProgressHomePower.setProgress(power);
    }

    public void setPauseButton() {
        btnPauseContinue.setText(getResources().getString(R.string.continue_text));
    }

    public void setContinueButton() {
        btnPauseContinue.setText(getResources().getString(R.string.pause));
    }

    public Chronometer getChmDuration() {
        return chmDuration;
    }


    @Override
    public void onLocationChanged(Location location) {
        TextView t = myView.findViewById(R.id.textViewHomeSpeed);
        if(location==null){

            t.setText("0 m/s");
        }else{
            float nCurrentSpeed = location.getSpeed();
            t.setText(nCurrentSpeed +" m/s");
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
