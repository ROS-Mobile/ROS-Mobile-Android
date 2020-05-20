package com.schneewittchen.rosandroid.widgets.gps;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;
import com.schneewittchen.rosandroid.widgets.camera.WidgetCameraEntity;
import com.schneewittchen.rosandroid.widgets.gridmap.WidgetGridMapEntity;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 05.05.20
 * @modified by
 */

public class GpsDetailVH extends BaseDetailViewHolder<WidgetGpsEntity> {

    EditText topicNameText;
    EditText topicTypeText;


    public GpsDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(WidgetGpsEntity entity) {
        topicNameText.setText(entity.subPubNoteEntity.topic);
        topicTypeText.setText(entity.subPubNoteEntity.messageType);
    }

    @Override
    public void updateEntity() {
        entity.subPubNoteEntity.messageType = topicTypeText.getText().toString();
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();
    }
}
