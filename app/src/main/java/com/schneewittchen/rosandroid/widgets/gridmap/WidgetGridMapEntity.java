package com.schneewittchen.rosandroid.widgets.gridmap;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 31.01.20
 * @updated on 21.04.20
 * @modified by Nils Rottmann
 */
public class WidgetGridMapEntity extends BaseEntity {

    public WidgetGridMapEntity() {
        this.setType("GridMap");
        this.subscriber = new SubPubNoteEntity();
        this.subscriber.topic = "map";
        this.subscriber.messageType = nav_msgs.OccupancyGrid._TYPE;
    }

    @Override
    public String getName() {
        return "Gridmap";
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
    public boolean equalContent(BaseEntity other) {
        return true;
    }

    @Override
    public WidgetGridMapEntity copy() {
        WidgetGridMapEntity newEnt = new WidgetGridMapEntity();
        this.fillContend(newEnt);

        return newEnt;
    }
}
