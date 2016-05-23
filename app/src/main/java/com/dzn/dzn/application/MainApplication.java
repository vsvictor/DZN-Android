package com.dzn.dzn.application;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by zhenya on 23.05.2016.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(getApplicationContext());
    }
}
