package com.dzn.dzn.application.Activities;


import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.dzn.dzn.application.R;
import com.dzn.dzn.application.Utils.PFHandbookProTypeFaces;

public class CreateSelfieActivity extends AppCompatActivity {
    private static final String TAG = "CreateSelfieActivity";

    private TextView tvCreateSelfie;

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

    }


}
