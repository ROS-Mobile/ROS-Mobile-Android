package com.schneewittchen.rosandroid.widgets.base;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 16.03.20
 * @updated on 2.04.20
 * @modified by
 */
public abstract class BaseEntity extends WidgetEntity {

    public abstract String getName();

    public abstract int getEntityType();

    public abstract int getWidgetVizViewId();

    public abstract Class<? extends BaseView> getViewType();

    public abstract int getWidgetDetailViewId();

    public abstract Class<? extends BaseDetailViewHolder> getDetailViewHolderType();

    public abstract Class<? extends WidgetNode> getNodeType();

    protected void setType(int type) {
        this.type = type;
    }

}
