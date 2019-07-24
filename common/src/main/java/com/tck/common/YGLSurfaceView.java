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
public class YGLSurfaceView extends GLSurfaceView {

    public AbstractBaseRender abstractBaseRender;


    public YGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
    }

    public YGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
    }

    public void setAbstractBaseRender(AbstractBaseRender abstractBaseRender) {
        this.abstractBaseRender = abstractBaseRender;

        if (this.abstractBaseRender != null) {
            setRenderer(abstractBaseRender);
        }
    }
}
