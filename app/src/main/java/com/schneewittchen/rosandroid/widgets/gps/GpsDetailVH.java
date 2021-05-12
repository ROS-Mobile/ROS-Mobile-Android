package com.schneewittchen.rosandroid.widgets.gps;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberWidgetViewHolder;

import java.util.Collections;
import java.util.List;

import sensor_msgs.NavSatFix;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 * @updated on 20.03.21
 * @modified by Nico Studt
 */
public class GpsDetailVH extends SubscriberWidgetViewHolder {


    @Override
    protected void initView(View parentView) {

    }

    @Override
    protected void bindEntity(BaseEntity entity) {

    }

    @Override
    protected void updateEntity(BaseEntity entity) {

    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(NavSatFix._TYPE);
    }
}
