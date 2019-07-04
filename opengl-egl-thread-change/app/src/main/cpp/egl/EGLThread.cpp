//
// Created by tck on 2019/7/3.
//

#include "EGLThread.h"

EGLThread::EGLThread() {

    pthread_mutex_init(&pthread_mutex, NULL);
    pthread_cond_init(&pthread_cond, NULL);

}

EGLThread::~EGLThread() {
    pthread_mutex_destroy(&pthread_mutex);
    pthread_cond_destroy(&pthread_cond);

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
            if (eglThread->renderType == OPENGL_RENDER_AUTO) {
                usleep(100000 / 60);
            } else {
                pthread_mutex_lock(&eglThread->pthread_mutex);
                pthread_cond_wait(&eglThread->pthread_cond,&eglThread->pthread_mutex);
                pthread_mutex_unlock(&eglThread->pthread_mutex);
            }


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

void EGLThread::setRenderType(int renderType) {
    this->renderType = renderType;
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

void EGLThread::notifyRender() {
    pthread_mutex_lock(&pthread_mutex);
    pthread_cond_signal(&pthread_cond);
    pthread_mutex_unlock(&pthread_mutex);
}



