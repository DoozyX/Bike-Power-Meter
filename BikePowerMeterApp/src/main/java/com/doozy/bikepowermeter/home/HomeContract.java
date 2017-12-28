package com.doozy.bikepowermeter.home;

import com.doozy.bikepowermeter.BasePresenter;
import com.doozy.bikepowermeter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HomeContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
