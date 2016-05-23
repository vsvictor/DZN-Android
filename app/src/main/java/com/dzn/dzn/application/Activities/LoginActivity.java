package com.dzn.dzn.application.Activities;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzn.dzn.application.Objects.Alarm;
import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.DataBaseHelper;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;
import com.vk.sdk.util.VKUtil;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tested class for publish photo
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //Integrate VK
    private static final String[] sVkScope = new String[] {
            VKScope.WALL,
            VKScope.PHOTOS
    };


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
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private DataBaseHelper dataBaseHelper;

    private boolean created = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        //Initialize Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        getHashKeyFacebook();

        setContentView(R.layout.activity_login);
        dataBaseHelper = DataBaseHelper.getInstance(this);

        //Initialize VK
        getVKCertificate();
        //VKSdk.login(LoginActivity.this, sVkScope);

        if (!VKSdk.wakeUpSession(this)) {
            Log.d(TAG, "VK authorize");
            VKSdk.login(LoginActivity.this, sVkScope);
        }


        //Initialize view elements
        initView();

        //Initialize Surface
        initSurface();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (camera == null) {
            Log.d(TAG, "Camera null");
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.setDisplayOrientation(90);
                camera.startPreview();
            } catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                Log.d(TAG, "VK login success");
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            //super.onActivityResult(requestCode, resultCode, data);
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
        ibFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                    Camera.Parameters p = camera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(p);
                    camera.startPreview();
                }
            }
        });
        ibSpread = (ImageButton) findViewById(R.id.ibSpread);
        ibStop = (ImageButton) findViewById(R.id.ibStop);
        llSpreadSelfie = (LinearLayout) findViewById(R.id.llSpreadSelfie);

        tvSelfieSpread = (TextView) findViewById(R.id.tvSelfieSpread);
        PFHandbookProTypeFaces.THIN.apply(tvSelfieSpread);
        }

    //Initialize SurfaceView & SurfaceHolder
    private void initSurface() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

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
                    Log.d(TAG, "surfaceCreated exception: " + ex.getMessage());
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

    }

    /**
     * Click on camera
     *
     * @param view
     */
    public void onCamera(View view) {

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

        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d(TAG, "Bitmap length: " + data.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                //rotate bitmap
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                btm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                //set photo
                ivPhoto.setImageBitmap(btm);

                callbackManager = CallbackManager.Factory.create();
                loginManager = LoginManager.getInstance();
                loginManager.logInWithPublishPermissions(LoginActivity.this, permissions);
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


                sharePhotoToVK(btm);
                camera.stopPreview();
            }
        });

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
        //finish();
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

    private void sharePhotoToVK(final Bitmap bitmap) {
        VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), 0, 6400000);

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                bitmap.recycle();
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                //Make post with photo
            }
            @Override
            public void onError(VKError error) {
                Log.d(TAG, "Error: " + error.errorMessage);
            }
        });

        /**
        VKPhotoArray photos = new VKPhotoArray();
        photos.add(new VKApiPhoto("My photo"));
        new VKShareDialogBuilder()
                .setText("My new photo")
                .setUploadedPhotos(photos)
                .setAttachmentImages(new VKUploadImage[]{
                        new VKUploadImage(bitmap, VKImageParameters.pngImage())
                })
                .setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
                    @Override
                    public void onVkShareComplete(int postId) {
                        //контент отправлен
                        Log.d(TAG, "onVkShareComplete");
                    }

                    @Override
                    public void onVkShareCancel() {
                        //отмена
                        Log.d(TAG, "onVkShareCancel");
                    }

                    @Override
                    public void onVkShareError(VKError error) {
                        Log.d(TAG, "onVkShareError");
                    }
                }).show(getFragmentManager(), "VK_SHARE_DIALOG");
         */
    }

}
