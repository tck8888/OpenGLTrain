package com.tck.camerapreview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import com.tck.common.MyLog;
import com.tck.common.OnSurfaceCreateListener;
import com.tck.common.YGLSurfaceView;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 17:59</p>
 *
 * @author tck
 * @version 1.0
 */
public class CameraView extends YGLSurfaceView {

    private YCamera yCamera;

    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setAbstractBaseRender(new CameraRender(context));
        yCamera = new YCamera();
        previewAngle(context);
        abstractBaseRender.setOnSurfaceCreateListener(new OnSurfaceCreateListener() {
            @Override
            public void onSurfaceCreate(SurfaceTexture surfaceTexture) {
                yCamera.initCamera(surfaceTexture, cameraId);
            }
        });

    }

    public void onDestory() {
        if (yCamera != null) {
            yCamera.stopPreview();
        }
    }

    public void previewAngle(Context context)
    {
        int angle = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        abstractBaseRender.resetMatrix();
        switch (angle)
        {
            case Surface.ROTATION_0:
               MyLog.d( "0");
                if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                    abstractBaseRender.setAngle(90, 0, 0, 1);
                    abstractBaseRender.setAngle(180, 1, 0, 0);
                }
                else
                {
                    abstractBaseRender.setAngle(90f, 0f, 0f, 1f);
                }

                break;
            case Surface.ROTATION_90:
               MyLog.d( "90");
                if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                    abstractBaseRender.setAngle(180, 0, 0, 1);
                    abstractBaseRender.setAngle(180, 0, 1, 0);
                }
                else
                {
                    abstractBaseRender.setAngle(90f, 0f, 0f, 1f);
                }
                break;
            case Surface.ROTATION_180:
               MyLog.d( "180");
                if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                    abstractBaseRender.setAngle(90f, 0.0f, 0f, 1f);
                    abstractBaseRender.setAngle(180f, 0.0f, 1f, 0f);
                }
                else
                {
                    abstractBaseRender.setAngle(-90, 0f, 0f, 1f);
                }
                break;
            case Surface.ROTATION_270:
               MyLog.d( "270");
                if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                    abstractBaseRender.setAngle(180f, 0.0f, 1f, 0f);
                }
                else
                {
                    abstractBaseRender.setAngle(0f, 0f, 0f, 1f);
                }
                break;
        }
    }
}
