package com.schneewittchen.rosandroid.widgets.logger;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.BaseView;
import com.schneewittchen.rosandroid.widgets.camera.WidgetCameraEntity;

public class WidgetLoggerEntity extends BaseEntity {
    public WidgetLoggerEntity() {
        this.setType("Logger");

        this.width=3;
        this.height=1;
        this.subPubNoteEntity = new SubPubNoteEntity();
        this.subPubNoteEntity.topic = "log";
        this.subPubNoteEntity.messageType = std_msgs.String._TYPE;

        this.text = "A logger";
        this.rotation = 0;
    }


    @Override
    public String getName() {
        return "Logger";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return LoggerView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_logger;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return LoggerDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return LoggerNode.class;
    }

    @Override
    public boolean equalContent(BaseEntity widget) {
        if (!(widget instanceof WidgetLoggerEntity))
            return false;

        WidgetLoggerEntity other = (WidgetLoggerEntity) widget;

        return this.subPubNoteEntity.equals(other.subPubNoteEntity);
    }

    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.subPubNoteEntity.topic = entity.subPubNoteEntity.topic;
        this.subPubNoteEntity.messageType = entity.subPubNoteEntity.messageType;

        this.rotation = entity.rotation;
        this.text = entity.text;
    }

    @Override
    public BaseEntity copy() {
        WidgetLoggerEntity newEnt = new WidgetLoggerEntity();
        newEnt.insert(this);

        return newEnt;
    }
}
