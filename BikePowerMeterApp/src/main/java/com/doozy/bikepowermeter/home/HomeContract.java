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
        public void hideStartButton();

        public void showStartButton();

        public void showPauseStopLayout();

        public void hidePauseStopLayout();

        public void setArcPower(int power);

        public void setPauseButton();

        public void setContinueButton();

        public Chronometer getChmDuration();

    }

    interface Presenter extends BasePresenter {


        void setPosition(Position position);

        void startRide();

        void pauseOrContinueRide();

        void stopRide();
    }
}
