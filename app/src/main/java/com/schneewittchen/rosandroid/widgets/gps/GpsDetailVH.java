package com.schneewittchen.rosandroid.widgets.gps;

import android.view.View;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;

import java.util.Arrays;
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
 */
public class GpsDetailVH extends BaseDetailSubscriberVH<GpsEntity> {

    public GpsDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    protected void initView(View parentView) {

    }

    @Override
    protected void bindEntity(GpsEntity entity) {

    }

    @Override
    protected void updateEntity() {

    }

    @Override
    public List<String> getTopicTypes() {
        return Arrays.asList(NavSatFix._TYPE);
    }
}
