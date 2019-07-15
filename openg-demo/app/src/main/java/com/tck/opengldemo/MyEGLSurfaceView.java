package com.tck.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGLContext;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/9 12:49</p>
 *
 * @author tck
 * @version 3.6
 */
public class MyEGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {


    public final static int RENDERMODE_WHEN_DIRTY = 0;
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    private Surface surface;
    private EGLContext eglContext;

    private MyGLThread myGLThread;
    private MyRender myRender;

    private int mRenderMode = RENDERMODE_WHEN_DIRTY;


    public MyEGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyEGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyEGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    public void setSurfaceAndEGLContext(Surface surface, EGLContext eglContext) {
        this.surface = surface;
        this.eglContext = eglContext;
    }

    public void setRenderMode(int mRenderMode) {
        if (myRender == null) {

            throw new RuntimeException("must set Render");
        }
        this.mRenderMode = mRenderMode;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (surface == null) {
            surface = holder.getSurface();
        }

        myGLThread = new MyGLThread(new WeakReference<MyEGLSurfaceView>(this));
        myGLThread.isCreate = true;
        myGLThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        myGLThread.width = width;
        myGLThread.height = height;
        myGLThread.isChange = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        myGLThread.onDestroy();
        myGLThread = null;
        surface = null;
        eglContext = null;
    }


    public interface MyRender {
        void onSurfaceCreated();

        void onSurfaceChanged(int width, int height);

        void onDrawFrame();

    }

    public void setRender(MyRender myRender) {
        this.myRender = myRender;
    }


    public EGLContext getEGLContext() {
        if (myGLThread != null) {
            return myGLThread.getEGLContext();
        }
        return null;
    }

    private void requestRender() {
        if (myGLThread != null) {
             myGLThread.requestRender();
        }
    }



   public static class MyGLThread extends Thread {

        private WeakReference<MyEGLSurfaceView> surfaceViewWeakReference;
        private EglHelper eglHelper = null;
        private boolean isExit = false;
        private boolean isCreate = false;
        private boolean isChange = false;
        private boolean isStart = false;

        private int width;
        private int height;
        private GLSurfaceView d;
        private Object object;


        public MyGLThread(WeakReference<MyEGLSurfaceView> surfaceViewWeakReference) {
            this.surfaceViewWeakReference = surfaceViewWeakReference;
        }

        @Override
        public void run() {
            super.run();
            isExit = false;
            isStart = false;
            object = new Object();
            eglHelper = new EglHelper();
            eglHelper.initEGL(surfaceViewWeakReference.get().surface, surfaceViewWeakReference.get().eglContext);
            while (true) {

                if (isExit) {
                    release();
                    break;
                }
                if (isStart) {
                    if (surfaceViewWeakReference.get().mRenderMode == RENDERMODE_WHEN_DIRTY) {
                        synchronized (object) {
                            try {
                                object.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (surfaceViewWeakReference.get().mRenderMode == RENDERMODE_CONTINUOUSLY) {
                        try {
                            Thread.sleep(1000 / 60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                onCreate();

                onChange(width, height);

                onDraw();

                isStart = true;
            }
        }


        private void onCreate() {
            if (isCreate && surfaceViewWeakReference.get().myRender != null) {
                isCreate = false;
                surfaceViewWeakReference.get().myRender.onSurfaceCreated();
            }
        }

        private void onChange(int width, int height) {
            if (isChange && surfaceViewWeakReference.get().myRender != null) {
                isChange = false;
                surfaceViewWeakReference.get().myRender.onSurfaceChanged(width, height);
            }
        }

        private void onDraw() {
            if (surfaceViewWeakReference.get().myRender != null && eglHelper != null) {
                surfaceViewWeakReference.get().myRender.onDrawFrame();
                if (!isStart) {
                    surfaceViewWeakReference.get().myRender.onDrawFrame();
                }

                eglHelper.swapBuffers();
            }
        }

        private void requestRender() {
            if (object != null) {
                synchronized (object) {
                    object.notifyAll();
                }
            }
        }

        public void onDestroy() {
            isExit = true;
            requestRender();
        }

        public void release() {
            if (eglHelper != null) {
                eglHelper.destoryEGL();
                eglHelper = null;
                object = null;
                surfaceViewWeakReference = null;
            }
        }

        public EGLContext getEGLContext() {
            if (eglHelper != null) {
                return eglHelper.getEGLContext();
            }
            return null;
        }
    }
}
