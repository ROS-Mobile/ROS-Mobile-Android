package com.schneewittchen.rosandroid.widgets.gps;

import android.view.View;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;
import com.schneewittchen.rosandroid.widgets.gridmap.WidgetGridMapEntity;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 05.05.20
 * @modified by
 */

public class GpsDetailVH extends BaseDetailViewHolder<WidgetGridMapEntity> {
    public GpsDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }
}
