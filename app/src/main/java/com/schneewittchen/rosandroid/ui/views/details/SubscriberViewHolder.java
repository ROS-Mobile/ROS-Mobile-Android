package com.schneewittchen.rosandroid.ui.views.details;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.21
 */
class SubscriberViewHolder implements IBaseViewHolder {

    private static String TAG = SubscriberViewHolder.class.getSimpleName();

    private AutoCompleteTextView topicNameTextView;
    private TextInputEditText topicTypeEditText;
    private TextInputLayout topicNameInputLayout;
    private ArrayAdapter<String> topicNameAdapter;
    private List<Topic> availableTopics;
    private List<String> topicNameItemList;
    private BaseEntity entity;
    public List<String> topicTypes;
    public DetailsViewModel viewModel;
    private DetailViewHolder parentViewHolder;


    public SubscriberViewHolder(DetailViewHolder parentViewHolder) {
        this.parentViewHolder = parentViewHolder;
    }


    @Override
    public void baseInitView(View view) {
        // Initialize Topic Edittext
        topicTypeEditText = view.findViewById(R.id.topicTypeEditText);
        topicNameTextView = view.findViewById(R.id.topicNameTextView);
        topicNameInputLayout = view.findViewById(R.id.topicNameLayout);

        // Initialize Topic Name Spinner
        topicNameItemList = new ArrayList<>();

        topicNameAdapter = new ArrayAdapter<>(view.getContext(),
                R.layout.dropdown_menu_popup_item, topicNameItemList);


        /*
        topicNameAdapter = new ArrayAdapter<String>(view.getContext(),
                R.layout.dropdown_menu_popup_item, topicNameItemList) {
            @Override
            public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
                final View v = super.getDropDownView(position, convertView, parent);
                v.post(() -> ((TextView) v).setSingleLine(false));
                return v;
            }
        };
        */

        topicNameTextView.setAdapter(topicNameAdapter);
        topicNameTextView.setOnClickListener(clickedView -> {
            updateTopicNameSpinner();
            topicNameTextView.showDropDown();
        });

        topicNameInputLayout.setEndIconOnClickListener(v -> {
            topicNameTextView.requestFocus();
            topicNameTextView.callOnClick();
        });

        topicNameTextView.setOnItemClickListener((parent, v, position, id) -> selectNameItem(position));
    }

    @Override
    public void baseBindEntity(BaseEntity entity) {
        this.entity = entity;
        String topicName = entity.topic.name;
        String messageType = entity.topic.type;

        topicNameTextView.setText(topicName, false);
        topicTypeEditText.setText(messageType);
    }

    @Override
    public void baseUpdateEntity(BaseEntity entity) {
        entity.topic.name = topicNameTextView.getText().toString();
        entity.topic.type = topicTypeEditText.getText().toString();
    }

    private void selectNameItem(int position) {
        String selectedName = topicNameItemList.get(position);

        // Search for topic type required for selected name
        for (Topic rosTopic : availableTopics) {
            if (rosTopic.name.equals(selectedName)) {
                topicTypeEditText.setText(rosTopic.type);
            }
        }

        topicTypeEditText.clearFocus();
        parentViewHolder.forceWidgetUpdate();
    }

    private void updateTopicNameSpinner() {
        // Get the list with all suitable topics
        topicNameItemList = new ArrayList<>();

        availableTopics = viewModel.getTopicList();

        for (Topic rosTopic : availableTopics) {
            if (topicTypes.isEmpty()) {
                topicNameItemList.add(rosTopic.name);
            }

            for (String topicType : topicTypes) {
                if (rosTopic.type.equals(topicType)) {
                    topicNameItemList.add(rosTopic.name);
                    break;
                }
            }
        }

        // Ros has no topics -> Default name
        if (topicNameItemList.isEmpty()) {
            topicNameItemList.add(entity.topic.name);
        } else {
            Collections.sort(topicNameItemList);
        }

        topicNameAdapter.clear();
        topicNameAdapter.addAll(topicNameItemList);
    }

}
