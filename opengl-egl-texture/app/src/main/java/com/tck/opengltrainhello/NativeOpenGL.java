package com.tck.opengltrainhello;

import android.view.Surface;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/3 15:50</p>
 *
 * @author tck
 * @version 3.6
 */
public class NativeOpenGL {

   static  {
        System.loadLibrary("native-lib");
    }

    public native void surfaceCreated(Surface surface);
    public native void surfaceChanged(int width ,int height);
    public native void imgData(int w, int h, int length, byte[] data);

}
