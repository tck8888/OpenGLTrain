package com.tck.opengldemo;

import android.content.Context;
import android.util.AttributeSet;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/15 8:02</p>
 *
 * @author tck
 * @version 3.6
 */
public class MyGLSurfaceView extends MyEGLSurfaceView {
    public MyGLSurfaceView(Context context) {
        this(context,null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRender(new MyRenderer());
    }
}
