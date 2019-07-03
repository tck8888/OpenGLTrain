//
// Created by tck on 2019/7/3.
//

#include "EGLThread.h"

EGLThread::EGLThread() {


}

EGLThread::~EGLThread() {

}

//回调函数
void *eglThreadImpl(void *args) {
    EGLThread *eglThread = static_cast<EGLThread *>(args);

    if (eglThread != NULL) {
        EGLHelper *eglHelper = new EGLHelper();
        eglHelper->initEGL(eglThread->aNativeWindow);
        eglThread->isExit = false;
        while (true) {
            if (eglThread->isCreated) {
                eglThread->isCreated = false;

                eglThread->onCreated(eglThread->onCreatedCtx);
            }

            if (eglThread->isChange) {
                eglThread->isChange = false;
                eglThread->onChanged(eglThread->width, eglThread->height, eglThread->onChangedCtx);
                eglThread->isStart = true;
            }

            if (eglThread->isStart) {
                eglThread->onDraw(eglThread->onDrawCtx);
                eglHelper->swapBuffers();
            }

            usleep(100000 / 60);

            if (eglThread->isExit) {
                break;
            }

        }
    }
    return 0;
}

void EGLThread::onSurfaceCreated(EGLNativeWindowType windowType) {

    if (eglThread == -1) {
        isCreated = true;
        aNativeWindow = windowType;

        pthread_create(&eglThread, NULL, eglThreadImpl, this);
    }


}

void EGLThread::onSurfaceChanged(int width, int height) {
    this->isChange = true;
    this->width = width;
    this->height = height;
}

//回调方法
void EGLThread::callBackOnCreated(EGLThread::OnCreated onCreated, void *ctx) {
    this->onCreated = onCreated;
    this->onCreatedCtx = ctx;

}

//回调方法
void EGLThread::callBackOnChanged(EGLThread::OnChanged onChanged, void *ctx) {
    this->onChanged = onChanged;
    this->onChangedCtx = ctx;
}


//回调方法
void EGLThread::callBackOnDraw(EGLThread::OnDraw onDraw, void *ctx) {
    this->onDraw = onDraw;
    this->onDrawCtx = ctx;

}

