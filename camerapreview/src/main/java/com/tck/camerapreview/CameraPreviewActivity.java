package com.tck.camerapreview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 17:42</p>
 *
 * @author tck
 * @version 1.0
 */
public class CameraPreviewActivity extends AppCompatActivity {

    private TGLSurfaceView cameraView;
    private Button btnSavePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        cameraView = (TGLSurfaceView) findViewById(R.id.camera_view);
        btnSavePic = (Button) findViewById(R.id.btn_save_pic);

        btnSavePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.savePic();
            }
        });
    }
}
