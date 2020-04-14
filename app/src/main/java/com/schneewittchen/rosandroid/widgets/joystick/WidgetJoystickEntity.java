package com.schneewittchen.rosandroid.widgets.joystick;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 31.01.20
 * @updated on 02.04.20
 * @modified by
 */
public class WidgetJoystickEntity extends BaseEntity {

    public WidgetJoystickEntity() {
        this.setType(WidgetEntity.JOYSTICK);
    }

    @Override
    public int getEntityType() {
        return WidgetEntity.JOYSTICK;
    }

    @Override
    public String getName() {
        return "Joystick";
    }

    @Override
    public int getWidgetVizViewId() {
        return R.layout.widget_detail_joystick;
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return JoystickView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_joystick;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return JoystickDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return JoystickNode.class;
    }

    @Override
    public boolean equalContent(BaseEntity other) {
        System.err.println("Check equal content");
        return true;
    }

}
