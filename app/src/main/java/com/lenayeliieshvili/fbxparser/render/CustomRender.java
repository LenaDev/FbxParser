package com.lenayeliieshvili.fbxparser.render;


import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.lenayeliieshvili.fbxparser.parser.FbxModel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CustomRender implements GLSurfaceView.Renderer {

    private FbxModel mFbxModel;
    private float mCubeRotation;
    private float scale = 0.2f;

    public CustomRender(FbxModel model) {
        mFbxModel = model;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, 0.0f, -10);
        gl.glRotatef(mCubeRotation, 1.0f, 1.0f, 1.0f);
        gl.glScalef(scale, scale, scale);

        mFbxModel.draw(gl);

        gl.glLoadIdentity();

        mCubeRotation += 0.2f;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
