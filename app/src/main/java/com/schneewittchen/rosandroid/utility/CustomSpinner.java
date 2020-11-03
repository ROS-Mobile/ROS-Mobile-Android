package com.schneewittchen.rosandroid.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatSpinner;

/**
 * A custom spinner class
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.09.20
 * @updated on
 * @modified by
 */

public class CustomSpinner extends AppCompatSpinner {
    private static final String TAG = "CustomSpinner";
    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CustomSpinner(Context context) {
        super(context);
    }

    public interface OnSpinnerEventsListener {

        void onSpinnerOpened();

        void onSpinnerClosed();

        void onSpinnerEmpty();

    }

    @Override
    public boolean performClick() {
        // register that the Spinner was opened so we have a status
        // indicator for the activity(which may lose focus for some other
        // reasons)
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened();
        }
        return super.performClick();
    }

    public void setSpinnerEventsListener(OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    /**
     * Propagate the closed Spinner event to the listener from outside.
     */
    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed();
        }
    }

    /**
     * Perform an empty list event if there are no items available in the adapter list
     */
    public void performEmptyListEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerEmpty();
        }
    }

    /**
     * A boolean flag indicating that the Spinner triggered an open event.
     *
     * @return true for opened Spinner
     */
    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        Log.d(TAG, "onWindowFocusChanged");
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasBeenOpened()) {
            if (hasWindowFocus) {
                Log.i(TAG, "closing popup");
                performClosedEvent();
            } else {
                Log.i(TAG, "empty spinner list");
                performEmptyListEvent();
            }
        }
    }
}
