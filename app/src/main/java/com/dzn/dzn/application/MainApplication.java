package com.dzn.dzn.application;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

public class MainApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private final String TWITTER_KEY = "xgKQiEvIz7szWvTT1TujRLi1C";
    private final String TWITTER_SECRET = "4hTtxVjDLIc6C2l5iZbexaFJ6BIl7j1qnpg6gQ0fhU6HYU3qys";
    private final String[] sVkScope = new String[]{
            VKScope.WALL,
            VKScope.PHOTOS
    };

    public MainApplication(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(this);
        VKSdk.initialize(this);
    }
    public String[] getVKScope(){return sVkScope;}
    public String[] getTwitterKeys(){
        String[] res = {TWITTER_KEY, TWITTER_SECRET};
        return res;
    }
}

