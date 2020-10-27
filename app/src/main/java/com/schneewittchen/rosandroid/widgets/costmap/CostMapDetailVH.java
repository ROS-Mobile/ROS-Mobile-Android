package com.schneewittchen.rosandroid.widgets.costmap;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1
 * @created on 14.09.2020
 * @updated on 23.10.2020
 * @modified by Nico Studt
 */
public class CostMapDetailVH extends BaseDetailViewHolder<CostMapEntity> {

    EditText topicNameText;
    EditText topicTypeText;


    public CostMapDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
    }

    @Override
    public void bind(CostMapEntity entity) {
        topicNameText.setText(entity.topic.name);
        topicTypeText.setText(entity.topic.type);
    }

    @Override
    public void updateEntity() {
        this.widget.topic.type = topicTypeText.getText().toString();
        this.widget.topic.name = topicNameText.getText().toString();
    }
}
