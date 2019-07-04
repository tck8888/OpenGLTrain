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

const char *vertex = "attribute vec4 v_Position;\n"
                     "attribute vec2 f_Position;\n"
                     "varying vec2 ft_Position;\n"
                     "void main() {\n"
                     "    ft_Position = f_Position;\n"
                     "    gl_Position = v_Position;\n"
                     "}";


const char *fragment = "precision mediump float;\n"
                       "varying vec2 ft_Position;\n"
                       "uniform sampler2D sTexture;\n"
                       "void main() {\n"
                       "    gl_FragColor=texture2D(sTexture, ft_Position);\n"
                       "}";
int program;

GLint a_position;
GLint f_Position;
GLint sTexture;
GLuint textureId;

int w;
int h;
void *pixels = NULL;

float vertexs[] = {
        1,-1,
        1,1,
        -1,-1,
        -1,1
};

float fragments[] ={
        1,1,
        1,0,
        0,1,
        0,0
};

void callback_surfaceCreated(void *ctx) {
    LOGD("callback_surfaceCreated");

    program = createProgram(vertex, fragment);
    LOGD("opengl program is %d", program);
    a_position = glGetAttribLocation(program, "v_Position");//顶点坐标
    f_Position = glGetAttribLocation(program, "f_Position");//纹理坐标

    sTexture = glGetUniformLocation(program,"sTexture");//2d纹理

    glGenTextures(1,&textureId);

    glBindTexture(GL_TEXTURE_2D,textureId);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,GL_LINEAR);


    if(pixels != NULL)
    {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    glBindTexture(GL_TEXTURE_2D, 0);


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

    glActiveTexture(GL_TEXTURE4);
    glUniform1i(sTexture, 4);

    glBindTexture(GL_TEXTURE_2D, textureId);

    glEnableVertexAttribArray(a_position);
    glVertexAttribPointer(a_position, 2, GL_FLOAT, false, 8, vertexs);

    glEnableVertexAttribArray(f_Position);
    glVertexAttribPointer(f_Position, 2, GL_FLOAT, false, 8, fragments);


    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);


    glBindTexture(GL_TEXTURE_2D, 0);

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

}extern "C"
JNIEXPORT void JNICALL
Java_com_tck_opengltrainhello_NativeOpenGL_imgData(JNIEnv *env, jobject instance, jint width, jint height,
                                                   jint length, jbyteArray data_) {
    jbyte *data = env->GetByteArrayElements(data_, NULL);

    w=width;
    h=height;
    pixels = malloc(length);
    memcpy(pixels, data, length);


    env->ReleaseByteArrayElements(data_, data, 0);
}