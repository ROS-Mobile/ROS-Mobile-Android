package com.schneewittchen.rosandroid.ui.fragments.details;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 24.01.20
 * @updated on 24.01.20
 * @modified by
 */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private final TouchListener listener;


    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, TouchListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((BaseDetailViewHolder)viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas canvas, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

        final View foregroundView = ((BaseDetailViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(canvas, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        final View foregroundView = ((BaseDetailViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        final View foregroundView = ((BaseDetailViewHolder) viewHolder).viewForeground;

        getDefaultUIUtil().onDraw(canvas, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    public interface TouchListener {

        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
