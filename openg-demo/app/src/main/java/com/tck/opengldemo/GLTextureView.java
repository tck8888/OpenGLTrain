package com.tck.opengldemo;

import android.content.Context;
import android.util.AttributeSet;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/15 12:32</p>
 *
 * @author tck
 * @version 3.6
 */
public class GLTextureView extends MyEGLSurfaceView{
    public GLTextureView(Context context) {
        this(context,null);
    }

    public GLTextureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GLTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRender(new TextureRenderWithVBO(context));
    }
}
