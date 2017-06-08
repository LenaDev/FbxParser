package com.lenayeliieshvili.fbxparser.opengl;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.lenayeliieshvili.fbxparser.fbx.FbxModel;

public class GestureGlSurfaceView extends GLSurfaceView {

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = .2f;
    private CustomRender mRenderer;
    private String DEBUG_TAG = GestureGlSurfaceView.class.getSimpleName();

    private float mPreviousX = 0;
    private float mPreviousY = 0;
    private float mDensity;


    public GestureGlSurfaceView(Context context, FbxModel fbxModel) {
        super(context);
        mRenderer = new CustomRender(fbxModel);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());


        if (mScaleDetector != null) {
            mScaleFactor = mScaleFactor * mScaleDetector.getScaleFactor();
            mRenderer.setScale(Math.max(0.0f, Math.min(mScaleFactor, 5.0f)));
        }


    }

    public GestureGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        if (event.getPointerCount() < 2 && event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if (mRenderer != null)
            {
                float deltaX = (x - mPreviousX) / mDensity / 2f;
                float deltaY = (y - mPreviousY) / mDensity / 2f;

                mRenderer.mDeltaX += deltaX;
                mRenderer.mDeltaY += deltaY;
            }

        }
        mPreviousX = x;
        mPreviousY = y;

        return true;
    }

    public void setDensity(float density) {
        mDensity = density;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.0f, Math.min(mScaleFactor, 5.0f));

            mRenderer.setScale(mScaleFactor);
            invalidate();
            return true;
        }
    }
}
