package com.tck.surfacetomanytexture;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tck.common.OnRenderCreateListener;
import com.tck.common.YGlSurfaceView;


/**
 * <p>description:</p>
 * <p>created on: 2019/7/24 13:50</p>
 *
 * @author tck
 * @version 1.0
 */
public class SurfaceToManyTextureActivity extends AppCompatActivity {

    private YGlSurfaceView surfaceView;
    private LinearLayout llContent;
    private SurfaceToManyTextureRender abstractBaseRender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfacetomanytexture);


        surfaceView = (YGlSurfaceView) findViewById(R.id.surface_view);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        abstractBaseRender = new SurfaceToManyTextureRender(this);
        surfaceView.setAbstractBaseRender(abstractBaseRender);
        abstractBaseRender.setOnRenderCreateListener(new OnRenderCreateListener() {
            @Override
            public void onCreate(int textid) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (llContent.getChildCount() > 0) {
                            llContent.removeAllViews();
                        }

                        for (int i = 0; i < 3; i++) {

                        }
                    }
                });
            }
        });

    }
}
