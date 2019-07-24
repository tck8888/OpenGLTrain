package com.tck.common;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 13:35</p>
 *
 * @author tck
 * @version 1.0
 */
public class YGlSurfaceView extends GLSurfaceView {
    public YGlSurfaceView(Context context) {
        super(context);
        init(context);
    }


    public YGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        //setRender(new WlRender());
       // setRenderMode(WLEGLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
