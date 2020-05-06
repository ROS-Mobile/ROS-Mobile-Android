package com.schneewittchen.rosandroid.widgets.gps;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.BaseView;

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
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "gps";
        this.subPubNoteEntity.messageType = sensor_msgs.NavSatFix._TYPE;
    }

    @Override
    public String getName() {
        return "Gps";
    }

    @Override
    public int getWidgetVizViewId() {
        return 0;
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
    public boolean equalContent(BaseEntity other) {
        return true;
    }

    @Override
    public com.schneewittchen.rosandroid.widgets.gps.WidgetGpsEntity copy() {
        com.schneewittchen.rosandroid.widgets.gps.WidgetGpsEntity newEnt = new com.schneewittchen.rosandroid.widgets.gps.WidgetGpsEntity();
        this.fillContend(newEnt);

        return newEnt;
    }
}

