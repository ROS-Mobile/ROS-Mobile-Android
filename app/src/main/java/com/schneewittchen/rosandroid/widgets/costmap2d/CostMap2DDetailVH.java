package com.schneewittchen.rosandroid.widgets.costmap2d;

import android.view.View;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.details.BaseDetailSubscriberVH;
import com.schneewittchen.rosandroid.widgets.costmap.CostMapEntity;

import java.util.Collections;
import java.util.List;

import nav_msgs.OccupancyGrid;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class CostMap2DDetailVH extends BaseDetailSubscriberVH<CostMapEntity> {


    public CostMap2DDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    protected void initView(View parentView) {

    }

    @Override
    protected void bindEntity(CostMapEntity entity) {

    }

    @Override
    protected void updateEntity() {

    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(OccupancyGrid._TYPE);
    }

}
