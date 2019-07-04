//
// Created by tck on 2019/7/3.
//

#ifndef OPENGLTRAINOPENGLTRAIN_HELLO_MYLOG_H
#define OPENGLTRAINOPENGLTRAIN_HELLO_MYLOG_H

#include "android/log.h"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,"egl===",__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,"egl===",__VA_ARGS__)

#endif //OPENGLTRAINOPENGLTRAIN_HELLO_MYLOG_H
