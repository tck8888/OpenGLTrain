package com.tck.camerapreview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 17:42</p>
 *
 * @author tck
 * @version 1.0
 */
public class CameraPreviewActivity extends AppCompatActivity {

    private CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        cameraView = (CameraView) findViewById(R.id.camera_view);

    }
}
