package com.schneewittchen.rosandroid.widgets.camera;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 13.05.20
 * @updated on
 * @modified by Nico Studt
 */
public class CameraDetailVH extends BaseDetailViewHolder<WidgetCameraEntity> {

    EditText topicNameText;
    Spinner topicTypeText;

    public CameraDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(WidgetCameraEntity entity) {
        topicNameText.setText(entity.subPubNoteEntity.topic);
        this.entity.subPubNoteEntity.messageType =  topicTypeText.getSelectedItem().toString();
    }

    @Override
    public void updateEntity() {
        entity.subPubNoteEntity.messageType = topicTypeText.getSelectedItem().toString();
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();
    }
}
