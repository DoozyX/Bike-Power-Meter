package com.doozy.bikepowermeter.home;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.doozy.bikepowermeter.services.impl.OpenWeatherMapWeatherServiceImpl;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Locale;

/**
 * TODO: Description
 */

public class HomeFragment extends Fragment implements HomeContract.View{
    private View myView;

    private HomeContract.Presenter mPresenter;

    private ArcProgress arcProgressHomePower;

    private Button btnStart;
    private Button btnPauseContinue;
    private Button btnStop;
    private LinearLayout layoutPauseStop;

    private RadioGroup rgPosition;
    private RadioButton rbRelaxed;
    private RadioButton rbAggressive;
    private RadioButton rbAerodynamic;

    private TextView tvSpeed;
    private Chronometer chmDuration;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.nav_home);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_main, container, false);

        arcProgressHomePower = myView.findViewById(R.id.arcProgressHomePower);

        rgPosition = myView.findViewById(R.id.radioGroupPosition);
        rgPosition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbRelaxed.getId()) {
                    mPresenter.setPosition(HomeContract.Position.RELAXED);
                } else if (checkedId == rbAggressive.getId()) {
                    mPresenter.setPosition(HomeContract.Position.AGGRESSIVE);
                } else if (checkedId == rbAerodynamic.getId()){
                    mPresenter.setPosition(HomeContract.Position.AERODYNAMIC);
                }
            }
        });
        rbRelaxed = myView.findViewById(R.id.rbRelaxed);
        rbAggressive = myView.findViewById(R.id.rbAggressive);
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

        new HomePresenter(this, new OpenWeatherMapWeatherServiceImpl(getActivity()), prefs, myView.getContext());

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

    @Override
    public void setSpeed(double speed) { tvSpeed.setText(String.format(Locale.ENGLISH, "%.2f%s", speed, getString(R.string.km_h))); }

    public void setPauseButton() {
        btnPauseContinue.setText(getResources().getString(R.string.pause));
    }

    public void setContinueButton() {
        btnPauseContinue.setText(getResources().getString(R.string.continue_text));
    }

    public Chronometer getChmDuration() {
        return chmDuration;
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
