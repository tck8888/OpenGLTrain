package com.tck.openglcamera;

import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tck.openglcamera.camera.WlCameraView;
import com.tck.openglcamera.encodec.WlBaseMediaEncoder;
import com.tck.openglcamera.encodec.WlMediaEncodec;
import com.tck.openglcamera.util.DisplayUtil;


public class VideoActivity extends AppCompatActivity {


    private WlCameraView wlCameraView;
    private Button btnRecord;

    private WlMediaEncodec wlMediaEncodec;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        wlCameraView = findViewById(R.id.cameraview);
        btnRecord = findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record();
            }
        });
    }

    public void record() {

        if(wlMediaEncodec == null)
        {
            Log.d("ywl5320", "textureid is " + wlCameraView.getTextureId());
            wlMediaEncodec = new WlMediaEncodec(this, wlCameraView.getTextureId());
            wlMediaEncodec.initEncodec(wlCameraView.getEglContext(),
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/wl_live_pusher.mp4", MediaFormat.MIMETYPE_VIDEO_AVC, DisplayUtil.getScreenWidth(this), DisplayUtil.getScreenHeight(this));
            wlMediaEncodec.setOnMediaInfoListener(new WlBaseMediaEncoder.OnMediaInfoListener() {
                @Override
                public void onMediaTime(int times) {
                    Log.d("ywl5320", "time is : " + times);
                }
            });

            wlMediaEncodec.startRecord();
            btnRecord.setText("正在录制");
        }
        else
        {
            wlMediaEncodec.stopRecord();
            btnRecord.setText("开始录制");
            wlMediaEncodec = null;
        }


    }
}
