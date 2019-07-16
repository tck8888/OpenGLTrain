package com.tck.openglcamera.encodec;

import android.content.Context;


/**
 * <p>description:</p>
 * <p>created on: 2019/7/16 9:41</p>
 *
 * @author tck
 * @version 3.6
 */
public class WlMediaEncodec extends WlBaseMediaEncoder{

    private WlEncodecRender wlEncodecRender;

    public WlMediaEncodec(Context context, int textureId) {
        super(context);
        wlEncodecRender = new WlEncodecRender(context, textureId);
        setRender(wlEncodecRender);
        setmRenderMode(WlBaseMediaEncoder.RENDERMODE_CONTINUOUSLY);
    }
}
