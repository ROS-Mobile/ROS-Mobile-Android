package com.schneewittchen.rosandroid.ui.views.details;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.21
 */
interface IBaseViewHolder {

    void baseInitView(View view);
    void baseBindEntity(BaseEntity entity);
    void baseUpdateEntity(BaseEntity entity);
}
