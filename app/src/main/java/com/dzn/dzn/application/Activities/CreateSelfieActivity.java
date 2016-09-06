package com.dzn.dzn.application.Activities;


import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dzn.dzn.application.RequestUserPermission;
import com.dzn.dzn.application.Utils.DateTimeOperator;
import com.google.android.gms.maps.model.LatLng;

import com.dzn.dzn.application.LocationService;
import com.dzn.dzn.application.MainActivity;
import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.Objects.Settings;
import com.dzn.dzn.application.Objects.Social;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class CreateSelfieActivity extends BaseActivity {
    private static final String TAG = "CreateSelfieActivity";
    private final static String CLASS_LABEL = "CreateSelfieActivity";

    private TextView tvCreateSelfie;
    private ImageView ivPhoto;
    private ImageButton ibFlash;
    private ImageButton ibSpread;
    private ImageButton ibStop;
    private LinearLayout llSpreadSelfie;
    private TextView tvSelfieSpread;

    private Bitmap btm;
    private static Camera camera = null;
    private SurfaceView sv;
    private SurfaceHolder surfaceHolder;
    private DataBaseHelper dataBaseHelper;
    private AlarmManager alarmManager;

    private boolean created = false;
    private PowerManager.WakeLock mWakeLock;

    private static int counter = 1;
    private int id;
    private Alarm alarm;
    private int idCamera;
    private boolean FULL_SCREEN = true;
    private boolean flashMode = true;
    private Uri uri;

    private Settings settings;
    private MediaPlayer mMediaPlayer;
    private CallbackManager callbackManager;

    private boolean published = false;

    private ArrayList<Social> arrSocial;
    private boolean restored;
    private int oldMode;
    private LatLng location;

    private Vibrator vibrator;
    private long[] vibrationPattern = {200, 400, 600, 400};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");


        //Start location service

        try {
            startService(new Intent(this, LocationService.class));
        }catch (RuntimeException ex){
            ex.printStackTrace();
        }

        if(savedInstanceState == null) {

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, CLASS_LABEL);
            //mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, CLASS_LABEL);
            mWakeLock.acquire();

            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            final KeyguardManager.KeyguardLock kl = km.newKeyguardLock(CLASS_LABEL);
            kl.disableKeyguard();

            created = false;
            setContentView(R.layout.activity_create_selfie);
            callbackManager = CallbackManager.Factory.create();
            idCamera = CameraInfo.CAMERA_FACING_FRONT;

            Bundle b = getIntent().getExtras();
            if (b != null) {
                id = getIntent().getExtras().getInt("id", -1);
                if (id > 0) {
                    alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    dataBaseHelper = DataBaseHelper.getInstance(this);
                    alarm = dataBaseHelper.getAlarm(id);
                    if(alarm == null) {finish();return;}
                    if (alarm != null && alarm.isOne()) {
                        alarm.setTurnOn(false);
                        dataBaseHelper.updateAlarm(alarm);
                    }
                }
            } else id = -1;

            //Initialize settings
            settings = Settings.getInstance(this);

            if(location == null) location = new LatLng(0,0);

            //Initialize view elements
            initView();

            //Initialize Surface
            initSurface();

            restored = false;
        }
        else{
            restored = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!restored) initCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationService.BROADCAST_ACTION);
        registerReceiver(locationReceiver, filter);
        if(!restored) {
            if (camera == null) initCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        unregisterReceiver(locationReceiver);
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
    @Override
    protected void onDestroy(){
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(oldMode);
        super.onDestroy();
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
        //requestCode==100 - Instagram published
        //requestCode==101 - Instagram instaled
        //requestCode==102 - twitter paublished
        //requestCode==103 - twitter instaled
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        if(!published){
            publishToFacebook();
            published = true;
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
        })) ;
        if(requestCode == 100){
            for(Social ss:arrSocial){
                if(ss.getID() == 4){arrSocial.remove(ss);break;}
            }
            publisher(arrSocial);
        }
        if(requestCode == 101){
            publishToInstagram(uri);
        }
        if(requestCode == 102){
            for(Social ss:arrSocial){
                if(ss.getID() == 2){arrSocial.remove(ss);break;}
            }
            publisher(arrSocial);
        }
        if(requestCode == 103){
            publishToTwitter(uri);
        }
    }
    private void initCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (camera == null) {
            Log.d(TAG, "Camera null");
            try {
                if(Camera.getNumberOfCameras()>1){
                    camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
                }
                else{
                    camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
                }
                initSurface();
                camera.setPreviewDisplay(surfaceHolder);
                setCameraDisplayOrientation(idCamera);
                setPreviewSize(FULL_SCREEN);
                setMaxPictureSize(camera);
                camera.startPreview();

                //Initialize camera flash mode
                initCameraFlashMode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void initCameraFlashMode() {
        if (idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            ibFlash.setVisibility(View.INVISIBLE);
        } else {
            //Set camera flash mode off
            Camera.Parameters p = camera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            ibFlash.setBackgroundResource(R.drawable.ic_flash_gray);
        }
    }
    private void initView() {
        Log.d(TAG, "initView");

        tvCreateSelfie = (TextView) findViewById(R.id.tvCreateSelfie);
        PFHandbookProTypeFaces.THIN.apply(tvCreateSelfie);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        ibFlash = (ImageButton) findViewById(R.id.ibFlash);

        //Initialize camera flash mode
        initCameraFlashMode();

        ibSpread = (ImageButton) findViewById(R.id.ibSpread);
        ibStop = (ImageButton) findViewById(R.id.ibStop);
        llSpreadSelfie = (LinearLayout) findViewById(R.id.llSpreadSelfie);

        tvSelfieSpread = (TextView) findViewById(R.id.tvSelfieSpread);
        PFHandbookProTypeFaces.THIN.apply(tvSelfieSpread);
    }
    private void initSurface() {
        sv = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = sv.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    setCameraDisplayOrientation(idCamera);
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    Log.d(TAG, "" + ex.getMessage());
                }

                playMelody();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                try {
                    camera.stopPreview();
                    setCameraDisplayOrientation(idCamera);
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }
    private void runVibration() {
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrationPattern, 1);
    }
    private void playMelody() {
        Uri alert = null;
        if(alarm != null) {
            if (settings.isVibro()) {
                runVibration();
            }
            if ((!alarm.getMelody().equals(""))) {
                Log.d(TAG, "Alarm melody: " + alarm.getMelody());
                alert = Uri.parse(alarm.getMelody());
            } else if (!settings.getMelody().equals("")) {
                Log.d(TAG, "Settings melody: " + settings.getMelody());
                alert = Uri.parse(settings.getMelody());
            } else {
                Log.d(TAG, "Set default ringtone");
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }

            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            oldMode = audioManager.getRingerMode();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            if (mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
            if (!mMediaPlayer.isPlaying()) {
                try {
                    float vol = ((float)settings.getSound())/100;
                    Log.i(TAG, "!!!!!!!!!Volue:"+vol);
                    mMediaPlayer.setDataSource(CreateSelfieActivity.this, alert);
                    if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                        mMediaPlayer.setVolume(vol, vol);
                        mMediaPlayer.setLooping(true);
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e){
                    Log.i(TAG, alert.toString());
                    e.printStackTrace();
                }
            }
        }
    }
    public void onFlash(View view) {
        if (idCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                if (camera != null) {
                    camera.stopPreview();
                    if (flashMode) {
                        Camera.Parameters p = camera.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                        camera.setParameters(p);
                        ibFlash.setBackgroundResource(R.drawable.ic_flash);
                    } else {
                        Camera.Parameters p = camera.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(p);
                        ibFlash.setBackgroundResource(R.drawable.ic_flash_gray);
                    }
                    flashMode = !flashMode;
                    camera.startPreview();
                } else {
                    Log.i(TAG, "Camera is NULL!!!!!!!!!");
                }
            }
        }
    }
    public void onCamera(View view) {
        if (Camera.getNumberOfCameras() > 1) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            idCamera = (idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
            if (idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT)
                ibFlash.setVisibility(View.INVISIBLE);
            else {
                ibFlash.setVisibility(View.VISIBLE);
                ibFlash.setBackgroundResource(R.drawable.ic_flash);
            }
            if (camera == null) {
                initCamera();
            }
        }
    }
    private boolean isAppInstalled(String str) {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            Log.d(TAG, str + " installed: true");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, str + " installed: false");
            return false;
        }
    }
    private Bitmap createPhoto(byte[] data){
        Log.i("PHOTO", "CreatePhoto");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        Bitmap res = null;
        if(bitmap != null){
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float dx = 1;
            float dy = 1;
            float scale = 1;
            Log.i(TAG, "Width:"+width+" height:"+height);
/*
            if(width>=2048||height>=2048){
                dx = 2048f/((float)width);
                dy = 2048f/((float)height);
                scale = ((dx<dy)?dx:dy);
                Log.i(TAG, "Scale:"+scale);
            }
            width = Math.round((((float)width)*scale));
            height = Math.round((((float)height)*scale));
            Log.i(TAG, "Width:"+width+" height:"+height);
*/
            //int angle = ((idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT)?-90:90);
            int angle = ((idCamera == Camera.CameraInfo.CAMERA_FACING_FRONT)?90:-90);
            Matrix matrix = new Matrix();
            //matrix.postScale(scale, scale);
            matrix.postRotate(angle);
            res = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            if(settings.isLocation()) drawText(res);
            drawLogo(res);
        }
        return res;
    }
    public void onStopAlarm(View view) {
        //created = true;
        Log.i("PHOTO", "1");
        vibrator.cancel();
        Log.i("PHOTO", "2");
        mMediaPlayer.stop();
        Log.i("PHOTO", "3");
        ibFlash.setVisibility(View.INVISIBLE);
        Log.i("PHOTO", "4");
        ibSpread.setVisibility(View.INVISIBLE);
        Log.i("PHOTO", "5");
 //       sv.setVisibility(View.GONE);
        Log.i("PHOTO", "6");
        ibStop.setVisibility(View.INVISIBLE);
        Log.i("PHOTO", "7");
//        ivPhoto.setVisibility(View.VISIBLE);
        Log.i("PHOTO", "8");
        tvCreateSelfie.setText(getResources().getString(R.string.good_morning));
        Log.i("PHOTO", "9");
        tvCreateSelfie.setTextSize(getResources().getDimension(R.dimen.app_padding_16dp));
        Log.i("PHOTO", "10");
        arrSocial = new ArrayList<Social>();
        Log.i("PHOTO", "11");
        if (alarm.isFacebook()) {
            Log.i("PHOTO", "12");
            Social fb = new Social();
            fb.setID(1);
            fb.setName("Facebook");
            arrSocial.add(fb);
            Log.i("PHOTO", "13");
        }
        if (alarm.isTwitter()) {
            Log.i("PHOTO", "14");
            Social tw = new Social();
            tw.setID(2);
            tw.setName("Twitter");
            arrSocial.add(tw);
            Log.i("PHOTO", "14");
        }
        if (alarm.isVkontakte()) {
            Log.i("PHOTO", "15");
            Social vk = new Social();
            vk.setID(3);
            vk.setName("VKontakte");
            arrSocial.add(vk);
            Log.i("PHOTO", "16");
        }
        if (alarm.isInstagram()) {
            Log.i("PHOTO", "17");
            Social im = new Social();
            im.setID(4);
            im.setName("Facebook");
            arrSocial.add(im);
            Log.i("PHOTO", "18");
        }
        Log.i("PHOTO", "19");

        llSpreadSelfie.setVisibility((settings.isSocial() && (arrSocial.size() > 0))?View.VISIBLE:View.INVISIBLE);
        Log.i("PHOTO", "20");

        Camera.PictureCallback cbCall = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.i("PHOTO", "21");
                Log.d(TAG, "Bitmap length: " + data.length);
                btm = createPhoto(data);

                sv.setVisibility(View.GONE);
                ivPhoto.setVisibility(View.VISIBLE);

                Log.i("PHOTO", "Created");
                Log.i("PHOTO", ""+btm.getWidth()+"x"+btm.getHeight());
                if (btm != null) {
                    ivPhoto.setImageBitmap(btm);
                    uri = getImageUri(btm);
                    created = !(settings.isSocial());
                    if (settings.isSocial()) {
                        if (isNetworkAvailable()) {
                            created = !(arrSocial.size() > 0);
                            publisher(arrSocial);
                        } else {
                            Toast.makeText(CreateSelfieActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                startActivity(new Intent(CreateSelfieActivity.this, MainActivity.class));
                            }
                        }, 5000);
                        //created = true;
                    }
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
        };
        Camera.PictureCallback raw = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.i("PHOTO", "RAW");
            }
        };
        Camera.ShutterCallback stCall = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                Log.i("PHOTO", "Shutter");
            }
        };
        camera.takePicture(stCall, null, cbCall);
        Log.i("PHOTO", "Post Take Photo");
    }
    private Bitmap getPhoto(File photo) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(photo));
        } catch (IOException ex) {
            Log.d(TAG, "getPhoto: " + ex.getMessage());
        }
        return bitmap;
    }
    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }
    void setPreviewSize(boolean fullScreen) {
        // получаем размеры экрана
        Display display = getWindowManager().getDefaultDisplay();
        boolean widthIsMax = display.getWidth() > display.getHeight();

        // определяем размеры превью камеры
        Camera.Size size = camera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        // RectF экрана, соотвествует размерам экрана
        rectDisplay.set(0, 0, display.getWidth(), display.getHeight());

        // RectF первью
        if (widthIsMax) {
            // превью в горизонтальной ориентации
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            // превью в вертикальной ориентации
            rectPreview.set(0, 0, size.height, size.width);
        }

        Matrix matrix = new Matrix();
        // подготовка матрицы преобразования
        if (!fullScreen) {
            // если превью будет "втиснут" в экран
            matrix.setRectToRect(rectPreview, rectDisplay,
                    Matrix.ScaleToFit.START);
        } else {
            // если экран будет "втиснут" в превью
            matrix.setRectToRect(rectDisplay, rectPreview,
                    Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }
        // преобразование
        matrix.mapRect(rectPreview);

        // установка размеров surface из получившегося преобразования
        sv.getLayoutParams().height = (int) (rectPreview.bottom);
        sv.getLayoutParams().width = (int) (rectPreview.right);
    }
    void setCameraDisplayOrientation(int cameraId) {
        // определяем насколько повернут экран от нормального положения
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;

        // получаем инфо по камере cameraId
        CameraInfo info = new CameraInfo();
        if(Camera.getNumberOfCameras()>1) {
            Camera.getCameraInfo(cameraId, info);
        }
        else{
            Camera.getCameraInfo(0, info);
        }
        // задняя камера
        if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
            result = ((360 - degrees) + info.orientation);
        } else
            // передняя камера
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                result = ((360 - degrees) - info.orientation);
                result += 360;
            }
        result = result % 360;
        camera.setDisplayOrientation(result);
        Log.i("PHOTO", "Orientation: "+result);
    }
    private void setMaxPictureSize(Camera cam){
        Camera.Parameters param = cam.getParameters();
        List<Camera.Size> ss = param.getSupportedPictureSizes();
        Collections.sort(ss, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size lhs, Camera.Size rhs) {
                int s1 = lhs.width*lhs.height;
                int s2 = rhs.width*rhs.height;
                return s1-s2;
            }
        });
        Camera.Size s = ss.get(ss.size()-1);
        param.setPictureSize(s.width, s.height);
        cam.setParameters(param);
    }
    private void publishToFacebook() {
        Bitmap image = btm;
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                //.setCaption("Like me!")
                .setCaption(getResources().getString(R.string.publish_message))
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                for(Social ss:arrSocial){
                    if(ss.getID() == 1) {arrSocial.remove(ss);break;}
                }
                publisher(arrSocial);
            }
            @Override
            public void onCancel() {
                for(Social ss:arrSocial){
                    if(ss.getID() == 1) {arrSocial.remove(ss);break;}
                }
                publisher(arrSocial);
            }
            @Override
            public void onError(FacebookException error) {
                for(Social ss:arrSocial){
                    if(ss.getID() == 1) {arrSocial.remove(ss);break;}
                }
                publisher(arrSocial);
            }
        });
    }
    private FacebookCallback<LoginResult> login = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            publishToFacebook();
            Log.i(TAG, "Shared");
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "Cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.i(TAG, "Error: "+error.getMessage());
        }
    };
    public void publishToTwitter(Uri uriImage){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.twitter.android");
        if(intent != null) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(
                    CreateSelfieActivity.this.getResources().getString(R.string.twitter_key),
                    CreateSelfieActivity.this.getResources().getString(R.string.twitter_secret));
            Fabric.with(CreateSelfieActivity.this, new TwitterCore(authConfig), new TweetComposer());
            final TweetComposer.Builder builder = new TweetComposer.Builder(CreateSelfieActivity.this)
                    .text(getResources().getString(R.string.publish_message))
                    .image(uriImage);
            //builder.show();
            Intent intent_tw =  builder.createIntent();
            startActivityForResult(intent_tw, 102);
        }
        else{
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="+"com.twitter.android"));
            startActivityForResult(intent, 103);
        }
    }
    private void publishPhotoToVK(final Bitmap bitmap) {
        VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(bitmap, VKImageParameters.pngImage()), 0, 0);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKApiPhoto photo = ((VKPhotoArray) response.parsedModel).get(0);
                makePhotoOnWallVk(photo);
                //bitmap.recycle();
                for(Social ss:arrSocial){
                    if(ss.getID() == 3) {arrSocial.remove(ss);break;}
                }
                publisher(arrSocial);
            }
            @Override
            public void onError(VKError error) {
                for(Social ss:arrSocial){
                    if(ss.getID() == 3) {arrSocial.remove(ss);break;}
                }
                publisher(arrSocial);
            }
        });
    }
    private void makePhotoOnWallVk(final VKApiPhoto photo) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.ATTACHMENTS, new VKAttachments(photo),
                VKApiConst.MESSAGE, getResources().getString(R.string.publish_message)));
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
    private void publishToInstagram(Uri uri) {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if(intent != null) {
            String path = getRealPathFromURI(uri);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            File media = new File(path);
            Uri shareUri = Uri.fromFile(media);
            share.putExtra(Intent.EXTRA_STREAM, shareUri);
            share.setPackage("com.instagram.android");
            startActivityForResult(share, 100);
        }
        else{
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
            startActivityForResult(intent, 101);
        }
    }
    private void publisher(ArrayList<Social> arr){
        if(arr.isEmpty()) {
            startActivity(new Intent(CreateSelfieActivity.this, MainActivity.class));
            finish();
            return;
        }
        Social curr = arr.get(0);
        if(curr.getID() == 1) publishToFacebook();
        else if(curr.getID() == 2) publishToTwitter(uri);
        else if (curr.getID() == 3) publishPhotoToVK(btm);
        else if(curr.getID() == 4) publishToInstagram(uri);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    private void drawText(Bitmap selphie){
        String result = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude,
                    location.longitude, 1);
            Log.e("Addresses", "-->" + addresses);
            //result = addresses.get(0).getLocality();
            result = addresses.get(0).getAddressLine(3) + ", " + addresses.get(0).getAddressLine(1);
        } catch (Exception ex){

        }

        //if(result.isEmpty()) result = "Alicubi in Terra...";
        if(result.isEmpty()) result = "";

        StringBuilder sb = new StringBuilder();
        Date dd = Calendar.getInstance().getTime();
        String format;
        if(settings.getLocale() == 1) format = "dd.MM.yyyy HH:mm";
        else format = "yyyy-MM-dd HH:mm";

        sb.append(DateTimeOperator.dateToString(dd, format));
        sb.append(" , ");
        sb.append(result);
        String text = sb.toString();

        Canvas canvas = new Canvas(selphie);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(110,110, 110));
        paint.setTextSize((int)(48));
        paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (selphie.getWidth() - bounds.width())/6;
        int y = (selphie.getHeight() - bounds.height());
        canvas.drawText(text, x, y, paint);
    }
    private void drawLogo(Bitmap selphie){
        Canvas canvas = new Canvas(selphie);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(110,110, 110));
        paint.setTextSize((int)(22));
        paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
        Rect bounds = new Rect();
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        int x = selphie.getWidth()-logo.getWidth();
        int y = selphie.getHeight()-logo.getHeight();
        canvas.drawBitmap(logo,x,y,paint);
    }
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null) {
                location = new LatLng(intent.getExtras().getDouble(LocationService.LATITUDE), intent.getExtras().getDouble(LocationService.LONGITUDE));
                Log.i(TAG, ""+location.latitude+","+location.longitude);
            }
        }
    };
}
