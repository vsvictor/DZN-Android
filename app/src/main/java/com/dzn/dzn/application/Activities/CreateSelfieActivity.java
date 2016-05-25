package com.dzn.dzn.application.Activities;


import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzn.dzn.application.MainActivity;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;
import com.dzn.dzn.application.Widget.SquaredFrame;
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
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.util.VKUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class CreateSelfieActivity extends BaseActivity {
    private static final String TAG = "CreateSelfieActivity";

    //Integrate VK
    private static final String[] sVkScope = new String[]{
            VKScope.WALL,
            VKScope.PHOTOS
    };

    //Integrate Twitter
    private static final String CONSUMER_KEY = "iicXFAOT5T0XgczLapahUcQOa";
    private static final String CONSUMER_SECRET = "BeObQbPyQyfWPEYpXvzUZvIn1ORZrirLRZXFnXZDIf0pAoXUKw";

    //integrate Facebook
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private List<String> permissions = Arrays.asList("publish_actions");
    private Bitmap btm;

    private TextView tvCreateSelfie;
    private ImageView ivPhoto;
    private ImageButton ibFlash;
    private ImageButton ibSpread;
    private ImageButton ibStop;
    private LinearLayout llSpreadSelfie;
    private TextView tvSelfieSpread;

    private Camera camera;
    private SquaredFrame surfaceView;
    private SurfaceHolder surfaceHolder;
    private DataBaseHelper dataBaseHelper;
    private AlarmManager alarmManager;

    private boolean created = false;
    private PowerManager.WakeLock mWakeLock;
    private final static String CLASS_LABEL = "CreateSelfieActivity";

    private static int counter = 1;
    private int id;
    private int idCamera;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, CLASS_LABEL);
        //mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, CLASS_LABEL);
        mWakeLock.acquire();

        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock(CLASS_LABEL);
        kl.disableKeyguard();

        //Initialize Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        created = false;
        setContentView(R.layout.activity_create_selfie);
        idCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = getIntent().getExtras().getInt("id", -1);
            if (id > 0) {
                alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                dataBaseHelper = DataBaseHelper.getInstance(this);
                Alarm al = dataBaseHelper.getAlarm(id);
                if (al.isOne()) {
                    al.setTurnOn(false);
                    dataBaseHelper.updateAlarm(al);
                }
            }
        } else id = -1;

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);
        ringtoneAlarm.play();

        //Initialize settings
        settings = Settings.getInstance(this);

        //Initialize VK
        getVKCertificate();

        //if (!VKSdk.wakeUpSession(this)) {
        //    Log.d(TAG, "VK authorize");
        //    VKSdk.login(CreateSelfieActivity.this, sVkScope);
        //}

        //Initialize Twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        //Initialize view elements
        initView();

        //Initialize Surface
        initSurface();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (camera == null) initCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (created) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.d(TAG, "VK login success");
            }

            @Override
            public void onError(VKError error) {
                Log.d(TAG, "VK login error: " + error.errorMessage);
            }
        }));
    }

    private void initCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        } else {
            Log.d(TAG, "Camera null");
            camera = Camera.open(idCamera);
            try {

                camera.setDisplayOrientation(90);
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
                Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
                setSurfaceSize(optimalSize);
                //surfaceHolder.setFixedSize(optimalSize.width, optimalSize.height);

                parameters.setPreviewSize(optimalSize.width, optimalSize.height);
                camera.setParameters(parameters);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();


            } catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }
        }
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        Log.d(TAG, "initView");

        tvCreateSelfie = (TextView) findViewById(R.id.tvCreateSelfie);
        PFHandbookProTypeFaces.THIN.apply(tvCreateSelfie);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        ibFlash = (ImageButton) findViewById(R.id.ibFlash);
        if (idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT)
            ibFlash.setBackgroundResource(R.mipmap.flash_gray);
        else ibFlash.setBackgroundResource(R.mipmap.flash);

        ibSpread = (ImageButton) findViewById(R.id.ibSpread);
        ibStop = (ImageButton) findViewById(R.id.ibStop);
        llSpreadSelfie = (LinearLayout) findViewById(R.id.llSpreadSelfie);

        tvSelfieSpread = (TextView) findViewById(R.id.tvSelfieSpread);
        PFHandbookProTypeFaces.THIN.apply(tvSelfieSpread);
    }

    //Initialize SurfaceView & SurfaceHolder
    private void initSurface() {
        surfaceView = (SquaredFrame) findViewById(R.id.surfaceView);

        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "surfaceCreated");
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.setDisplayOrientation(90);
                    camera.startPreview();
                } catch (Exception ex) {
                    //Log.d(TAG, ex.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "surfaceDestroyed");
            }
        });
    }

    /**
     * Click on flash
     *
     * @param view
     */
    public void onFlash(View view) {
        if (idCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                if (camera != null) {
                    camera.stopPreview();
                    Camera.Parameters p = camera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    camera.setParameters(p);
                    camera.startPreview();
                } else {
                    Log.i(TAG, "Camera is NULL!!!!!!!!!");
                }
            }
        }
    }

    /**
     * Click on camera
     *
     * @param view
     */
    public void onCamera(View view) {
        if (Camera.getNumberOfCameras() > 1) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            idCamera = (idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
            if (idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT)
                ibFlash.setBackgroundResource(R.mipmap.flash_gray);
            else ibFlash.setBackgroundResource(R.mipmap.flash);
            if (camera == null) {
                initCamera();
            }
        }
    }

    /**
     * Click on stop alarm
     *
     * @param view
     */
    public void onStopAlarm(View view) {
        created = true;
        ibFlash.setVisibility(View.INVISIBLE);
        ibSpread.setVisibility(View.INVISIBLE);
        surfaceView.setVisibility(View.GONE);
        ibStop.setVisibility(View.INVISIBLE);

        ivPhoto.setVisibility(View.VISIBLE);

        tvCreateSelfie.setText(getResources().getString(R.string.good_morning));

        tvCreateSelfie.setTextSize(getResources().getDimension(R.dimen.app_padding_16dp));

        llSpreadSelfie.setVisibility(View.VISIBLE);

        if (!VKSdk.wakeUpSession(this)) {
            Log.d(TAG, "VK authorize");
            VKSdk.login(CreateSelfieActivity.this, sVkScope);
        }

        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d(TAG, "Bitmap length: " + data.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    //rotate bitmap
                    if (idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(-90);
                        btm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    }
                    //btm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                    //set photo
                    else if (idCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap bob = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                        btm = Bitmap.createScaledBitmap(bob, bitmap.getWidth() / 2, bitmap.getHeight(), false);
                    }

                    if (settings.isSocial()) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                //set photo
                                ivPhoto.setImageBitmap(btm);
                            }

                            @Override
                            protected Void doInBackground(Void... params) {
                                //post photo to FB
                                postPhotoToFacebook();

                                //post photo to VK
                                postPhotoToVK(btm);

                                //post photo to Twitter
                                postPhotoToTwitter(btm);

                                //post photo to Instagram
                                postPhotoToInstagram(btm);

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                finish();
                            }
                        }.execute();
                    } else {
                        ivPhoto.setImageBitmap(btm);
                        finish();
                    }
/**
 callbackManager = CallbackManager.Factory.create();
 loginManager = LoginManager.getInstance();
 loginManager.logInWithPublishPermissions(CreateSelfieActivity.this, permissions);
 loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
@Override public void onSuccess(LoginResult loginResult) {
//sharePhotoToFacebook(btm);
Intent intent = new Intent(CreateSelfieActivity.this, MainActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
finish();
}

@Override public void onCancel() {
System.out.println("onCancel");
Intent intent = new Intent(CreateSelfieActivity.this, MainActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
finish();
}

@Override public void onError(FacebookException exception) {
System.out.println("onError");
Intent intent = new Intent(CreateSelfieActivity.this, MainActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
finish();
}
});
 */
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateSelfieActivity.this);
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.error_photo_message);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                        }
                    });
                }
                camera.stopPreview();
            }
        });
        if (id > 0) {
            int counter = getIntent().getExtras().getInt("counter");
            long time = getIntent().getExtras().getLong("time");
            Intent intent = new Intent(this, CreateSelfieActivity.class);
            intent.putExtra("counter", counter);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, counter, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
        counter = 1;
        for (Alarm alarm : getListAlarm()) {
            Date d = alarm.getDate();
            Date today = Calendar.getInstance().getTime();
            today.setHours(d.getHours());
            today.setMinutes(d.getMinutes());
            today.setSeconds(0);
            if ((today.getTime() > System.currentTimeMillis()) && alarm.isTurnOn()) {
                Intent intentNew = new Intent(this, CreateSelfieActivity.class);
                intentNew.putExtra("id", alarm.getID());
                intentNew.putExtra("counter", counter);
                intentNew.putExtra("time", today.getTime());
                PendingIntent pendingIntentNew = PendingIntent.getActivity(this, counter, intentNew, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, today.getTime(), pendingIntentNew);
                counter++;
            }
        }
    }

    private ArrayList<Alarm> getListAlarm() {
        if (dataBaseHelper == null) {
            Log.i(TAG, "DNHelper is null");
            dataBaseHelper = DataBaseHelper.getInstance(this);
        }

        ArrayList<Alarm> ar = dataBaseHelper.getAlarmList();
        if (ar == null) return new ArrayList<Alarm>();

        return ar;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private void setSurfaceSize(Camera.Size size) {

        // // Get the dimensions of the video
        int videoWidth = size.height;
        int videoHeight = size.width;
        float videoProportion = (float) videoWidth / (float) videoHeight;

        // Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        // Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        // Commit the layout parameters
        surfaceView.setLayoutParams(lp);
    }

    private void postPhotoToFacebook() {
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(CreateSelfieActivity.this, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sharePhotoToFacebook(btm);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Facebook exception: " + exception.getMessage());
            }
        });
    }

    /**
     * Share photo to Facebook
     *
     * @param bitmap
     */
    private void sharePhotoToFacebook(Bitmap bitmap) {
        Log.d(TAG, "sharePhotoToFacebook");
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption(getResources().getString(R.string.morning))
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
    }

    /**
     * Facebook Key Hash
     */
    private void getHashKeyFacebook() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.dzn.dzn.application",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     *
     */
    private void getVKCertificate() {
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d(TAG, "VK certificate: " + fingerprints[0]);
    }

    /**
     * Upload and make post photo on wall VK
     *
     * @param bitmap
     */
    private void postPhotoToVK(final Bitmap bitmap) {
        VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(bitmap, VKImageParameters.pngImage()), 0, 0);

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKApiPhoto photo = ((VKPhotoArray) response.parsedModel).get(0);
                //Make post with photo
                makePhotoOnWallVk(photo);

                bitmap.recycle();
            }

            @Override
            public void onError(VKError error) {
                Log.d(TAG, "Error: " + error.errorMessage);
            }
        });
    }

    /**
     * Make post photo on wall VK
     *
     * @param photo
     */
    private void makePhotoOnWallVk(final VKApiPhoto photo) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.ATTACHMENTS, new VKAttachments(photo), VKApiConst.MESSAGE, "Dzn-Dzn photo"));
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d(TAG, "makePhotoOnWallVk complete");
            }

            @Override
            public void onError(VKError error) {
                Log.d(TAG, "makePhotoOnWallVk error: " + error.errorMessage);
            }
        });
    }

    /**
     * Get Photo
     *
     * @param photo
     * @return
     */
    private Bitmap getPhoto(File photo) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(photo));
        } catch (IOException ex) {
            Log.d(TAG, "getPhoto: " + ex.getMessage());
        }
        return bitmap;
    }

    /**
     * Post tweet to Twitter
     *
     * @param bitmap
     */
    private void postPhotoToTwitter(final Bitmap bitmap) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text(getResources().getString(R.string.publish_message))
                .image(getImageUri(bitmap));
        builder.show();
    }

    /**
     * Get Uri from bitmap
     *
     * @param inImage
     * @return
     */
    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Post photo to Instagram
     *
     * @param bitmap
     */
    private void postPhotoToInstagram(final Bitmap bitmap) {
        String instagramPackage = "com.instagram.android";
        if (isPackageInstalled(instagramPackage)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.setPackage(instagramPackage);
            intent.putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap));
            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.publish_message));
            startActivity(intent);
        } else {
            Log.d(TAG, "postPhotoToInstagram: You should install Instagram app first");
        }
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

}
