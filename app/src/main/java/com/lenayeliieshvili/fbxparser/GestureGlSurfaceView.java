package com.lenayeliieshvili.fbxparser;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.lenayeliieshvili.fbxparser.render.CustomRender;

public class GestureGlSurfaceView extends GLSurfaceView {

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 0.4f;
    private CustomRender mRenderer;
    public GestureGlSurfaceView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public GestureGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.0f, Math.min(mScaleFactor, 5.0f));
//            setScaleX(mScaleFactor);
//            setScaleY(mScaleFactor);

            mRenderer.setScale(mScaleFactor);
            invalidate();
            return true;
        }
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        mRenderer = (CustomRender) renderer;
    }
}
