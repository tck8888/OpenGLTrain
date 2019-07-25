package com.tck.camerapreview;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.tck.common.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * <p>description:负责往屏幕上渲染</p>
 * <p>created on: 2019/7/25 13:07</p>
 *
 * @author tck
 * @version 1.0
 */
public class ScreenFilter extends AbstractFilter{


    public ScreenFilter(Context context) {
        super(context,R.raw.base_vertex,R.raw.base_frag);
    }
}
