package com.tck.camerapreview;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraHelper implements Camera.PreviewCallback {

    private static final String TAG = "CameraHelper";
    // public static final int WIDTH = 640;
    //  public static final int HEIGHT = 480;
//目标分辨率尺寸
    private static final float PICTURE_SIZE_PROPORTION = 1.1f;

    private int mCameraId;
    private Camera mCamera;
    private byte[] buffer;
    private Camera.PreviewCallback mPreviewCallback;
    private SurfaceTexture mSurfaceTexture;

    private Camera.Size previewSize;
    private int width;
    private int height;

    public CameraHelper(int cameraId) {
        mCameraId = cameraId;
    }

    public void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        stopPreview();
        startPreview(mSurfaceTexture, width, height);
    }

    public int getCameraId() {
        return mCameraId;
    }

    public void stopPreview() {
        if (mCamera != null) {
            //预览数据回调接口
            mCamera.setPreviewCallback(null);
            //停止预览
            mCamera.stopPreview();
            //释放摄像头
            mCamera.release();
            mCamera = null;
        }
    }

    public void startPreview(SurfaceTexture surfaceTexture, int width, int height) {
        this.width = width;
        this.height = height;
        mSurfaceTexture = surfaceTexture;
        try {
            //获得camera对象
            mCamera = Camera.open(mCameraId);
            //配置camera的属性
            Camera.Parameters parameters = mCamera.getParameters();
            //设置预览数据格式为nv21
            parameters.setPreviewFormat(ImageFormat.NV21);
            //设置自动对焦
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            //获取最接近屏幕分辨率的的预览尺寸
            previewSize = getpreviewSize(parameters, width, height);
            //获取对应比例的最大分辨率
            Camera.Size pictureSize = getPictureSize(parameters, PICTURE_SIZE_PROPORTION);

            //设置预览尺寸
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            //分辨率尺寸
            parameters.setPictureSize(pictureSize.width, pictureSize.height);

            // 设置摄像头 图像传感器的角度、方向
            mCamera.setParameters(parameters);
            // buffer = new byte[WIDTH * HEIGHT * 3 / 2];
            //数据缓存区
            // mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            //设置预览画面
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 计算获得最接近比例的预览尺寸
     *
     * @param parameters
     * @param height
     * @param width
     * @return
     */
    private Camera.Size getpreviewSize(Camera.Parameters parameters, int height, int width) {
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size selectPreviewSize = null; //缓存当前最准确比例的Size
        float currentDifference = 0; //缓存当前最小的差值
        /**
         * 下面这行代码是求传入高度和宽度的高宽比例,这里可以发现一个细节我下面的预览尺寸求的的宽高比例.
         * 是的他们一个是高宽比一个是宽高比,说明为什么这样,因为如果按照2个都是高宽比来获得预览尺寸你会发现,获得的尺寸怎么都有可能会拉伸变形(除非狗屎运尺寸完美刚好)
         * 最好的办法就是,不求最合适目标尺寸的长方形比例,而求一个最适合目标尺寸的正方形比例,这样拉伸变形就不会出现了
         */
        float proportion = (float) height / (float) width;
        for (int i = 0; i < previewSizeList.size(); i++) {
            Camera.Size size = previewSizeList.get(i);
            float previewSizeProportion = ((float) size.width) / ((float) size.height); //计算当前预览尺寸的宽高比例
            float tempDifference = Math.abs(previewSizeProportion - proportion); //相减求绝对值的差值
            if (i == 0) {
                selectPreviewSize = size;
                currentDifference = tempDifference;
                continue;
            }
            if (tempDifference <= currentDifference) { //获得最小差值
                if (tempDifference == currentDifference) { //如果差值一样
                    if ((selectPreviewSize.width + selectPreviewSize.height) < (size.width + size.height)) { //判断那个尺寸大保留那个
                        selectPreviewSize = size;
                        currentDifference = tempDifference;
                    }

                } else { //如果差值更小更准确
                    selectPreviewSize = size;
                    currentDifference = tempDifference;
                }
            }
            // Log.e(TAG, "currentDifference=" + currentDifference + "width=" + selectPreviewSize.width + "height=" + selectPreviewSize.height);

        }
        return selectPreviewSize;
    }


    /**
     * 计算获得最接近比例的分辨率
     *
     * @param parameters
     * @param targetProportion
     * @return
     */
    private Camera.Size getPictureSize(Camera.Parameters parameters, float targetProportion) {
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        Camera.Size selectPreviewSize = null;
        float currentDifference = 0;
        for (int i = 0; i < pictureSizeList.size(); i++) {
            Camera.Size size = pictureSizeList.get(i);
            // Log.e(TAG, "分辨率列表_" + i + "_width=" + size.width + "height=" + size.height);
            float pictureSizeProportion = ((float) size.width) / ((float) size.height);
            // Log.e(TAG, "分辨率列表_" + i + "_比例=" + pictureSizeProportion);
            float tempDifference = Math.abs(pictureSizeProportion - targetProportion);
            if (i == 0) {
                selectPreviewSize = size;
                currentDifference = tempDifference;
                continue;
            }
            if (tempDifference <= currentDifference) {
                if (tempDifference == currentDifference) {
                    if ((selectPreviewSize.width + selectPreviewSize.height) < (size.width + size.height)) {
                        //判断那个尺寸大保留那个
                        selectPreviewSize = size;
                        currentDifference = tempDifference;
                    }
                } else { //如果差值更小更准确
                    selectPreviewSize = size;
                    currentDifference = tempDifference;

                }
            }

        }
        //Log.e(TAG, "当前选择分辨率width=" + selectPreviewSize.width + "height=" + selectPreviewSize.height);
        return selectPreviewSize;
    }


    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // data数据依然是倒的
        if (null != mPreviewCallback) {
            mPreviewCallback.onPreviewFrame(data, camera);
        }
        //   camera.addCallbackBuffer(buffer);
    }


}
