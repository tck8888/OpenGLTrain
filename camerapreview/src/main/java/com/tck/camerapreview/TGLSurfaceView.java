package com.tck.camerapreview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/25 12:57</p>
 *
 * @author tck
 * @version 1.0
 */
public class TGLSurfaceView extends GLSurfaceView {

    private TRender renderer;

    public TGLSurfaceView(Context context) {
        this(context, null);
    }

    public TGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);
        renderer = new TRender(this);
        setRenderer(renderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        renderer.onSurfaceDestroyed();
    }
}
