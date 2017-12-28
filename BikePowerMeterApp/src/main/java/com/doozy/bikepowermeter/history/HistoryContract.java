package com.doozy.bikepowermeter.history;

import com.doozy.bikepowermeter.BasePresenter;
import com.doozy.bikepowermeter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface HistoryContract {
    interface View extends BaseView<HistoryContract.Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
