package com.dzn.dzn.application.Activities;


import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.dzn.dzn.application.R;

public class CreateSelfieActivity extends AppCompatActivity {
    private static final String TAG = "CreateSelfieActivity";

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
        camera.release();
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

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


}
