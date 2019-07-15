package com.tck.opengldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/15 13:11</p>
 *
 * @author tck
 * @version 3.6
 */
public class TextureRenderWithVBOAndFBO2 implements MyEGLSurfaceView.MyRender {
    private static final String TAG = "MyRenderFBO";

    private Context context;

    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };
    private FloatBuffer vertexBuffer;

    private float[] fragmentData = {
//            0f, 0f,
//            1f, 0f,
//            0f, 1f,
//            1f, 1f
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };

    private FloatBuffer fragmentBuffer;


    private int program;
    private int vPosition;
    private int fPosition;
    private int textureid;
    private int sampler;

    private int vboId;
    private int fboId;

    private int imgTextureId;

    private FboRender fboRender;

    private int umatrix;
    private float[] matrix = new float[16];

    public TextureRenderWithVBOAndFBO2(Context context) {
        this.context = context;
        fboRender = new FboRender(context);

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);


        fragmentBuffer = ByteBuffer.allocateDirect(fragmentData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(fragmentData);
        fragmentBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated() {
        Log.d(TAG, "onSurfaceCreated: ");
        fboRender.onCreate();

        String vertexSource = ShaderUtils.getRawResource(context, R.raw.vertex_shader_m);
        String fragmentSource = ShaderUtils.getRawResource(context, R.raw.fragment_shader);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);

        vPosition = GLES20.glGetAttribLocation(program, "v_Position");
        fPosition = GLES20.glGetAttribLocation(program, "f_Position");

        sampler = GLES20.glGetUniformLocation(program, "sTexture");
        umatrix = GLES20.glGetUniformLocation(program, "u_Matrix");

        //1.创建vbo
        int[] vbos = new int[1];
        GLES20.glGenBuffers(1, vbos, 0);
        vboId = vbos[0];
        //2.绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);
        //3.分配VBO需要的缓存大小
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4 + fragmentData.length * 4, null, GLES20.GL_STATIC_DRAW);
        //4.为vbo设置顶点数据的值
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertexData.length * 4, vertexBuffer);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4, fragmentData.length * 4, fragmentBuffer);
        //5.解绑VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        //1.创建fbo
        int[] fbos = new int[1];
        GLES20.glGenBuffers(1,fbos,0);
        fboId = fbos[0];
        //2.绑定fbo
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);





        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        textureid = textureIds[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(sampler, 0);


        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //3.设置fbo的内存大小
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,2248 ,1080, 0,GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        //把纹理绑定到FBO
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureid, 0);
        if(GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE)
        {
            Log.e(TAG, "fbo wrong");
        }
        else
        {
            Log.e(TAG, "fbo success");
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        //解绑fbo
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        imgTextureId = loadTexrute(R.drawable.androids);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.d(TAG, "onSurfaceChanged: ");
        GLES20.glViewport(0, 0, width, height);
        fboRender.onChange(width, height);


        if(width > height)
        {
            Matrix.orthoM(matrix, 0, -width / ((height / 702f) * 526f),  width / ((height / 702f) * 526f), -1f, 1f, -1f, 1f);
        }
        else
        {
            Matrix.orthoM(matrix, 0, -1,  1, -height / ((width / 526f) * 702f), height / ((width / 526f) * 702f), -1f, 1f);
        }
        Matrix.rotateM(matrix, 0, 180, 1, 0, 0);
        Matrix.rotateM(matrix, 0, 180, 0, 0, 1);
    }

    @Override
    public void onDrawFrame() {
        Log.d(TAG, "onDrawFrame: ");
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1f, 0f, 0f, 1f);

        GLES20.glUseProgram(program);
        //正交投影
        GLES20.glUniformMatrix4fv(umatrix, 1, false, matrix, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imgTextureId);

        //绑定vbo,使用vbo里面的数据
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);

        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                0);


        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        //解绑vbo
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        fboRender.onDraw(textureid);

    }

    private int loadTexrute(int src){
        int []textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), src);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return  textureIds[0];
    }
}
