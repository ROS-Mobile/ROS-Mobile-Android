package com.schneewittchen.rosandroid.ui.general;

import android.graphics.Matrix;
import android.view.MotionEvent;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 11.09.20
 * @updated on
 * @modified by
 */
public class MatrixGestureDetector {

    private static final String TAG = "MatrixGestureDetector";

    private int ptpIdx = 0;
    private final Matrix mTempMatrix = new Matrix();
    private final Matrix mMatrix;
    private final OnMatrixChangeListener mListener;
    private final float[] mSrc = new float[4];
    private final float[] mDst = new float[4];
    private int mCount;


    public interface OnMatrixChangeListener {
        void onChange(Matrix matrix);
    }


    public MatrixGestureDetector(Matrix matrix, MatrixGestureDetector.OnMatrixChangeListener listener) {
        this.mMatrix = matrix;
        this.mListener = listener;
    }


    public void onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 2) {
            return;
        }

        int action = event.getActionMasked();
        int index = event.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int idx = index * 2;
                mSrc[idx] = event.getX(index);
                mSrc[idx + 1] = event.getY(index);
                mCount++;
                ptpIdx = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < mCount; i++) {
                    idx = ptpIdx + i * 2;
                    mDst[idx] = event.getX(i);
                    mDst[idx + 1] = event.getY(i);
                }

                mTempMatrix.setPolyToPoly(mSrc, ptpIdx, mDst, ptpIdx, mCount);
                mMatrix.postConcat(mTempMatrix);

                if(mListener != null) {
                    mListener.onChange(mMatrix);
                }

                System.arraycopy(mDst, 0, mSrc, 0, mDst.length);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(index) == 0) ptpIdx = 2;
                mCount--;
                break;
        }
    }
}