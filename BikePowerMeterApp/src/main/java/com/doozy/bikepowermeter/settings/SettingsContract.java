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

        void setRiderWeight(int weight);

        void setBikeWeight(int weight);

        void showBikeTireMountain();

        void showBikeTireRoad();

        boolean isUnitMetric(int unit);

        boolean isUnitImperial(int unit);

        boolean isBikeTireMountain(int bikeTireType);

        boolean isBikeTireRoad(int bikeTireType);
    }

    /**
     * Specifies the functions that Presenter needs to implement
     */
    interface Presenter extends BasePresenter {

        void saveUnit(int unit);

        void saveBikeWeight(int weight);

        void saveRiderWeight(int weight);

        void setBikeTireSize(int bikeTireSize);
    }
}
