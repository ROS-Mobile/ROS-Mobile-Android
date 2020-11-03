package com.schneewittchen.rosandroid.widgets.base;

import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
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
        text = "";
        rotation = 0;
    }


    public abstract String getName();

    public abstract Class<? extends BaseView> getViewType();

    public abstract int getWidgetDetailViewId();

    public abstract Class<? extends BaseDetailViewHolder> getDetailViewHolderType();

    public abstract Class<? extends BaseNode> getNodeType();

    public abstract boolean equalContent(BaseEntity other);

    public abstract BaseEntity copy();

    protected void setType(String type) {
        this.type = type;
    }

    public void insert(WidgetEntity entity) {
        this.id = entity.id;
        this.type = entity.type;
        this.name = entity.name;
        this.configId = entity.configId;
        this.creationTime = entity.creationTime;
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.width = entity.width;
        this.height = entity.height;
        this.text = entity.text == null ? "" : entity.text;
        this.rotation = entity.rotation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof BaseEntity) {
            BaseEntity other = (BaseEntity) o;

            return other.id == this.id
                    && other.name.equals(this.name)
                    && other.configId == this.configId
                    && other.type.equals(this.type)
                    && other.posX == this.posX
                    && other.posY == this.posY
                    && other.width == this.width
                    && other.height == this.height;
        }

        return false;
    }

}
