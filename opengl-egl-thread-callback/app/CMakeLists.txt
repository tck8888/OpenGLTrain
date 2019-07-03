
cmake_minimum_required(VERSION 3.4.1)


add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp
        src/main/cpp/egl/EGLHelper.cpp
        src/main/cpp/egl/EGLThread.cpp
        )


target_link_libraries( # Specifies the target library.
        native-lib
        egl
        GLESv2
        android
        log)