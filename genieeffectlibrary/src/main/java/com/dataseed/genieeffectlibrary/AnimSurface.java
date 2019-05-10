package com.dataseed.genieeffectlibrary;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by mikezhu on 6/27/17.
 */
public class AnimSurface extends View {

    private static final int WIDTH_POINTS = 60;
    private static final int HEIGHT_POINTS = 60;

    private static final int STARTX = 200;
    private static final int STARTY = 400;

    private Bitmap mBitmap;
    private boolean mIsDebug;
    private Paint mPaint;
    private float[] mInhalePoint;
    private GenieMesh mGenieMesh;
    private View originView;

    public AnimSurface(Context context) {
        super(context);
        setFocusable(true);
    }

    public AnimSurface(Context context, float startX, float startY, Bitmap bitmap) {
        super(context);
        setFocusable(true);

        mBitmap = bitmap;

        mPaint = new Paint();
        mInhalePoint = new float[]{0, 0};
        mGenieMesh = new GenieMesh(startX, startY, WIDTH_POINTS, HEIGHT_POINTS);
        mGenieMesh.setBitmapSize(mBitmap.getWidth(), mBitmap.getHeight());

        buildMesh(mBitmap.getWidth(), mBitmap.getHeight());
    }

    private void initView(final View view, final int endPointX, final int endPointY) {
        setFocusable(true);

        originView = view;
        final int[] viewPosition = new int[2];
        view.getLocationOnScreen(viewPosition);
        view.buildDrawingCache();


        int[] selfPosition = new int[2];
        AnimSurface.this.getLocationOnScreen(selfPosition);

        int startX = viewPosition[0] - selfPosition[0];
        int startY = viewPosition[1] - selfPosition[1];


        if (view.getVisibility() != VISIBLE) {
            view.setVisibility(VISIBLE);
            view.buildDrawingCache();
            mBitmap = view.getDrawingCache();
            view.setVisibility(INVISIBLE);
        } else {
            mBitmap = view.getDrawingCache();
        }

        mPaint = new Paint();
        mInhalePoint = new float[]{0, 0};
        mGenieMesh = new GenieMesh(startX, startY, WIDTH_POINTS, HEIGHT_POINTS);
        mGenieMesh.setBitmapSize(mBitmap.getWidth(), mBitmap.getHeight());
        mGenieMesh.setInhaleDirect(GenieMesh.INHALE_DIRECT_RIGHT);

        buildPaths(endPointX, endPointY);
        buildMesh(mBitmap.getWidth(), mBitmap.getHeight());


    }

    public void setIsDebug(boolean isDebug) {
        mIsDebug = isDebug;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBitmap == null) {
            return;
        }

        float bitmapWidth = mBitmap.getWidth();
        float bitmapHeight = mBitmap.getHeight();

        buildPaths(bitmapWidth / 2, h - 20);
        buildMesh(bitmapWidth, bitmapHeight);
    }

    final ValueAnimator animator1 = ValueAnimator.ofFloat(0, 1);
    final ValueAnimator animator2 = ValueAnimator.ofFloat(0, 1);

    public boolean startAnimation(final View view, final int endPointX, final int endPointY, final boolean reverse) {
        this.post(new Runnable() {
            @Override
            public void run() {
                initView(view, endPointX, endPointY);
                startAnimation(reverse);
            }
        });

        return true;
    }

    public boolean startAnimation(final boolean reverse) {
        if (mBitmap == null || animator1.isRunning() || animator2.isRunning()) {
            return false;
        }

        final AnimatorSet animatorSet = new AnimatorSet();

        animator1.removeAllUpdateListeners();
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentIndex = (int) ((0 + HEIGHT_POINTS + 1) * (reverse ? 1 - (float) animation.getAnimatedValue() : (float) animation.getAnimatedValue()));
                mGenieMesh.buildMeshes(currentIndex);
                invalidate();
            }
        });
        animator1.setDuration(400);
        animator1.setInterpolator(new AccelerateInterpolator());

        animator2.removeAllUpdateListeners();
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGenieMesh.buildAnimPaths(reverse ? 1 - (float) animation.getAnimatedValue() : (float) animation.getAnimatedValue());
                mGenieMesh.buildMeshes(0);
                invalidate();
            }
        });
        animator2.setDuration(230);
        animator2.setInterpolator(new AccelerateInterpolator());

        if (reverse) {
            animatorSet.playSequentially(animator1, animator2);
//            animatorSet.playTogether(animator1, animator2);
        } else {
            animatorSet.playSequentially(animator2, animator1);
//            animatorSet.playTogether(animator1, animator2);
        }
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (originView != null) {
                    originView.setVisibility(VISIBLE);
                    originView.setVisibility(INVISIBLE);
                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (originView != null && reverse) {
                    originView.setVisibility(VISIBLE);
                }

                ((ViewGroup) AnimSurface.this.getParent()).removeView(AnimSurface.this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mGenieMesh == null) {
            return;
        }

        canvas.drawBitmapMesh(mBitmap,
                mGenieMesh.getWidth(),
                mGenieMesh.getHeight(),
                mGenieMesh.getVertices(),
                0, null, 0, mPaint);
        // Draw the target point.
        //mPaint.setColor(Color.RED);
        //mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mInhalePoint[0], mInhalePoint[1], 5, mPaint);

        if (mIsDebug) {
            // Draw the mesh vertices.
//            canvas.drawPoints(mInhaleMesh.getVertices(), mPaint);
            // Draw the paths
//            mPaint.setColor(Color.BLUE);
//            mPaint.setStyle(Paint.Style.STROKE);
//            Path[] paths = mInhaleMesh.getPaths();
//            for (Path path : paths) {
//                canvas.drawPath(path, mPaint);
//            }
        }
    }

    private void buildMesh(float w, float h) {
        mGenieMesh.buildMeshes(w, h);
    }

    private void buildPaths(float endX, float endY) {
        mInhalePoint[0] = endX;
        mInhalePoint[1] = endY;
        mGenieMesh.buildPaths(endX, endY);
    }

    private int mLastPointX = 0;
    private int mLastPointY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pt = {event.getX(), event.getY()};

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) pt[0];
            int y = (int) pt[1];
            if (mLastPointX != x || mLastPointY != y) {
                mLastPointX = x;
                mLastPointY = y;
                buildPaths(pt[0], pt[1]);
                invalidate();
            }
        }
        return true;
    }
}
