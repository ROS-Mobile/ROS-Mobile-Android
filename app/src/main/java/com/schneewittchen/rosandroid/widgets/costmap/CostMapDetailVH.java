package com.schneewittchen.rosandroid.widgets.costmap;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.09.2020
 * @updated
 * @modified
 */
public class CostMapDetailVH extends BaseDetailViewHolder<WidgetCostMapEntity> {

    EditText topicNameText;
    EditText topicTypeText;


    public CostMapDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(WidgetCostMapEntity entity) {
        topicNameText.setText(entity.subPubNoteEntity.topic);
        topicTypeText.setText(entity.subPubNoteEntity.messageType);
    }

    @Override
    public void updateEntity() {
        entity.subPubNoteEntity.messageType = topicTypeText.getText().toString();
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();
    }
}
