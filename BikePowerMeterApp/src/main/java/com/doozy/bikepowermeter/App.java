package com.doozy.bikepowermeter;

import android.app.Application;

import com.doozy.bikepowermeter.data.AppDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppDatabase.getAppDatabase(this);
        RequestQueueSingleton.getInstance(this);
    }
}