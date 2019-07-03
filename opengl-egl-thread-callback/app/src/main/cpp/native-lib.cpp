#include <jni.h>
#include <string>

#include "EGL/egl.h"
#include "GLES2/gl2.h"
#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "egl/EGLThread.h"

#include "log/MyLog.h"


ANativeWindow *aNativeWindow = NULL;
EGLThread *eglThread = NULL;


void callback_surfaceCreated(void *ctx) {
    LOGD("callback_surfaceCreated");
}

void callback_surfaceChanged(int w, int h, void *ctx) {
    LOGD("callback_surfaceChanged");
    EGLThread *eglThread = static_cast<EGLThread *>(ctx);
    glViewport(0,0,w,h);
}

void callback_onDraw(void *ctx) {
    LOGD("callback_onDraw");
    glClearColor(1.0F,0.0F,0.0F,1.0F);
    glClear(GL_COLOR_BUFFER_BIT);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_tck_opengltrainhello_NativeOpenGL_surfaceCreated(JNIEnv *env, jobject instance,
                                                          jobject surface) {


    aNativeWindow = ANativeWindow_fromSurface(env, surface);
    eglThread = new EGLThread();
    eglThread->callBackOnCreated(callback_surfaceCreated, eglThread);
    eglThread->callBackOnChanged(callback_surfaceChanged, eglThread);
    eglThread->callBackOnDraw(callback_onDraw, eglThread);

    eglThread->onSurfaceCreated(aNativeWindow);


}extern "C"
JNIEXPORT void JNICALL
Java_com_tck_opengltrainhello_NativeOpenGL_surfaceChanged(JNIEnv *env, jobject instance, jint width,
                                                          jint height) {


    if (eglThread != NULL) {
        eglThread->onSurfaceChanged(width, height);
    }

}