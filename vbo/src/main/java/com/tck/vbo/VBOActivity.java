package com.tck.vbo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tck.common.YGlSurfaceView;


/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 13:50</p>
 *
 * @author tck
 * @version 1.0
 */
public class VBOActivity extends AppCompatActivity {

    private YGlSurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vbo);
        surfaceView = (YGlSurfaceView) findViewById(R.id.surface_view);
        surfaceView.setAbstractBaseRender(new VBORender(this));
    }
}
