package com.schneewittchen.rosandroid.widgets.gridmap;

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
 * @version 1.0.1
 * @created on 13.02.20
 * @updated on 13.05.20
 * @modified by Nico Studt
 */
public class GridMapDetailVH extends BaseDetailViewHolder<WidgetGridMapEntity> {

    EditText topicNameText;
    EditText topicTypeText;


    public GridMapDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(WidgetGridMapEntity entity) {
        topicNameText.setText(entity.subPubNoteEntity.topic);
        topicTypeText.setText(entity.subPubNoteEntity.messageType);
    }

    @Override
    public void updateEntity() {
        entity.subPubNoteEntity.messageType = topicTypeText.getText().toString();
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();
    }
}
