package com.dataseed.genieeffectlibrary;

import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Created by mikezhu on 6/27/17.
 */
public class GenieMesh extends Mesh {
    public static final String INHALE_DIRECT_LEFT = "LEFT";
    public static final String INHALE_DIRECT_RIGHT = "RIGHT";
    public static final String INHALE_DIRECT_TOP = "TOP";
    public static final String INHALE_DIRECT_BOTTOM = "BOTTOM";

    private Path mFirstPath;
    private Path mSecondPath;
    private Path mFirstPathTemp;
    private Path mSecondPathTemp;
    private PathMeasure mFirstPathMeasure;
    private PathMeasure mSecondPathMeasure;
    private String inhaleDirect;

    private float mEndX;
    private float mEndY;

    public GenieMesh(float startX, float startY, int widthPoints, int heightPoints) {
        super(startX, startY, widthPoints, heightPoints);
        mFirstPath = new Path();
        mSecondPath = new Path();
        mFirstPathTemp = new Path();
        mSecondPathTemp = new Path();
        mFirstPathMeasure = new PathMeasure();
        mSecondPathMeasure = new PathMeasure();
    }

    @Override
    public void buildPaths(float endX, float endY) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }
        mEndX = endX;
        mEndY = endY;

        mFirstPathMeasure.setPath(mFirstPath, false);
        mSecondPathMeasure.setPath(mSecondPath, false);

        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.reset();
        mSecondPath.reset();

        mFirstPath.moveTo(mStartX, mStartY);
        mSecondPath.moveTo(mStartX + w, mStartY);

        mFirstPath.cubicTo(mStartX, mStartY + h * 2 / 3, mEndX, mStartY + h * 2 / 3, mEndX, mEndY);
        mSecondPath.cubicTo(mStartX + w, mStartY + h * 2 / 3, mEndX, mStartY + h * 2 / 3, mEndX, mEndY);
    }

    private float mScale;

    public void buildAnimPaths(float xScale) {
        mFirstPath.reset();
        mSecondPath.reset();

        mFirstPathTemp.reset();
        mSecondPathTemp.reset();

        switch (getInhaleDirect()) {
            case INHALE_DIRECT_LEFT:
                break;
            case INHALE_DIRECT_TOP:
                buildTopPath(xScale);
                break;
            case INHALE_DIRECT_RIGHT:
                buildBottomRight(xScale);
                break;
            case INHALE_DIRECT_BOTTOM:
            default:
                buildBottomPath(xScale);
        }


    }

    private void buildTopPath(float xScale) {
        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.moveTo(mStartX, mStartY + h);
        mSecondPath.moveTo(mStartX + w, mStartY + h);

        mFirstPathTemp.moveTo(mStartX, mStartY + h);
        mSecondPathTemp.moveTo(mStartX + w, mStartY + h);

        mScale = xScale;

        mFirstPath.cubicTo(mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, mStartY - (mStartY - mEndY) * xScale);
        mSecondPath.cubicTo(mStartX + w, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, mStartY - (mStartY - mEndY) * xScale);

        mFirstPathTemp.cubicTo(mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, mStartY - (mStartY - mEndY) * xScale);
        mSecondPathTemp.cubicTo(mStartX + w, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, mStartY - (mStartY - mEndY) * xScale);

        if (xScale < 1) {
            mFirstPath.lineTo(mEndX, mStartY - (mStartY - mEndY) * xScale);
            mSecondPath.lineTo(mEndX, mStartY - (mStartY - mEndY) * xScale);
        }
    }

    private void buildBottomPath(float xScale) {
        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.moveTo(mStartX, mStartY);
        mSecondPath.moveTo(mStartX + w, mStartY);

        mFirstPathTemp.moveTo(mStartX, mStartY);
        mSecondPathTemp.moveTo(mStartX + w, mStartY);

        mScale = xScale;

        mFirstPath.cubicTo(mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, (mEndY - h - mStartY) * xScale + h + mStartY);
        mSecondPath.cubicTo(mStartX + w, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, (mEndY - h - mStartY) * xScale + h + mStartY);

        mFirstPathTemp.cubicTo(mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, mStartY + h * 2 / 3, (mEndX - mStartX) * xScale + mStartX, (mEndY - h - mStartY) * xScale + h + mStartY);
        mSecondPathTemp.cubicTo(mStartX + w, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, mStartY + h * 2 / 3, mStartX + w - (mStartX + w - mEndX) * xScale, (mEndY - h - mStartY) * xScale + h + mStartY);

        if (xScale < 1) {
            mFirstPath.lineTo(mEndX, (mEndY - h - mStartY) * xScale + h + mStartY);
            mSecondPath.lineTo(mEndX, (mEndY - h - mStartY) * xScale + h + mStartY);
        }
    }

    private void buildBottomRight(float xScale) {
        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.moveTo(mStartX, mStartY + h);
        mSecondPath.moveTo(mStartX, mStartY);

        mFirstPathTemp.moveTo(mStartX, mStartY + h);
        mSecondPathTemp.moveTo(mStartX, mStartY);

        mScale = xScale;

        mFirstPath.cubicTo(mStartX + (mEndX - mStartX) * 2 / 3, mStartY + h, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + h - (mStartY + h - mEndY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + h - (mStartY + h - mEndY) * xScale);
        mSecondPath.cubicTo(mStartX + (mEndX - mStartX) * 2 / 3, mStartY, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + (mEndY - mStartY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + (mEndY - mStartY) * xScale);

        mFirstPathTemp.cubicTo(mStartX + (mEndX - mStartX) * 2 / 3, mStartY + h, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + h - (mStartY + h - mEndY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + h - (mStartY + h - mEndY) * xScale);
        mSecondPathTemp.cubicTo(mStartX + (mEndX - mStartX) * 2 / 3, mStartY, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + (mEndY - mStartY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + (mEndY - mStartY) * xScale);

        if (xScale < 1) {
            mFirstPath.lineTo((mEndX - w - mStartX) * xScale + mStartX + w, mEndY);
            mSecondPath.lineTo((mEndX - w - mStartX) * xScale + mStartX + w, mEndY);
        }
    }

    private void buildBottomLeft(float xScale) {
        float w = mBmpWidth;
        float h = mBmpHeight;

        mFirstPath.moveTo(mStartX + w, mStartY);
        mSecondPath.moveTo(mStartX + w, mStartY + h);

        mFirstPathTemp.moveTo(mStartX + w, mStartY);
        mSecondPathTemp.moveTo(mStartX + w, mStartY + h);

        mScale = xScale;

        mFirstPath.cubicTo(mStartX + w + (mEndX - mStartX) * 2 / 3, mStartY + h, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + h - (mStartY + h - mEndY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + h - (mStartY + h - mEndY) * xScale);
        mSecondPath.cubicTo(mStartX + w + (mEndX - mStartX) * 2 / 3, mStartY, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + (mEndY - mStartY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + (mEndY - mStartY) * xScale);

        mFirstPathTemp.cubicTo(mStartX + w + (mEndX - mStartX) * 2 / 3, mStartY + h, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + h - (mStartY + h - mEndY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + h - (mStartY + h - mEndY) * xScale);
        mSecondPathTemp.cubicTo(mStartX + w + (mEndX - mStartX) * 2 / 3, mStartY, mStartX + (mEndX - mStartX) * 2 / 3, mStartY + (mEndY - mStartY) * xScale, (mEndX - w - mStartX) * xScale + mStartX + w, mStartY + (mEndY - mStartY) * xScale);

        if (xScale < 1) {
            mFirstPath.lineTo((mEndX - w - mStartX) * xScale + mStartX + w, mEndY);
            mSecondPath.lineTo((mEndX - w - mStartX) * xScale + mStartX + w, mEndY);
        }
    }

    @Override
    public void buildMeshes(int timeIndex) {
        if (mBmpWidth <= 0 || mBmpHeight <= 0) {
            throw new IllegalArgumentException(
                    "Bitmap size must be > 0, did you call setBitmapSize(int, int) method?");
        }

        if (mScale >= 1) {
            mFirstPathMeasure.setPath(mFirstPath, false);
            mSecondPathMeasure.setPath(mSecondPath, false);
        } else {
            mFirstPathMeasure.setPath(mFirstPathTemp, false);
            mSecondPathMeasure.setPath(mSecondPathTemp, false);
        }

        int index = 0;
        float[] pos1 = {0.0f, 0.0f};
        float[] pos2 = {0.0f, 0.0f};
        float firstLen = mFirstPathMeasure.getLength();
        float secondLen = mSecondPathMeasure.getLength();

        float len1 = firstLen / mVerticalSplit;
        float len2 = secondLen / mVerticalSplit;

        float firstPointDist = timeIndex * len1;
        float secondPointDist = timeIndex * len2;

        for (int y = 0; y <= mVerticalSplit; y++) {
            mFirstPathMeasure.getPosTan(y * len1 + firstPointDist, pos1, null);
            mSecondPathMeasure.getPosTan(y * len2 + secondPointDist, pos2, null);
            /*
             *        ...
             *   |x------x------x------x------x|
             *   |x------x------x------x------x|
             *   |x------x------x------x------x|
             *   |x------x------x------x------x|
             *   |x------x------x------x------x|
             *   |x------x------x------x------x|
             *   |x------x------x------x------x|
             *        ...
             *
             *   to:
             *                   ...
             *                 |xxxxx|
             *               |x-x-x-x-x|
             *              |x--x--x--x--x|
             *             | |  |  |  |  | |
             *            |x---x---x---x---x|
             *          |  |   |   |   |   | |
             *         |x----x----x----x----x|
             *         ||    |    |    |    | |
             *        ||     |     |     |     ||
             *        |x-----x-----x-----x-----x|
             *       | |     |     |     |     ||
             *     | |      |     |     |      ||
             *    ||      |      |      |      ||
             *    |x------x------x------x------x|
             *                   ...
             *
             *
             *
             */

            float fx1 = pos1[0];
            float fx2 = pos2[0];
            float fy1 = pos1[1];
            float fy2 = pos2[1];

            float dy = fy2 - fy1;
            float dx = fx2 - fx1;

            for (int x = 0; x <= mHorizontalSplit; x++) {
                float fx = dx * x / mHorizontalSplit;
                float fy = dy * x / mHorizontalSplit;

                switch (getInhaleDirect()) {
                    case INHALE_DIRECT_LEFT:
                        // TODO:
//                        mVertices[((index % (mHorizontalSplit + 1)) * (mHorizontalSplit + 1) + mHorizontalSplit - (int) (index / (mVerticalSplit + 1))) * 2 + 0] = fx + fx1;
//                        mVertices[((index % (mHorizontalSplit + 1)) * (mHorizontalSplit + 1) + mHorizontalSplit - (int) (index / (mVerticalSplit + 1))) * 2 + 1] = fy + fy1;
                        break;
                    case INHALE_DIRECT_TOP:
                        mVertices[((mVerticalSplit + 1) * (mHorizontalSplit + 1) - index - 1) * 2 + 0] = fx2 - fx;
                        mVertices[((mVerticalSplit + 1) * (mHorizontalSplit + 1) - index - 1) * 2 + 1] = fy + fy1;
                        break;
                    case INHALE_DIRECT_RIGHT:
                        mVertices[((mHorizontalSplit - index % (mHorizontalSplit + 1)) * (mHorizontalSplit + 1) + (int) (index / (mVerticalSplit + 1))) * 2 + 0] = fx + fx1;
                        mVertices[((mHorizontalSplit - index % (mHorizontalSplit + 1)) * (mHorizontalSplit + 1) + (int) (index / (mVerticalSplit + 1))) * 2 + 1] = fy + fy1;
                        break;
                    case INHALE_DIRECT_BOTTOM:
                    default:
                        mVertices[index * 2 + 0] = fx + fx1;
                        mVertices[index * 2 + 1] = fy + fy1;
                }


                index += 1;
            }
        }
    }

    public Path[] getPaths() {
        return new Path[]{mFirstPath, mSecondPath};
    }

    private String getInhaleDirect() {
        float left = mStartX;
        float right = mStartX + mBmpWidth;
        float top = mStartY;
        float bottom = mStartY + mBmpHeight;

        float differenceLeft = mEndX - left;
        float differenceRight = mEndX - right;
        float differenceTop = mEndY - top;
        float differenceBottom = mEndY - bottom;

        boolean isLeft = differenceLeft < 0;
        boolean isRight = differenceRight > 0;
        boolean isTop = differenceTop < 0;
        boolean isBottom = differenceBottom > 0;

        if (isLeft && !isTop && !isBottom) {
            inhaleDirect = INHALE_DIRECT_LEFT;
            return inhaleDirect;
        }

        if (isTop && !isLeft && !isRight) {
            inhaleDirect = INHALE_DIRECT_TOP;
            return inhaleDirect;
        }

        if (isRight && !isTop && !isBottom) {
            inhaleDirect = INHALE_DIRECT_RIGHT;
            return inhaleDirect;
        }

        if (isBottom && !isLeft && !isRight) {
            inhaleDirect = INHALE_DIRECT_BOTTOM;
            return inhaleDirect;
        }

        if (isLeft && isTop) {
            inhaleDirect = Math.abs(differenceLeft) < Math.abs(differenceTop) ? INHALE_DIRECT_TOP : INHALE_DIRECT_LEFT;
            return inhaleDirect;
        }

        if (isLeft && isBottom) {
            inhaleDirect = Math.abs(differenceLeft) < Math.abs(differenceBottom) ? INHALE_DIRECT_BOTTOM : INHALE_DIRECT_LEFT;
            return inhaleDirect;
        }

        if (isRight && isTop) {
            inhaleDirect = Math.abs(differenceRight) < Math.abs(differenceTop) ? INHALE_DIRECT_TOP : INHALE_DIRECT_RIGHT;
            return inhaleDirect;
        }

        if (isRight && isBottom) {
            inhaleDirect = Math.abs(differenceRight) < Math.abs(differenceBottom) ? INHALE_DIRECT_BOTTOM : INHALE_DIRECT_RIGHT;
            return inhaleDirect;
        }

        return INHALE_DIRECT_BOTTOM;
    }

    public void setInhaleDirect(String inhaleDirect) {
        this.inhaleDirect = inhaleDirect;
    }
}