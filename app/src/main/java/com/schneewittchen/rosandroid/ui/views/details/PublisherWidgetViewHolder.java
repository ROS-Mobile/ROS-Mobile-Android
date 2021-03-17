package com.schneewittchen.rosandroid.ui.views.details;

import android.util.Log;
import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

import java.util.ArrayList;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.21
 */
public abstract class PublisherWidgetViewHolder extends DetailViewHolder {

    private WidgetViewHolder widgetViewHolder;
    private PublisherViewHolder publisherViewHolder;


    public PublisherWidgetViewHolder() {
        widgetViewHolder = new WidgetViewHolder();
        publisherViewHolder = new PublisherViewHolder(viewModel, new ArrayList<>());
    }


    public void baseInitView(View view) {
        widgetViewHolder.baseInitView(view);
        publisherViewHolder.baseInitView(view);
    }

    public void baseBindEntity(BaseEntity entity) {
        widgetViewHolder.baseBindEntity(entity);
        publisherViewHolder.baseBindEntity(entity);
    }

    public void baseUpdateEntity(BaseEntity entity) {
        widgetViewHolder.baseUpdateEntity(entity);
        publisherViewHolder.baseUpdateEntity(entity);
    }
}
