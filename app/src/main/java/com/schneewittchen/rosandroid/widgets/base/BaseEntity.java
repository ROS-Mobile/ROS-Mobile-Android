package com.schneewittchen.rosandroid.widgets.base;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

public abstract class BaseEntity extends WidgetEntity {

    public BaseEntity() {
        super();
    }
    public abstract int getEntityType();

    public abstract String getName();

    public abstract Class<? extends WidgetNode> getNodeType();

    public abstract int getWidgetVizViewId();

    public abstract int getWidgetDetailViewId();
}
