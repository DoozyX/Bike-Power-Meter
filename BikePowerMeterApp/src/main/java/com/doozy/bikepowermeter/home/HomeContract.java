package com.doozy.bikepowermeter.home;

import android.widget.Chronometer;
import com.doozy.bikepowermeter.BasePresenter;
import com.doozy.bikepowermeter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HomeContract {
    enum Position {
        RELAXED,
        AGGRESSIVE,
        AERODYNAMIC
    }

    interface View extends BaseView<Presenter> {
        void hideStartButton();

        void showStartButton();

        void showPauseStopLayout();

        void hidePauseStopLayout();

        void setArcPower(int power);

        void setSpeed(double speed);

        void setPauseButton();

        void setContinueButton();

        Chronometer getChmDuration();

    }

    interface Presenter extends BasePresenter {


        void setPosition(Position position);

        void startRide();

        void pauseOrContinueRide();

        void stopRide();
    }
}
