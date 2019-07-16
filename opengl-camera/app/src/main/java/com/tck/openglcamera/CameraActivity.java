package com.tck.openglcamera;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tck.openglcamera.camera.WlCameraView;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/15 15:40</p>
 *
 * @author tck
 * @version 3.6
 */
public class CameraActivity extends AppCompatActivity {


    private WlCameraView wlCameraView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        wlCameraView = findViewById(R.id.cameraview);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wlCameraView.onDestory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        wlCameraView.previewAngle(this);
    }
}
