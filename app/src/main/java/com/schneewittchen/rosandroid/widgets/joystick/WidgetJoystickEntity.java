package com.schneewittchen.rosandroid.widgets.joystick;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
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
 * @updated on 10.05.20
 * @modified by Nico Studt
 */
public class WidgetJoystickEntity extends BaseEntity {

    public WidgetJoystickEntity() {
        this.setType("Joystick");

        this.width = 4;
        this.height = 4;
        this.publisher = new SubPubNoteEntity();
        this.publisher.topic = "cmd_vel";
        this.publisher.messageType = geometry_msgs.Twist._TYPE;
        this.xAxisMapping = "Linear/X";
        this.yAxisMapping = "Angular/Z";
        this.xScaleLeft = -1;
        this.xScaleRight = 1;
        this.yScaleLeft = -1;
        this.yScaleRight = 1;
    }

    @Override
    public String getName() {
        return "Joystick";
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
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.publisher.topic = entity.publisher.topic;
        this.publisher.messageType = entity.publisher.messageType;
        this.xAxisMapping = entity.xAxisMapping;
        this.yAxisMapping = entity.yAxisMapping;
        this.xScaleLeft = entity.xScaleLeft;
        this.xScaleRight = entity.xScaleRight;
        this.yScaleLeft = entity.yScaleLeft;
        this.yScaleRight = entity.yScaleRight;
    }

    @Override
    public WidgetJoystickEntity copy() {
        WidgetJoystickEntity newEnt = new WidgetJoystickEntity();
        newEnt.insert(this);

        return newEnt;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetJoystickEntity))
            return false;

        WidgetJoystickEntity other = (WidgetJoystickEntity) widget;

        return this.publisher.equals(other.publisher)
                && this.xAxisMapping.equals(other.xAxisMapping)
                && this.yAxisMapping.equals(other.yAxisMapping)
                && this.xScaleLeft == other.xScaleLeft
                && this.xScaleRight == other.xScaleRight
                && this.yScaleLeft == other.yScaleLeft
                && this.yScaleRight == other.yScaleRight;
    }

}
