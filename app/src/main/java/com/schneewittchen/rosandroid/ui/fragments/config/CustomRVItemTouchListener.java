package com.schneewittchen.rosandroid.ui.fragments.config;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 05.02.20
 * @updated on 05.02.20
 * @modified by
 */
public class CustomRVItemTouchListener implements RecyclerView.OnItemTouchListener {

    private final GestureDetector gestureDetector;
    private final RecyclerViewItemClickListener clickListener;


    public CustomRVItemTouchListener(Context context, final RecyclerView recyclerView,
                                     final RecyclerViewItemClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                /*
                //find the long pressed view
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                }
                */

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        View child = recyclerView.findChildViewUnder(event.getX(), event.getY());

        if (child != null && clickListener != null && gestureDetector.onTouchEvent(event)) {
            clickListener.onClick(recyclerView, child, recyclerView.getChildLayoutPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
