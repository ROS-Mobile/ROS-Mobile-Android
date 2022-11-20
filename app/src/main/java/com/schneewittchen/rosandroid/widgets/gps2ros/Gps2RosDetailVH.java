package com.schneewittchen.rosandroid.widgets.gps2ros;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.PublisherWidgetViewHolder;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickEntity;

import java.util.Collections;
import java.util.List;

import sensor_msgs.NavSatFix;

/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */
public class Gps2RosDetailVH extends PublisherWidgetViewHolder {

    @Override
    public void initView(View view) {
    }

    @Override
    public void bindEntity(BaseEntity entity) {
        Gps2RosEntity widget = (Gps2RosEntity) entity;
    }

    @Override
    public void updateEntity(BaseEntity entity) {
        Gps2RosEntity widget = (Gps2RosEntity) entity;
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(NavSatFix._TYPE);
    }

}
