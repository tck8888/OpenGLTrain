package com.tck.openglcamera.imgvideo;

import android.content.Context;
import android.util.AttributeSet;

import com.tck.openglcamera.egl.WLEGLSurfaceView;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/17 8:25</p>
 *
 * @author tck
 * @version 1.0
 */
public class WlImgVideoView extends WLEGLSurfaceView {
    private WlImgVideoRender wlImgVideoRender;
    private int fbotextureid;

    public WlImgVideoView(Context context) {
        this(context, null);
    }

    public WlImgVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WlImgVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        wlImgVideoRender = new WlImgVideoRender(context);
        setRender(wlImgVideoRender);
        setRenderMode(WLEGLSurfaceView.RENDERMODE_WHEN_DIRTY);
        wlImgVideoRender.setOnRenderCreateListener(new WlImgVideoRender.OnRenderCreateListener() {
            @Override
            public void onCreate(int textid) {
                fbotextureid = textid;
            }
        });
    }

    public void setCurrentImg(int imgsr) {
        if (wlImgVideoRender != null) {
            wlImgVideoRender.setCurrentImgSrc(imgsr);
            requestRender();
        }
    }

    public int getFbotextureid() {
        return fbotextureid;
    }

}
