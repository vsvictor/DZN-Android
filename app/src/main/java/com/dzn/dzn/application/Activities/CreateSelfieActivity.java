package com.dzn.dzn.application.Activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

public class CreateSelfieActivity extends AppCompatActivity {
    private static final String TAG = "CreateSelfieActivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_selfie);

        //Initialize view elements
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
        }
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        tvCreateSelfie = (TextView) findViewById(R.id.tvCreateSelfie);
        PFHandbookProTypeFaces.THIN.apply(tvCreateSelfie);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.setDisplayOrientation(90);
                    camera.startPreview();
                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        ibFlash = (ImageButton) findViewById(R.id.ibFlash);
        ibSpread = (ImageButton) findViewById(R.id.ibSpread);
        ibStop = (ImageButton) findViewById(R.id.ibStop);
        llSpreadSelfie = (LinearLayout) findViewById(R.id.llSpreadSelfie);

        tvSelfieSpread = (TextView) findViewById(R.id.tvSelfieSpread);
        PFHandbookProTypeFaces.THIN.apply(tvSelfieSpread);

    }

    /**
     * Click on flash
     * @param view
     */
    public void onFlash(View view) {

    }

    /**
     * Click on camera
     * @param view
     */
    public void onCamera(View view) {

    }

    /**
     * Click on stop alarm
     * @param view
     */
    public void onStopAlarm(View view) {
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
                Bitmap btm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                //set photo
                ivPhoto.setImageBitmap(btm);

                camera.stopPreview();
            }
        });

        //camera.stopPreview();
    }

}
