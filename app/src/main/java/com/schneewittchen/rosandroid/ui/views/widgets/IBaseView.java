package com.schneewittchen.rosandroid.ui.views.widgets;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 */
public interface IBaseView {

    void setWidgetEntity(BaseEntity entity);
    BaseEntity getWidgetEntity();

    boolean sameWidgetEntity(BaseEntity other);
}
