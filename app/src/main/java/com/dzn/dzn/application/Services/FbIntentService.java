package com.dzn.dzn.application.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.dzn.dzn.application.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by zhenya on 27.05.2016.
 */
public class FbIntentService extends IntentService {
    private static final String TAG = "FbIntentService";

    //Integrate Twitter
    private static final String TW_APP_NAME = "com.twitter.android";
    private static final String CONSUMER_KEY = "iicXFAOT5T0XgczLapahUcQOa";
    private static final String CONSUMER_SECRET = "BeObQbPyQyfWPEYpXvzUZvIn1ORZrirLRZXFnXZDIf0pAoXUKw";

    private static final String FB_APP_NAME = "com.facebook.katana";
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private List<String> permissions = Arrays.asList("publish_actions");
    private Bitmap bitmap;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FbIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String path = intent.getStringExtra("photo");
        Uri uri = Uri.parse(path);
        bitmap = loadBitmap(uri);

        if (isPackageInstalled(TW_APP_NAME)) {
            postPhotoToTwitter(uri);
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        //Initialize Twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
    }

    private Bitmap loadBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Check installed application
     *
     * @param packageName
     * @return
     */
    private boolean isPackageInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Post tweet to Twitter
     *
     * @param uri
     */
    private void postPhotoToTwitter(Uri uri) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text(getResources().getString(R.string.publish_message))
                .image(uri);
        builder.show();
    }
}
