package com.schneewittchen.rosandroid.widgets.base;

import android.util.Log;

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

    public static String TAG = BaseEntity.class.getSimpleName();


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

    public abstract boolean equalContent(BaseEntity other);


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof BaseEntity) {
            BaseEntity other = (BaseEntity) o;

            return other.id == this.id
                    && other.type == this.type
                    && other.posX == this.posX
                    && other.posY == this.posY
                    && other.width == this.width
                    && other.height == this.height;
        }

        return false;
    }
}
