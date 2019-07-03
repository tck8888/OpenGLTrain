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

extern "C"
JNIEXPORT void JNICALL
Java_com_tck_opengltrainhello_NativeOpenGL_surfaceCreated(JNIEnv *env, jobject instance,
                                                          jobject surface) {


    aNativeWindow = ANativeWindow_fromSurface(env, surface);
    eglThread = new EGLThread();

    eglThread->onSurfaceCreated(aNativeWindow);


}extern "C"
JNIEXPORT void JNICALL
Java_com_tck_opengltrainhello_NativeOpenGL_surfaceChanged(JNIEnv *env, jobject instance, jint width,
                                                          jint height) {


    if (eglThread != NULL) {
        eglThread->onSurfaceChanged(width, height);
    }

}