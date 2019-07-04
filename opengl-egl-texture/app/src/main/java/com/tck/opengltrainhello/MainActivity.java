package com.tck.opengltrainhello;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Bundle;
import android.widget.TextView;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {


    private NativeOpenGL nativeOpenGL;
    private MySurfaceView mySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        nativeOpenGL = new NativeOpenGL();
        mySurfaceView = findViewById(R.id.surface_view);
        mySurfaceView.setNativeOpenGL(nativeOpenGL);


        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.mingren);
        ByteBuffer fcbuffer = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getWidth() * 4);
        bitmap.copyPixelsToBuffer(fcbuffer);
        fcbuffer.flip();
        byte[] pixels = fcbuffer.array();
        nativeOpenGL.imgData(bitmap.getWidth(), bitmap.getHeight(), pixels.length, pixels);
    }
}
