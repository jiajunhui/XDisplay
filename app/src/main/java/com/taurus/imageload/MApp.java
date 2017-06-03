package com.taurus.imageload;

import android.app.Application;

import com.kk.taurus.imagedisplay.ImageDisplay;

/**
 * Created by Taurus on 2017/6/2.
 */

public class MApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageDisplay.buildDefaultSetting(getApplicationContext());
    }
}
