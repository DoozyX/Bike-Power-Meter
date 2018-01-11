package com.doozy.bikepowermeter.settings;

import com.doozy.bikepowermeter.BasePresenter;
import com.doozy.bikepowermeter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SettingsContract {

    /**
     * Specifies the functions that view need to implement
     */
    interface View extends BaseView<Presenter> {
        void showUnitMetric();

        void showUnitImperial();

        void setYourWeight(String weight);

        void setBikeWeight(String weight);

        void showBikeTireMountain();

        void showBikeTireRoad();

        boolean isUnitMetric(String unit);

        boolean isUnitImperial(String unit);

        boolean isBikeTireMountain(String bikeTireType);

        boolean isBikeTireRoad(String bikeTireType);
    }

    /**
     * Specifies the functions that Presenter needs to implement
     */
    interface Presenter extends BasePresenter {

        void saveUnit(String unit);

        void saveBikeWeight(String weight);

        void saveYourWeight(String weight);

        void setBikeTireSize(String bikeTireSize);
    }
}
