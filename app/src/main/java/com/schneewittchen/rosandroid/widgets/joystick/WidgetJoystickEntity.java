package com.schneewittchen.rosandroid.widgets.joystick;

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
 * @version 1.1.1
 * @created on 31.01.20
 * @updated on 14.04.20
 * @modified by
 */
public class WidgetJoystickEntity extends BaseEntity {

    public WidgetJoystickEntity() {
        this.setType("Joystick");
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "cmd_vel";
        this.subPubNoteEntity.messageType = geometry_msgs.Twist._TYPE;
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

    @Override
    public WidgetJoystickEntity copy() {
        WidgetJoystickEntity newEnt = new WidgetJoystickEntity();
        this.fillContend(newEnt);

        return newEnt;
    }

}
