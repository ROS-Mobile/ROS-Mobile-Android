package com.schneewittchen.rosandroid.widgets.gridmap;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.joystick.WidgetJoystickEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 31.01.20
 * @updated on 14.04.20
 * @modified by
 */
public class WidgetGridMapEntity extends BaseEntity {

    String topic;
    int colorScheme;
    boolean drawBehind;
    boolean useTimeStamp;


    public WidgetGridMapEntity() {
        this.setType(WidgetEntity.MAP);
    }

    @Override
    public String getName() {
        return "Gridmap";
    }

    @Override
    public int getEntityType() {
        return WidgetEntity.MAP;
    }

    @Override
    public int getWidgetVizViewId() {
        return 0;
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return GridMapView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_map;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return GridDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return null;
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
