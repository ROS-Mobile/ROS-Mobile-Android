package com.schneewittchen.rosandroid.widgets.battery;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberWidgetViewHolder;

import java.util.Collections;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 13.05.2021
 */
public class BatteryDetailVH extends SubscriberWidgetViewHolder {


    @Override
    public void initView(View view) {
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        BatteryEntity batteryEntity = (BatteryEntity) entity;
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        BatteryEntity batteryEntity = (BatteryEntity)entity;
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(sensor_msgs.BatteryState._TYPE);
    }
}
