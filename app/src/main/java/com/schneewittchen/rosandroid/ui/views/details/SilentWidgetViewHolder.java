package com.schneewittchen.rosandroid.ui.views.details;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;



/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 01.04.21
 */
public abstract class SilentWidgetViewHolder extends DetailViewHolder {

    private WidgetViewHolder widgetViewHolder;


    public SilentWidgetViewHolder() {
        this.widgetViewHolder = new WidgetViewHolder(this);
    }


    public void baseInitView(View view) {
        widgetViewHolder.baseInitView(view);
    }

    public void baseBindEntity(BaseEntity entity) {
        widgetViewHolder.baseBindEntity(entity);
    }

    public void baseUpdateEntity(BaseEntity entity) {
        widgetViewHolder.baseUpdateEntity(entity);
    }
}
