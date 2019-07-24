package com.tck.common;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 13:54</p>
 *
 * @author tck
 * @version 1.0
 */
public abstract class AbstractBaseRender implements GLSurfaceView.Renderer {

    public Context context;
    public float[] matrix = new float[16];

    public AbstractBaseRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        onDrawFrame();
    }

    protected abstract void onSurfaceCreated();

    protected abstract void onSurfaceChanged(int width, int height);

    protected abstract void onDrawFrame();


    public OnRenderCreateListener onRenderCreateListener;



    public void setOnRenderCreateListener(OnRenderCreateListener onRenderCreateListener) {
        this.onRenderCreateListener = onRenderCreateListener;
    }


    public OnSurfaceCreateListener onSurfaceCreateListener;

    public void setOnSurfaceCreateListener(OnSurfaceCreateListener onSurfaceCreateListener) {
        this.onSurfaceCreateListener = onSurfaceCreateListener;
    }

    public void resetMatrix()
    {
        Matrix.setIdentityM(matrix, 0);
    }

    public void setAngle(float angle, float x, float y, float z)
    {
        Matrix.rotateM(matrix, 0, angle, x, y, z);
    }
}
