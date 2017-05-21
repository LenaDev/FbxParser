package com.lenayeliieshvili.fbxparser;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.lenayeliieshvili.fbxparser.parser.FbxModel;
import com.lenayeliieshvili.fbxparser.render.CustomRender;

public class GestureGlSurfaceView extends GLSurfaceView {

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mDetector;
    private float mScaleFactor = .2f;
    private CustomRender mRenderer;
    private String DEBUG_TAG = GestureGlSurfaceView.class.getSimpleName();

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
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

        mDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString() + "Distance: " +distanceX + " y: " + distanceY);
                mRenderer.setTranslation((e1.getX() - e2.getX()),(e1.getY() - e2.getY()));
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
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

        if (event.getAction() == MotionEvent.ACTION_MOVE)
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
