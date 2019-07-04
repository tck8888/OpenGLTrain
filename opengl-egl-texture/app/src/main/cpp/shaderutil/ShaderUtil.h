//
// Created by tck on 2019/7/4.
//

#ifndef OPENGL_EGL_SHADER_SHADERUTIL_H
#define OPENGL_EGL_SHADER_SHADERUTIL_H

#include "GLES2/gl2.h"


static int loadShaders(int shaderType, const char *code)
{
    int shader = glCreateShader(shaderType);
    glShaderSource(shader, 1, &code, 0);
    glCompileShader(shader);
    return  shader;
}

static int createProgram(const char *vertex , const char * fragment)
{
    int vertexShader = loadShaders(GL_VERTEX_SHADER, vertex);
    int fragmentShader = loadShaders(GL_FRAGMENT_SHADER, fragment);
    int program = glCreateProgram();
    glAttachShader(program, vertexShader);
    glAttachShader(program, fragmentShader);
    glLinkProgram(program);
    return program;
}


#endif //OPENGL_EGL_SHADER_SHADERUTIL_H
