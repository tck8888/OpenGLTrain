package com.tck.opengldemo.muti;

import android.content.Context;
import android.util.AttributeSet;

import com.tck.opengldemo.MyEGLSurfaceView;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/15 15:04</p>
 *
 * @author tck
 * @version 3.6
 */
public class MutiSurfaceVeiw extends MyEGLSurfaceView {

    private MutiRender wlMutiRender;

    public MutiSurfaceVeiw(Context context) {
        this(context, null);
    }

    public MutiSurfaceVeiw(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MutiSurfaceVeiw(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        wlMutiRender = new MutiRender(context);
        setRender(wlMutiRender);
    }

    public void setTextureId(int textureId, int index)
    {
        if(wlMutiRender != null)
        {
            wlMutiRender.setTextureId(textureId, index);
        }
    }
}
