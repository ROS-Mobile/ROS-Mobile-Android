package com.schneewittchen.rosandroid.widgets.gridmap;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.camera.WidgetCameraEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 31.01.20
 * @updated on 13.05.20
 * @modified by Nico Studt
 */
public class WidgetGridMapEntity extends BaseEntity {

    public WidgetGridMapEntity() {
        this.setType("GridMap");

        this.width = 4;
        this.height = 4;
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "map";
        this.subPubNoteEntity.messageType = nav_msgs.OccupancyGrid._TYPE;
    }


    @Override
    public String getName() {
        return "GridMap";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return GridMapView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_gridmap;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return GridMapDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return GridMapNode.class;
    }


    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.subPubNoteEntity.topic = entity.subPubNoteEntity.topic;
        this.subPubNoteEntity.messageType = entity.subPubNoteEntity.messageType;
    }

    @Override
    public WidgetGridMapEntity copy() {
        WidgetGridMapEntity newEnt = new WidgetGridMapEntity();
        newEnt.insert(this);

        return newEnt;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetGridMapEntity))
            return false;

        WidgetGridMapEntity other = (WidgetGridMapEntity) widget;

        return this.subPubNoteEntity.equals(other.subPubNoteEntity);
    }
}
