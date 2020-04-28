package com.schneewittchen.rosandroid.widgets.camera;

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
        this.subscriber = new SubPubNoteEntity();
        this.subscriber.topic = "camera/image_raw";
        this.subscriber.messageType = sensor_msgs.Image._TYPE;
    }

    @Override
    public String getName() {
        return "Camera";
    }

    @Override
    public int getWidgetVizViewId() {
        return 0;
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
    public boolean equalContent(BaseEntity other) {
        return true;
    }

    @Override
    public WidgetCameraEntity copy() {
        WidgetCameraEntity newEnt = new WidgetCameraEntity();
        this.fillContend(newEnt);

        return newEnt;
    }
}

