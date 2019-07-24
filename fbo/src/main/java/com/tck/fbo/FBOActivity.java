package com.tck.fbo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tck.common.YGLSurfaceView;


/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 13:50</p>
 *
 * @author tck
 * @version 1.0
 */
public class FBOActivity extends AppCompatActivity {

    private YGLSurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbo);
        surfaceView = (YGLSurfaceView) findViewById(R.id.surface_view);
        surfaceView.setAbstractBaseRender(new FBORenderTrain(this));
    }
}
