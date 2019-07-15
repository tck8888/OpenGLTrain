package com.tck.opengldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tck.opengldemo.muti.MutiSurfaceVeiw;

public class MainActivity extends AppCompatActivity {


    private GLTextureView glTextureView;
    private LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        glTextureView = (GLTextureView) findViewById(R.id.glTextureView);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);

        glTextureView.getMyRender().setOnRenderCreateListener(new TextureRenderWithVBOAndFBO2.OnRenderCreateListener() {
            @Override
            public void onCreate(final int textid) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (llContainer.getChildCount() > 0) {
                            llContainer.removeAllViews();
                        }

                        for (int i = 0; i < 3; i++) {
                            MutiSurfaceVeiw wlMutiSurfaceVeiw = new MutiSurfaceVeiw(MainActivity.this);
                            wlMutiSurfaceVeiw.setTextureId(textid, i);
                            wlMutiSurfaceVeiw.setSurfaceAndEGLContext(null, wlMutiSurfaceVeiw.getEGLContext());

                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            lp.width = 200;
                            lp.height = 300;
                            wlMutiSurfaceVeiw.setLayoutParams(lp);

                            llContainer.addView(wlMutiSurfaceVeiw);
                        }
                    }
                });
            }
        });


    }
}
