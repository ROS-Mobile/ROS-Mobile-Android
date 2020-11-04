package com.schneewittchen.rosandroid.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatSpinner;

/**
 * A custom spinner class
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.09.2020
 * @updated on 03.11.2020
 * @modified by Nico Studt
 */

public class CustomSpinner extends AppCompatSpinner implements AdapterView.OnItemSelectedListener {

    /**
     * An interface which a client of this Spinner could use to receive
     * open/closed events for this Spinner.
     */
    public interface OnSpinnerEventsListener {

        /**
         * Callback triggered when the spinner was opened.
         */
        void onSpinnerOpened(CustomSpinner spinner);

        /**
         * Callback triggered when an item of the spinner was selected.
         */
        void onSpinnerItemSelected(CustomSpinner spinner, Integer position);

        /**
         * Callback triggered when the spinner was closed.
         */
        void onSpinnerClosed(CustomSpinner spinner);

    }


    private OnSpinnerEventsListener mListener;
    private boolean mOpenInitiated = false;


    public CustomSpinner(Context context) {
        super(context);
        this.setOnItemSelectedListener(this);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onSpinnerItemSelected(this, position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (mListener != null) {
            mListener.onSpinnerItemSelected(this, null);
        }
    }

    @Override
    public boolean performClick() {
        // register that the Spinner was opened so we have a status
        // indicator for when the container holding this Spinner may lose focus
        mOpenInitiated = true;

        if (mListener != null) {
            mListener.onSpinnerOpened(this);
        }

        return super.performClick();
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent();
        }
    }

    /**
     * Register the listener which will listen for events.
     */
    public void setSpinnerEventsListener(
            OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    /**
     * Propagate the closed Spinner event to the listener from outside if needed.
     */
    public void performClosedEvent() {
        mOpenInitiated = false;

        if (mListener != null) {
            mListener.onSpinnerClosed(this);
        }
    }

    /**
     * Perform an empty list event if there are no items available in the adapter list
     */
    public void performEmptyListEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed(this);
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

}
