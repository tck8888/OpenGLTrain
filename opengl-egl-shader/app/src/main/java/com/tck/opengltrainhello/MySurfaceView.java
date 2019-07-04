package com.tck.opengltrainhello;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/3 15:52</p>
 *
 * @author tck
 * @version 3.6
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private NativeOpenGL nativeOpenGL;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (nativeOpenGL != null) {
            nativeOpenGL.surfaceCreated(surfaceHolder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (nativeOpenGL != null) {
            nativeOpenGL.surfaceChanged(i1,i2);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void setNativeOpenGL(NativeOpenGL nativeOpenGL) {
        this.nativeOpenGL = nativeOpenGL;
    }

    public NativeOpenGL getNativeOpenGL() {
        return nativeOpenGL;
    }
}
