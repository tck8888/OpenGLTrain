package com.tck.opengltrainhello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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
    }
}
