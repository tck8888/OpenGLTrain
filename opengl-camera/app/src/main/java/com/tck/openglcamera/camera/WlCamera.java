package com.tck.openglcamera.camera;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class WlCamera {

    private static final String TAG = "WlCamera";
    private Camera camera;

    public WlCamera() {
    }

    private SurfaceTexture surfaceTexture;

    public void initCamera(SurfaceTexture surfaceTexture, int cameraId) {
        this.surfaceTexture = surfaceTexture;
        setCameraParm(cameraId);

    }

    private void setCameraParm(int cameraId) {
        try {
            camera = Camera.open(cameraId);
            camera.setPreviewTexture(surfaceTexture);

            Camera.Parameters parameters = camera.getParameters();

            //设置自动对焦
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            parameters.setFlashMode("off");
            parameters.setPreviewFormat(ImageFormat.NV21);
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
            for (int i = 0; i < supportedPictureSizes.size(); i++) {
                Log.d(TAG, "setCameraParm: supportedPictureSizes width= " + supportedPictureSizes.get(i).width + " height= " + supportedPictureSizes.get(i).height);
            }

            parameters.setPictureSize(supportedPictureSizes.get(0).width,
                    supportedPictureSizes.get(0).height);
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            Log.d(TAG, "setCameraParm: ==================");
            for (int i = 0; i < supportedPreviewSizes.size(); i++) {
                Log.d(TAG, "setCameraParm: supportedPreviewSizes width= " + supportedPreviewSizes.get(i).width + " height= " + supportedPreviewSizes.get(i).height);
            }

            parameters.setPreviewSize(supportedPreviewSizes.get(0).width,
                    supportedPreviewSizes.get(0).height);
            camera.setParameters(parameters);
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        if (camera != null) {
            camera.startPreview();
            camera.release();
            camera = null;
        }
    }

    public void changeCamera(int cameraId) {
        if (camera != null) {
            stopPreview();
        }
        setCameraParm(cameraId);
    }

}
