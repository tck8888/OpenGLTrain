//
// Created by tck on 2019/7/3.
//

#include "EGLHelper.h"

EGLHelper::EGLHelper() {
    eglDisplay = EGL_NO_DISPLAY;
    eglSurface = EGL_NO_SURFACE;
    eglContext = EGL_NO_CONTEXT;
    eglConfig = NULL;
}

EGLHelper::~EGLHelper() {

}

int EGLHelper::initEGL(EGLNativeWindowType windowType) {

    //1.
    eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (eglDisplay == EGL_NO_DISPLAY) {
        LOGE("eglDisplay  error");
        return -1;
    }
    //2.
    EGLint *version = new EGLint[2];
    if (!eglInitialize(eglDisplay, &version[0], &version[1])) {
        LOGE("eglInitialize  error");
        return -1;
    }

    //3.
    const EGLint attrib[] = {
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            EGL_DEPTH_SIZE, 8,
            EGL_STENCIL_SIZE, 8,
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL_NONE
    };
    //config_size, EGLint *num_config
    EGLint num_config;
    if (!eglChooseConfig(eglDisplay, attrib, NULL, 1, &num_config)) {
        LOGE("eglChooseConfig  error 1");
        return -1;
    }

    //4.
    if (!eglChooseConfig(eglDisplay, attrib, &eglConfig, num_config, &num_config)) {
        LOGE("eglChooseConfig  error 2");
        return -1;
    }

    //5.

    int attrib_list[] = {
            EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL_NONE
    };
    eglContext = eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, attrib_list);

    if (eglContext == EGL_NO_CONTEXT) {
        LOGE("eglCreateContext  error");
        return -1;
    }

    //6.
    eglSurface = eglCreateWindowSurface(eglDisplay, eglConfig, windowType, NULL);
    if (eglSurface == EGL_NO_SURFACE) {
        LOGE("eglCreateWindowSurface  error");
        return -1;
    }

    //7.

    if (!eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
        LOGE("eglMakeCurrent  error");
        return -1;
    }

    LOGD("initEGL  success");
    //8.

    //eglSwapBuffers(eglDisplay, eglSurface);

    return 0;
}

int EGLHelper::swapBuffers() {

    if (eglDisplay != EGL_NO_DISPLAY && eglSurface != EGL_NO_SURFACE) {
        if (eglSwapBuffers(eglDisplay, eglSurface)) {
            return 0;
        }
    }

    return -1;
}

void EGLHelper::destoryEGL() {
    if (eglDisplay != EGL_NO_DISPLAY) {
        eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
    }

    if (eglDisplay != EGL_NO_DISPLAY && eglSurface != EGL_NO_SURFACE) {
        eglDestroySurface(eglDisplay, eglSurface);
        eglSurface = EGL_NO_SURFACE;
    }

    if (eglDisplay != EGL_NO_DISPLAY && eglContext != EGL_NO_CONTEXT) {
        eglDestroyContext(eglDisplay, eglContext);
        eglContext = EGL_NO_CONTEXT;
    }

    if (eglDisplay != EGL_NO_DISPLAY) {
        eglTerminate(eglDisplay);
        eglDisplay = EGL_NO_DISPLAY;
    }
}


