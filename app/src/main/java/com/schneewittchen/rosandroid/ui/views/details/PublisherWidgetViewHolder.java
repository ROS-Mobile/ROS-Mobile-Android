package com.schneewittchen.rosandroid.ui.views.details;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;

import java.util.List;


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
        this.widgetViewHolder = new WidgetViewHolder(this);
        this.publisherViewHolder = new PublisherViewHolder(this);
        this.publisherViewHolder.topicTypes = this.getTopicTypes();
    }


    public abstract List<String> getTopicTypes();


    @Override
    public void setViewModel(DetailsViewModel viewModel) {
        super.setViewModel(viewModel);
        publisherViewHolder.viewModel = viewModel;
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
