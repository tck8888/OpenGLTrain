//
// Created by tck on 2019/7/3.
//

#ifndef OPENGLTRAINOPENGLTRAIN_HELLO_EGLHELPER_H
#define OPENGLTRAINOPENGLTRAIN_HELLO_EGLHELPER_H


#include "EGL/egl.h"
#include "../log/MyLog.h"

class EGLHelper {

public:
    EGLDisplay eglDisplay;
    EGLSurface eglSurface;
    EGLConfig eglConfig;
    EGLContext eglContext;
public:
    EGLHelper();

    ~EGLHelper();

    int initEGL(EGLNativeWindowType windowType);

    int swapBuffers();

    void destoryEGL();

};


#endif //OPENGLTRAINOPENGLTRAIN_HELLO_EGLHELPER_H
