package com.schneewittchen.rosandroid.widgets.debug;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.gridmap.GridMapEntity;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class DebugDetailVH extends BaseDetailViewHolder<DebugEntity> {

    EditText topicNameText;
    EditText topicTypeText;


    public DebugDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(DebugEntity entity) {
        topicNameText.setText(entity.topic.name);
        topicTypeText.setText(entity.topic.type);
    }

    @Override
    public void updateEntity() {
        this.widget.topic.type = topicTypeText.getText().toString();
        this.widget.topic.name = topicNameText.getText().toString();
    }
}