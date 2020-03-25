package com.schneewittchen.rosandroid.widgets.gridmap;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.WidgetNode;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickDetailVH;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickNode;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickView;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 31.01.20
 * @updated on 16.02.20
 * @modified by
 */
public class WidgetGridMapEntity extends WidgetEntity {

    String topic;
    int colorScheme;
    boolean drawBehind;
    boolean useTimeStamp;


    public WidgetGridMapEntity() {
        this.setType(WidgetEntity.MAP);
    }


    @Override
    public String getName() {
        return "Map";
    }

    public Class<? extends BaseView> getViewType() {
        return GridMapView.class;
    }

    public Class<? extends WidgetNode> getNodeType() {
        return JoystickNode.class;
    }

    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return GridDetailVH.class;
    }

    public int getDetailViewLayoutId() {
        return R.layout.widget_detail_map;
    }
}
