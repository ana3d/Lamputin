package com.example.antti.lamputin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.Parameters param;
    ImageButton buttonSwitch;
    boolean isTorchOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        isTorchOn = true;
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        buttonSwitch = (ImageButton) findViewById(R.id.btn_on_off);

        buttonSwitch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                try {
                    if (isTorchOn) {
                        turnOffFlashLight();
                        isTorchOn = false;
                    } else {
                        turnOnFlashLight();
                        isTorchOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void turnOnFlashLight() {

        param = camera.getParameters();
        param.setPreviewSize(800, 600);
        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        //param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setDisplayOrientation(90);
        camera.setParameters(param);
        camera.startPreview();
        buttonSwitch.setImageResource(R.drawable.icon_on);

    }

    public void turnOffFlashLight() {

        param = camera.getParameters();
        param.setPreviewSize(800, 600);
        //param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setDisplayOrientation(90);
        camera.setParameters(param);
        camera.startPreview();

        buttonSwitch.setImageResource(R.drawable.icon_off);

    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        param = camera.getParameters();
        param.setPreviewSize(800, 600);
        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setDisplayOrientation(90);
        camera.setParameters(param);
        isTorchOn = true;

        try {
            camera.setPreviewDisplay(surfaceHolder);

            camera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void showToast(String text) {

        int duration = Toast.LENGTH_LONG;
        Context context = getApplicationContext();

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
}