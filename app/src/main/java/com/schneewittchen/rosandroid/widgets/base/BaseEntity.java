package com.schneewittchen.rosandroid.widgets.base;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 16.03.20
 * @updated on 14.04.20
 * @modified by
 */
public abstract class BaseEntity extends WidgetEntity {

    public static String TAG = BaseEntity.class.getSimpleName();

    public BaseEntity() {
        posX = 0;
        posY = 0;
        width = 1;
        height = 1;
    }

    public abstract String getName();

    public abstract int getEntityType();

    public abstract int getWidgetVizViewId();

    public abstract Class<? extends BaseView> getViewType();

    public abstract int getWidgetDetailViewId();

    public abstract Class<? extends BaseDetailViewHolder> getDetailViewHolderType();

    public abstract Class<? extends BaseNode> getNodeType();


    public abstract boolean equalContent(BaseEntity other);

    public abstract BaseEntity copy();

    protected void setType(int type) {
        this.type = type;
    }

    protected void fillContend(BaseEntity other) {
        other.posX = posX;
        other.posY = posY;
        other.width = width;
        other.height = height;
        other.id = id;
        other.name = name;
        other.type = type;
        other.configId = configId;
        other.creationTime = creationTime;
    }

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
