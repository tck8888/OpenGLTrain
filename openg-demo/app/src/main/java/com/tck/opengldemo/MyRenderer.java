package com.tck.opengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/9 12:50</p>
 *
 * @author tck
 * @version 3.6
 */
public class MyRenderer implements MyEGLSurfaceView.MyRender {

    private static final String TAG = "MyRenderer";

    public MyRenderer() {
    }

    @Override
    public void onSurfaceCreated() {
        Log.d(TAG, "onSurfaceCreated: ");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Log.d(TAG, "onSurfaceChanged: ");
    }

    @Override
    public void onDrawFrame() {
        Log.d(TAG, "onDrawFrame: ");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
       GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
    }
}
