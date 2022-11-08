package com.schneewittchen.rosandroid.ui.views.details;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.ui.general.WidgetChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.09.20
 * @updated on 05.11.2020
 * @modified by Nico Studt
 */
public abstract class BaseDetailSubscriberVH<T extends SubscriberWidgetEntity> extends BaseDetailViewHolder<T> {

    public static final String TAG = BaseDetailViewHolder.class.getSimpleName();

    protected AutoCompleteTextView topicNameTextView;
    protected TextInputEditText topicTypeEditText;
    protected TextInputLayout topicNameInputLayout;

    private List<Topic> availableTopics;
    private List<String> topicNameItemList;

    private ArrayAdapter<String> topicNameAdapter;


    public BaseDetailSubscriberVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    public abstract List<String> getTopicTypes();


    @Override
    protected void baseInitView(View parentView) {
        super.baseInitView(parentView);

        Log.i(TAG, "init");

        // Initialize Topic Edittext
        topicTypeEditText = parentView.findViewById(R.id.topicTypeEditText);
        topicNameInputLayout = parentView.findViewById(R.id.topicNameLayout);

        // Initialize Topic Name Spinner
        topicNameItemList = new ArrayList<>();

        topicNameTextView = parentView.findViewById(R.id.topicNameTextView);
        topicNameAdapter = new ArrayAdapter<>(parentView.getContext(),
                                                R.layout.dropdown_menu_popup_item, topicNameItemList);

        topicNameTextView.setAdapter(topicNameAdapter);
        topicNameTextView.setOnClickListener(clickedView -> {
            updateTopicNameSpinner();
            topicNameTextView.showDropDown();
        });

        topicNameInputLayout.setEndIconOnClickListener(v -> {
            topicNameTextView.requestFocus();
            topicNameTextView.callOnClick();
        });

        topicNameTextView.setOnItemClickListener((parent, view, position, id) -> selectNameItem(position));
    }

    @Override
    protected void baseBindEntity(T entity) {
        super.baseBindEntity(entity);

        String topicName = entity.topic.name;
        String messageType = entity.topic.type;

        topicNameTextView.setText(topicName, false);
        topicTypeEditText.setText(messageType);
    }

    @Override
    protected void baseUpdateEntity() {
        super.baseUpdateEntity();

        entity.topic.name = topicNameTextView.getText().toString();
        entity.topic.type = topicTypeEditText.getText().toString();
    }


    private void selectNameItem(int position) {
        String selectedName = topicNameItemList.get(position);

        // Search for topic type required for selected name
        for (Topic rosTopic: availableTopics) {
            if (rosTopic.name.equals(selectedName)) {
                topicTypeEditText.setText(rosTopic.type);
            }
        }

        itemView.requestFocus();
    }

    private void updateTopicNameSpinner() {
        // Get the list with all suitable topics
        topicNameItemList = new ArrayList<>();

        availableTopics = mViewModel.getTopicList();

        for (Topic rosTopic: availableTopics) {
            if (getTopicTypes().isEmpty()) {
                topicNameItemList.add(rosTopic.name);
            }

            for (String topicType: getTopicTypes()) {
                if (rosTopic.type.equals(topicType)) {
                    topicNameItemList.add(rosTopic.name);
                    break;
                }
            }
        }

        // Ros has no topics -> Default name
        if (topicNameItemList.isEmpty()) {
            topicNameItemList.add(entity.topic.name);
        } else{
            Collections.sort(topicNameItemList);
        }

        topicNameAdapter.clear();
        topicNameAdapter.addAll(topicNameItemList);
    }

}
