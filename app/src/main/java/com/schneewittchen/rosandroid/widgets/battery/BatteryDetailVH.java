package com.schneewittchen.rosandroid.widgets.battery;

import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.schneewittchen.rosandroid.R;
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

    SwitchMaterial voltageSwitch;
    boolean forceSetChecked;

    @Override
    public void initView(View view) {
        voltageSwitch = view.findViewById(R.id.voltageSwitch);
        voltageSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!forceSetChecked) forceWidgetUpdate();
        });
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        BatteryEntity batteryEntity = (BatteryEntity) entity;

        forceSetChecked = true;
        voltageSwitch.setChecked(batteryEntity.displayVoltage);
        forceSetChecked = false;
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        BatteryEntity batteryEntity = (BatteryEntity)entity;
        batteryEntity.displayVoltage = voltageSwitch.isChecked();
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(sensor_msgs.BatteryState._TYPE);
    }
}
