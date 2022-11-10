package com.schneewittchen.rosandroid.widgets.switchbutton;

import android.view.View;
import android.widget.EditText;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.PublisherWidgetViewHolder;

import java.util.Collections;
import java.util.List;

import std_msgs.Bool;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.05.2022
 */
public class SwitchButtonDetailVH extends PublisherWidgetViewHolder {

    private EditText textOffText;
    private EditText textOnText;

    @Override
    public void initView(View view) {
        textOffText = view.findViewById(R.id.switchTextOffText);
        textOnText = view.findViewById(R.id.switchTextONText);
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        SwitchButtonEntity switchEntity = (SwitchButtonEntity) entity;

        textOffText.setText(switchEntity.offText);
        textOnText.setText(switchEntity.onText);
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        SwitchButtonEntity switchEntity = (SwitchButtonEntity) entity;
        switchEntity.offText = textOffText.getText().toString();
        switchEntity.onText = textOnText.getText().toString();
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(Bool._TYPE);
    }
}
