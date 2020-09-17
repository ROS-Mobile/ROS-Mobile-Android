package com.schneewittchen.rosandroid.widgets.debug;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.camera.CameraDetailVH;
import com.schneewittchen.rosandroid.widgets.camera.CameraNode;
import com.schneewittchen.rosandroid.widgets.camera.CameraView;

import org.ros.internal.node.response.Response;
import org.ros.master.client.TopicType;
import org.ros.node.topic.Subscriber;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class WidgetDebugEntity extends BaseEntity {

    public WidgetDebugEntity() {
        this.setType("Debug");

        this.width = 4;
        this.height = 3;
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "debug";
        this.subPubNoteEntity.messageType = Subscriber.TOPIC_MESSAGE_TYPE_WILDCARD;
        this.numberMessages = 10;
    }


    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return DebugView.class;
    }

    // TODO: Add own layout for image style?
    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_debug;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return DebugDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return DebugNode.class;
    }


    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.subPubNoteEntity.topic = entity.subPubNoteEntity.topic;
        this.subPubNoteEntity.messageType = entity.subPubNoteEntity.messageType;
        this.numberMessages = entity.numberMessages;
        this.validMessage = entity.validMessage;
    }

    @Override
    public WidgetDebugEntity copy() {
        WidgetDebugEntity newEnt = new WidgetDebugEntity();
        newEnt.insert(this);

        return newEnt;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetDebugEntity))
            return false;

        WidgetDebugEntity other = (WidgetDebugEntity) widget;

        return this.subPubNoteEntity.equals(other.subPubNoteEntity)
                && this.numberMessages == other.numberMessages
                && this.validMessage == other.validMessage;
    }
}

