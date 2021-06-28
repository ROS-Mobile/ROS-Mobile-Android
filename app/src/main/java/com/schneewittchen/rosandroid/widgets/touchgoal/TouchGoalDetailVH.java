package com.schneewittchen.rosandroid.widgets.touchgoal;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.PublisherLayerViewHolder;

import java.util.Collections;
import java.util.List;

import geometry_msgs.PoseStamped;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 26.05.2021
 */
public class TouchGoalDetailVH extends PublisherLayerViewHolder {

    private static final String TAG = TouchGoalDetailVH.class.getSimpleName();


    @Override
    protected void initView(View parentView) {
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        TouchGoalEntity scanEntity = (TouchGoalEntity) entity;
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        TouchGoalEntity scanEntity = (TouchGoalEntity) entity;
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(PoseStamped._TYPE);
    }

}
