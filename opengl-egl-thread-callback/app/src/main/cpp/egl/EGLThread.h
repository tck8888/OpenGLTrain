//
// Created by tck on 2019/7/3.
//

#ifndef OPENGLTRAINOPENGLTRAIN_HELLO_EGLTHREAD_H
#define OPENGLTRAINOPENGLTRAIN_HELLO_EGLTHREAD_H

#include "EGL/egl.h"
#include "GLES2/gl2.h"
#include "pthread.h"
#include "android/native_window.h"
#include "../log/MyLog.h"
#include "EGLHelper.h"
#include "unistd.h"

class EGLThread {

public:
    ANativeWindow *aNativeWindow = NULL;
    pthread_t eglThread = -1;
    bool isCreated = false;
    bool isChange = false;
    bool isExit = false;
    bool isStart = false;

    int width = 0;
    int height = 0;

    typedef void (*OnCreated)(void *);

    OnCreated onCreated;
    void *onCreatedCtx;

    typedef void (*OnChanged)(int width, int height, void *);

    OnChanged onChanged;
    void *onChangedCtx;

    typedef void (*OnDraw)(void *);

    OnDraw onDraw;
    void *onDrawCtx;

public:
    EGLThread();

    ~EGLThread();

    void onSurfaceCreated(EGLNativeWindowType windowType);

    void onSurfaceChanged(int width, int height);

    void callBackOnCreated(OnCreated onCreated, void *ctx);

    void callBackOnChanged(OnChanged onChanged, void *ctx);

    void callBackOnDraw(OnDraw onDraw, void *ctx);


};


#endif //OPENGLTRAINOPENGLTRAIN_HELLO_EGLTHREAD_H
