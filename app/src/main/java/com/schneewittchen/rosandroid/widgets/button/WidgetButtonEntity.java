package com.schneewittchen.rosandroid.widgets.button;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.joystick.WidgetJoystickEntity;

public class WidgetButtonEntity extends BaseEntity {
    public  WidgetButtonEntity(){
        this.setType("Button");

        this.width=3;
        this.height=4;
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "btn_press";
        this.subPubNoteEntity.messageType = std_msgs.Bool._TYPE;
        this.text = "A button";
        this.rotation = 0;
    }

    @Override
    public String getName() {
        return "Button";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return ButtonView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_button;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return ButtonDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return ButtonNode.class;
    }

    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.subPubNoteEntity.topic = entity.subPubNoteEntity.topic;
        this.subPubNoteEntity.messageType = entity.subPubNoteEntity.messageType;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetButtonEntity))
            return false;

        WidgetButtonEntity other = (WidgetButtonEntity)widget;

        return this.subPubNoteEntity.equals(other.subPubNoteEntity);
    }

    @Override
    public BaseEntity copy() {
        WidgetButtonEntity newEnt = new WidgetButtonEntity();
        newEnt.insert(this);

        return newEnt;
    }

    public String numberToDegrees(int number){
        return new Integer(number).toString() + "°";
    }

    public int degreesToNumber(String degrees) {
        return Integer.parseInt(degrees.substring(0, degrees.length() - 1));
    }
}
