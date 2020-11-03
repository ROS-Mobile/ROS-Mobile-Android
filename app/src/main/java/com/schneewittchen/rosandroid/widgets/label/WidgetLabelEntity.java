package com.schneewittchen.rosandroid.widgets.label;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.SubPubNoteEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.BaseView;

public class WidgetLabelEntity extends BaseEntity {
    public  WidgetLabelEntity() {
        this.setType("Label");

        this.width=3;
        this.height=1;
        this.subPubNoteEntity = new SubPubNoteEntity();

        this.text = "A label";
        this.rotation = 0;
    }


    @Override
    public String getName() {
        return "Label";
    }

    @Override
    public Class<? extends BaseView> getViewType() {
        return LabelView.class;
    }

    @Override
    public int getWidgetDetailViewId() {
        return R.layout.widget_detail_label;
    }

    @Override
    public Class<? extends BaseDetailViewHolder> getDetailViewHolderType() {
        return LabelDetailVH.class;
    }

    @Override
    public Class<? extends BaseNode> getNodeType() {
        return LabelNode.class;
    }

    @Override
    public boolean equalContent(BaseEntity other) {
        return this.text == other.text;
    }

    @Override
    public void insert(WidgetEntity entity) {
        super.insert(entity);

        this.rotation = entity.rotation;
        this.text = entity.text;
    }

    @Override
    public BaseEntity copy() {
        WidgetLabelEntity newEnt = new WidgetLabelEntity();
        newEnt.insert(this);

        return newEnt;
    }
}
