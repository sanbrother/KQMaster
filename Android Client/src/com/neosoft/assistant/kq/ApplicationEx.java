package com.neosoft.assistant.kq;

import android.app.Application;

public class ApplicationEx extends Application {
    private static ApplicationEx singleInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }

    public static ApplicationEx getInstance() {
        return singleInstance;
    }
}
