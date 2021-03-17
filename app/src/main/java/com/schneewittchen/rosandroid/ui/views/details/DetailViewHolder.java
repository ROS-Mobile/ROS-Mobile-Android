package com.schneewittchen.rosandroid.ui.views.details;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.fragments.details.DetailWidgetFragment;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.21
 */
public abstract class DetailViewHolder implements IBaseViewHolder{

    protected BaseEntity entity;
    protected View itemView;
    protected DetailsViewModel viewModel;
    private WidgetChangeListener widgetChangeListener;

    protected abstract void initView(View itemView);
    protected abstract void bindEntity(BaseEntity entity);
    protected abstract void updateEntity(BaseEntity entity);

    public abstract void baseInitView(View itemView);
    public abstract void baseBindEntity(BaseEntity entity);
    public abstract void baseUpdateEntity(BaseEntity entity);


    public void setViewModel(DetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
        baseBindEntity(entity);
        bindEntity(entity);
    }

    public void setView(View view) {
        this.itemView = view;
        baseInitView(view);
        initView(view);
    }

    public void setWidgetChangeListener(WidgetChangeListener changeListener) {
        this.widgetChangeListener = changeListener;
    }
}
