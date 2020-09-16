package com.schneewittchen.rosandroid.widgets.costmap;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.BaseView;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.09.2020
 * @updated
 * @modified
 */
public class WidgetCostMapEntity extends BaseEntity {

    public WidgetCostMapEntity() {
        this.setType("CostMap");

        this.width = 4;
        this.height = 4;
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "move_base/global_costmap/costmap";
        this.subPubNoteEntity.messageType = nav_msgs.OccupancyGrid._TYPE;
    }


    @Override
    public String getName() {
        return "CostMap";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return CostMapView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_gridmap;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return CostMapDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return CostMapNode.class;
    }


    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.subPubNoteEntity.topic = entity.subPubNoteEntity.topic;
        this.subPubNoteEntity.messageType = entity.subPubNoteEntity.messageType;
    }

    @Override
    public WidgetCostMapEntity copy() {
        WidgetCostMapEntity newEnt = new WidgetCostMapEntity();
        newEnt.insert(this);

        return newEnt;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetCostMapEntity))
            return false;

        WidgetCostMapEntity other = (WidgetCostMapEntity) widget;

        return this.subPubNoteEntity.equals(other.subPubNoteEntity);
    }
}
