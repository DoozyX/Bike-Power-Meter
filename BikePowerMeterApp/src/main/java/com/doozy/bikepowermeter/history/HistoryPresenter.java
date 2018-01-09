package com.doozy.bikepowermeter.history;

import android.support.annotation.NonNull;

/**
 * TODO: Description
 */

public class HistoryPresenter implements HistoryContract.Presenter {
    public HistoryPresenter(@NonNull HistoryContract.View view) {
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
