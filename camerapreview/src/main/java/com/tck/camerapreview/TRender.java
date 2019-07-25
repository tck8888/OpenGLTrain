package com.tck.camerapreview;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/25 12:59</p>
 *
 * @author tck
 * @version 1.0
 */
public class TRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private ScreenFilter mScreenFilter;
    private CameraFilter mCameraFilter;
    private CameraHelper mCameraHelper;
    private SurfaceTexture mSurfaceTexture;
    private float[] mtx = new float[16];
    private int[] mTextures;
    private TGLSurfaceView tglSurfaceView;
    private BeautyFilter beautyFilter;


    public TRender(TGLSurfaceView tglSurfaceView) {
        this.tglSurfaceView = tglSurfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //初始化的操作
        mCameraHelper = new CameraHelper(Camera.CameraInfo.CAMERA_FACING_FRONT);
        //准备好摄像头绘制的画布
        //通过opengl创建一个纹理id
        mTextures = new int[1];
        //偷懒 这里可以不配置 （当然 配置了也可以）
        GLES20.glGenTextures(mTextures.length, mTextures, 0);
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        //
        mSurfaceTexture.setOnFrameAvailableListener(this);
        //注意：必须在gl线程操作opengl
        mCameraFilter = new CameraFilter(tglSurfaceView.getContext());
        mScreenFilter = new ScreenFilter(tglSurfaceView.getContext());
        beautyFilter = new BeautyFilter(tglSurfaceView.getContext());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//开启预览
        mCameraHelper.startPreview(mSurfaceTexture,width,height);
        mCameraFilter.onReady(width,height);
        mScreenFilter.onReady(width,height);
        beautyFilter.onReady(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 配置屏幕
        //清理屏幕 :告诉opengl 需要把屏幕清理成什么颜色
        GLES20.glClearColor(0, 0, 0, 0);
        //执行上一个：glClearColor配置的屏幕颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 把摄像头的数据先输出来
        // 更新纹理，然后我们才能够使用opengl从SurfaceTexure当中获得数据 进行渲染
        mSurfaceTexture.updateTexImage();
        //surfaceTexture 比较特殊，在opengl当中 使用的是特殊的采样器 samplerExternalOES （不是sampler2D）
        //获得变换矩阵
        mSurfaceTexture.getTransformMatrix(mtx);
        //
        mCameraFilter.setMatrix(mtx);
        //责任链
        int id = mCameraFilter.onDrawFrame(mTextures[0]);
        //加效果滤镜
        // id  = 效果1.onDrawFrame(id);
        // id = 效果2.onDrawFrame(id);
        //....
        //加完之后再显示到屏幕中去
        int beautyId = beautyFilter.onDrawFrame(id);

        mScreenFilter.onDrawFrame(beautyId);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        tglSurfaceView.requestRender();
    }

    public void onSurfaceDestroyed() {
        mCameraHelper.stopPreview();
    }
}
