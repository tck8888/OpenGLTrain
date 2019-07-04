#include <jni.h>
#include <string>

#include "EGL/egl.h"
#include "GLES2/gl2.h"
#include "android/native_window.h"
#include "android/native_window_jni.h"
#include "egl/EGLThread.h"
#include "shaderutil/ShaderUtil.h"

#include "log/MyLog.h"


ANativeWindow *aNativeWindow = NULL;
EGLThread *eglThread = NULL;

const char *vertex = "attribute vec4 a_position;\n"
                     "\n"
                     "void main(){\n"
                     "    gl_Position = a_position;\n"
                     "}";
const char *fragment = "precision mediump float;\n"
                       "\n"
                       "void main(){\n"
                       "    gl_FragColor = vec4(1f,0f,0f,1f);\n"
                       "}";
int program;

GLint a_position;

float vertexs[] = {
        -1, -1,
        0, -1,
        0, 0,

        1, -1

};

void callback_surfaceCreated(void *ctx) {
    LOGD("callback_surfaceCreated");

    program = createProgram(vertex, fragment);
    LOGD("opengl program is %d", program);
    a_position = glGetAttribLocation(program, "a_position");

}

void callback_surfaceChanged(int w, int h, void *ctx) {
    LOGD("callback_surfaceChanged");
    EGLThread *eglThread = static_cast<EGLThread *>(ctx);
    glViewport(0, 0, w, h);
}

void callback_onDraw(void *ctx) {
    LOGD("callback_onDraw");
    glClearColor(0.0F, 0.0F, 0.0F, 1.0F);

    glClear(GL_COLOR_BUFFER_BIT);

    glUseProgram(program);
    glEnableVertexAttribArray(a_position);
    glVertexAttribPointer(a_position, 2, GL_FLOAT, false, 8, vertexs);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_tck_opengltrainhello_NativeOpenGL_surfaceCreated(JNIEnv *env, jobject instance,
                                                          jobject surface) {


    aNativeWindow = ANativeWindow_fromSurface(env, surface);
    eglThread = new EGLThread();
    eglThread->setRenderType(OPENGL_RENDER_HANDLE);
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