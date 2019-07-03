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
                LOGD("eglThread call onSurfaceCreated");
                eglThread->isCreated = false;
            }

            if (eglThread->isChange) {
                LOGD("eglThread call onSurfaceChange");
                eglThread->isChange = false;
                glViewport(0, 0, eglThread->width, eglThread->height);
                eglThread->isStart = true;
            }

            if (eglThread->isStart) {
                LOGD("eglThread call onDraw");
                glClearColor(0.0F, 1.0F, 1.0F, 1.0F);
                glClear(GL_COLOR_BUFFER_BIT);
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

    glViewport(0, 0, width, height);
    glClearColor(0.0F, 1.0F, 0.0F, 1.0F);
    glClear(GL_COLOR_BUFFER_BIT);


}
