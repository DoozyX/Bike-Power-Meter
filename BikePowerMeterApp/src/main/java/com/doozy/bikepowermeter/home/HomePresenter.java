package com.doozy.bikepowermeter.home;

import android.support.annotation.NonNull;

/**
 * Created by doozy on 28-Dec-17
 */

public class HomePresenter implements HomeContract.Presenter {

    public HomePresenter(@NonNull HomeContract.View mHomeView) {
        mHomeView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
