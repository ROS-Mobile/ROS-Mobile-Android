package com.schneewittchen.rosandroid.widgets.gps;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.camera.WidgetCameraEntity;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 05.05.20
 * @modified by
 */

public class WidgetGpsEntity extends BaseEntity {


    public WidgetGpsEntity() {
        this.setType("Gps");

        this.width = 4;
        this.height = 4;
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "gps";
        this.subPubNoteEntity.messageType = sensor_msgs.NavSatFix._TYPE;
    }


    @Override
    public String getName() {
        return "GPS";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return GpsView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_gps;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return GpsDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return GpsNode.class;
    }


    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.subPubNoteEntity.topic = entity.subPubNoteEntity.topic;
        this.subPubNoteEntity.messageType = entity.subPubNoteEntity.messageType;
    }

    @Override
    public WidgetGpsEntity copy() {
        WidgetGpsEntity newEnt = new WidgetGpsEntity();
        newEnt.insert(this);

        return newEnt;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetGpsEntity))
            return false;

        WidgetGpsEntity other = (WidgetGpsEntity) widget;

        return this.subPubNoteEntity.equals(other.subPubNoteEntity);
    }
}

