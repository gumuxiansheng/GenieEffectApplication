package com.dataseed.genieeffectlibrary;

/**
 * Created by mikezhu on 6/27/17.
 */
public abstract class Mesh {

    private static final int HORIZONTAL_SPLIT = 60;
    private static final int VERTICAL_SPLIT = 60;
    /**
     * splitting partitions in horizontal and vertical
     */
    protected int mHorizontalSplit;
    protected int mVerticalSplit;

    protected int mBmpWidth = -1;
    protected int mBmpHeight = -1;

    protected float mStartX = 0;
    protected float mStartY = 0;

    protected final float[] mVertices;

    public Mesh() {
        mHorizontalSplit = HORIZONTAL_SPLIT;
        mVerticalSplit = VERTICAL_SPLIT;
        mVertices = new float[(mHorizontalSplit + 1) * (mVerticalSplit + 1) * 2];
    }

    public Mesh(float startX, float startY, int widthPoints, int heightPoints) {
        mHorizontalSplit = widthPoints;
        mVerticalSplit = heightPoints;
        mStartX = startX;
        mStartY = startY;
        mVertices = new float[(mHorizontalSplit + 1) * (mVerticalSplit + 1) * 2];
    }

    public float[] getVertices() {
        return mVertices;
    }

    public int getWidth() {
        return mHorizontalSplit;
    }

    public int getHeight() {
        return mVerticalSplit;
    }

    public static void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    public void setBitmapSize(int w, int h) {
        mBmpWidth = w;
        mBmpHeight = h;
    }

    public abstract void buildPaths(float endX, float endY);

    public abstract void buildMeshes(int index);

    public void buildMeshes(float w, float h) {
        int index = 0;
        for (int y = 0; y <= mVerticalSplit; y++) {
            float fy = y * h / mVerticalSplit;
            for (int x = 0; x <= mHorizontalSplit; x++) {
                float fx = x * w / mHorizontalSplit;
                setXY(mVertices, index, fx + mStartX, fy + mStartY);
                index++;
            }
        }
    }
}