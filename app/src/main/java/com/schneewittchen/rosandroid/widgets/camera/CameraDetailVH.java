package com.schneewittchen.rosandroid.widgets.camera;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 13.05.20
 * @updated on 22.10.2020
 * @modified by Nico Studt
 */
public class CameraDetailVH extends BaseDetailViewHolder<CameraEntity> {

    EditText topicNameText;
    Spinner topicTypeText;


    public CameraDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(CameraEntity entity) {
        topicNameText.setText(entity.topic.name);
    }

    @Override
    public void updateEntity() {
        this.widget.topic.type = topicTypeText.getSelectedItem().toString();
        this.widget.topic.name = topicNameText.getText().toString();
    }
}
