package com.tck.camerapreview;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

/**
 * <p>description:</p>
 * <p>created on: 2019/7/25 15:02</p>
 *
 * @author tck
 * @version 1.0
 */
public class MagicBeautyFilter extends AbstractFrameFilter {

    private int mSingleStepOffsetLocation;
    private int mParamsLocation;
    private int mLevel;

    public MagicBeautyFilter(Context context) {
        super(context, R.raw.base_vertex, R.raw.beauty);
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(mGLProgramId, "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(mGLProgramId, "params");
       }

    @Override
    public int onDrawFrame(int textureId) {
        //设置显示窗口
        GLES20.glViewport(0, 0, mOutputWidth, mOutputHeight);

        //不调用的话就是默认的操作glsurfaceview中的纹理了。显示到屏幕上了
        //这里我们还只是把它画到fbo中(缓存)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        //使用着色器
        GLES20.glUseProgram(mGLProgramId);
     //   GLES20.glUniform1f(mParamsLocation,0.6f);
       // GLES20.glUniform2fv(mSingleStepOffsetLocation, 1,FloatBuffer.wrap(new float[] {2.0f / mOutputWidth, 2.0f / mOutputHeight}));

        //传递坐标
        mGLVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mGLVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mGLTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mGLTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //因为这一层是摄像头后的第一层，所以需要使用扩展的  GL_TEXTURE_EXTERNAL_OES
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(vTexture, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return mFrameBufferTextures[0];
    }

    public void setBeautyLevel(TGLSurfaceView tglSurfaceView, final float value) {
        tglSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(mParamsLocation,value);
            }
        });
    }
}
