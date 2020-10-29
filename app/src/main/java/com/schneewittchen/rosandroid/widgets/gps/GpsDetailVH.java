package com.schneewittchen.rosandroid.widgets.gps;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 27.10.2020
 * @modified by Nico Studt
 */

public class GpsDetailVH extends BaseDetailViewHolder<GpsEntity> {

    EditText topicNameText;
    EditText topicTypeText;


    public GpsDetailVH(@NonNull View view, WidgetChangeListener changeListener) {
        super(view, changeListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(GpsEntity entity) {
        topicNameText.setText(entity.topic.name);
        topicTypeText.setText(entity.topic.type);
    }

    @Override
    public void updateEntity() {
        this.widget.topic.type = topicTypeText.getText().toString();
        this.widget.topic.name = topicNameText.getText().toString();
    }
}
