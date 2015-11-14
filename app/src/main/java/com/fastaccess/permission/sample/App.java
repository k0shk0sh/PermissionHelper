package com.fastaccess.permission.sample;

import android.app.Application;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by Kosh on 14/11/15 1:34 PM. copyrights @ Innov8tif
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CustomActivityOnCrash.install(this);
    }
}
