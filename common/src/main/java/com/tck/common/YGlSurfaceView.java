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

    private AbstractBaseRender abstractBaseRender;
    private Context context;

    public YGlSurfaceView(Context context) {
        this(context, null);

    }


    public YGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setEGLContextClientVersion(2);
    }

    public void setAbstractBaseRender(AbstractBaseRender abstractBaseRender) {
        this.abstractBaseRender = abstractBaseRender;

        if (this.abstractBaseRender != null) {
            setRenderer(abstractBaseRender);
        }
    }
}
