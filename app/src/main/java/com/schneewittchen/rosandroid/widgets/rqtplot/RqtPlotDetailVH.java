package com.schneewittchen.rosandroid.widgets.rqtplot;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberWidgetViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 29.05.21
 */
public class RqtPlotDetailVH extends SubscriberWidgetViewHolder {

    @Override
    public void initView(View view) {
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        RqtPlotEntity plotEntity = (RqtPlotEntity) entity;
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        RqtPlotEntity plotEntity = (RqtPlotEntity) entity;
    }

    @Override
    public List<String> getTopicTypes() {
        return new ArrayList<>();
    }

}
