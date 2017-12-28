package com.doozy.bikepowermeter.history;

import android.support.annotation.NonNull;

/**
 * Created by doozy on 28-Dec-17
 */

public class HistoryPresenter implements HistoryContract.Presenter {
    public HistoryPresenter(@NonNull HistoryContract.View view) {
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
