package com.schneewittchen.rosandroid.ui.views.details;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nav_msgs.OccupancyGrid;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.21
 */
public class PublisherViewHolder implements IBaseViewHolder {

    public static final String TAG = PublisherViewHolder.class.getSimpleName();

    private TextInputEditText topicNameEditText;
    private TextInputEditText topicTypeEditText;
    private List<Topic> availableTopics;
    private List<String> topicNameItemList;
    public List<String> topicTypes;
    public DetailsViewModel viewModel;


    public PublisherViewHolder(DetailsViewModel viewModel, List<String> topicTypes) {
        this.viewModel = viewModel;
        this.topicTypes = topicTypes;
    }

    @Override
    public void baseInitView(View widgetView) {

        // Initialize Topic Edittext
        topicTypeEditText = widgetView.findViewById(R.id.topicTypeEditText);
        topicNameEditText = widgetView.findViewById(R.id.topicNameEditText);

        // Initialize Topic Name Spinner
        topicNameItemList = new ArrayList<>();

        /*
        topicNameTextView = widgetView.findViewById(R.id.topicNameEditText);
        topicNameAdapter = new ArrayAdapter<>(widgetView.getContext(),
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
         */
    }

    @Override
    public void baseBindEntity(BaseEntity entity) {
        Log.i(TAG, "Base bind " + entity);
        Log.i(TAG, "Topic " + entity.topic);
        Log.i(TAG, "Name " + entity.topic.name);
        String topicName = entity.topic.name;
        String messageType = entity.topic.type;

        topicNameEditText.setText(topicName);
        topicTypeEditText.setText(messageType);
    }

    @Override
    public void baseUpdateEntity(BaseEntity entity) {
        entity.topic.name = topicNameEditText.getText().toString();
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

        //itemView.requestFocus();
    }

    private void updateTopicNameSpinner() {
        // Get the list with all suitable topics
        topicNameItemList = new ArrayList<>();

        availableTopics = viewModel.getTopicList();

        for (Topic rosTopic: availableTopics) {
            if (topicTypes.isEmpty()) {
                topicNameItemList.add(rosTopic.name);
            }

            for (String topicType: topicTypes) {
                if (rosTopic.type.equals(topicType)) {
                    topicNameItemList.add(rosTopic.name);
                    break;
                }
            }
        }

        // Ros has no topics -> Default name
        if (topicNameItemList.isEmpty()) {
            //TODO: topicNameItemList.add(entity.topic.name);
        } else{
            Collections.sort(topicNameItemList);
        }

    }
}
