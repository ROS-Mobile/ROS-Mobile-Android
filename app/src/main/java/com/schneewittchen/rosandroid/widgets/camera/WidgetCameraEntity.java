package com.schneewittchen.rosandroid.widgets.camera;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.joystick.WidgetJoystickEntity;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 27.04.20
 * @updated on
 * @modified by
 */
public class WidgetCameraEntity extends BaseEntity {

    String topic;
    int colorScheme;
    boolean drawBehind;
    boolean useTimeStamp;


    public WidgetCameraEntity() {
        this.setType("Camera");

        this.width = 4;
        this.height = 3;
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "camera/image_raw";
        this.subPubNoteEntity.messageType = sensor_msgs.Image._TYPE;
    }


    @Override
    public String getName() {
        return "Camera";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return CameraView.class;
    }

    // TODO: Add own layout for image style?
    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_camera;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return CameraDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return CameraNode.class;
    }


    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.subPubNoteEntity.topic = entity.subPubNoteEntity.topic;
        this.subPubNoteEntity.messageType = entity.subPubNoteEntity.messageType;
    }

    @Override
    public WidgetCameraEntity copy() {
        WidgetCameraEntity newEnt = new WidgetCameraEntity();
        newEnt.insert(this);

        return newEnt;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetCameraEntity))
            return false;

        WidgetCameraEntity other = (WidgetCameraEntity) widget;

        return this.subPubNoteEntity.equals(other.subPubNoteEntity);
    }
}

