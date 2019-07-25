package com.tck.common;

import android.content.Context;
import android.opengl.GLES20;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 13:20</p>
 *
 * @author tck
 * @version 1.0
 */
public class ShaderUtils {

    public static String getRawResource(Context context, int rawId) {
        InputStream inputStream = context.getResources().openRawResource(rawId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);

            int[] compile = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compile, 0);
            if (compile[0] != GLES20.GL_TRUE) {
                MyLog.d("shader compile error");
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
            return shader;
        } else {
            return 0;
        }

    }

    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        return createProgram(vertexShader, fragmentShader);
    }

    public static int createProgram(Context context, int vertexSourceRawId, int fragmentSourceRawId) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getRawResource(context, vertexSourceRawId));
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, getRawResource(context, fragmentSourceRawId));
        return createProgram(vertexShader, fragmentShader);
    }

    private static int createProgram(int vertexShader, int fragmentShader) {
        if (vertexShader != 0 && fragmentShader != 0) {
            int program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);
            return program;
        }
        return 0;
    }

    /**
     * 创建纹理并配置
     */
    public static void glGenTextures(int[] textures){
        //创建
        GLES20.glGenTextures(textures.length, textures, 0);
        //配置
        for (int i = 0; i < textures.length; i++) {
            // opengl的操作 面向过程的操作
            //bind 就是绑定 ，表示后续的操作就是在这一个 纹理上进行
            // 后面的代码配置纹理，就是配置bind的这个纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[i]);
            /**
             * 过滤参数
             *  当纹理被使用到一个比他大 或者比他小的形状上的时候 该如何处理
             */
            // 放大
            // GLES20.GL_LINEAR  : 使用纹理中坐标附近的若干个颜色，通过平均算法 进行放大
            // GLES20.GL_NEAREST : 使用纹理坐标最接近的一个颜色作为放大的要绘制的颜色
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);

            /*设置纹理环绕方向*/
            //纹理坐标 一般用st表示，其实就是x y
            //纹理坐标的范围是0-1。超出这一范围的坐标将被OpenGL根据GL_TEXTURE_WRAP参数的值进行处理
            //GL_TEXTURE_WRAP_S, GL_TEXTURE_WRAP_T 分别为x，y方向。
            //GL_REPEAT:平铺
            //GL_MIRRORED_REPEAT: 纹理坐标是奇数时使用镜像平铺
            //GL_CLAMP_TO_EDGE: 坐标超出部分被截取成0、1，边缘拉伸
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

            //解绑
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        }
    }

}
