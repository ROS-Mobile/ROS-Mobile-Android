package com.schneewittchen.rosandroid.ui.views;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.SubscriberEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.utility.CustomSpinner;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.09.20
 * @updated on
 * @modified by
 */
public abstract class BaseDetailSubscriberVH<T extends SubscriberEntity> extends BaseDetailViewHolder<T> implements CustomSpinner.OnSpinnerEventsListener {

    public static final String TAG = BaseDetailViewHolder.class.getSimpleName();

    protected CustomSpinner topicNameTextView;
    protected CustomSpinner topicTypeTextView;

    private List<String> topicNameList;
    private List<String> topicTypeList;

    private ArrayAdapter<String> topicTypeAdapter;
    private ArrayAdapter<String> topicNameAdapter;


    public BaseDetailSubscriberVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    public List<String> getTopicTypes(){
        return null;
    }


    @Override
    protected void baseInitView(View parentView) {
        super.baseInitView(parentView);

        Log.i(TAG, "init");

        // Initialize Views
        topicNameTextView = parentView.findViewById(R.id.topicNameText);
        topicTypeTextView = parentView.findViewById(R.id.topicTypeText);

        // Initialize Topic Name Spinner
        topicNameList = new ArrayList<>();
        topicNameAdapter = new ArrayAdapter<>(parentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, topicNameList);
        topicNameTextView.setAdapter(topicNameAdapter);

        // Initialize Topic Type Spinner
        topicTypeAdapter = new ArrayAdapter<>(parentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, topicTypeList);
        topicTypeTextView.setAdapter(topicTypeAdapter);

        topicNameTextView.setSpinnerEventsListener(this);
        topicTypeTextView.setSpinnerEventsListener(this);

        /* Define action responses for topic names
        topicNameText.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                updateTopicNameSpinner();
            }

            @Override
            public void onSpinnerClosed() {
                updateEntity();
            }
        });

        // Define action responses for message type
        topicTypeText.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() { }

            @Override
            public void onSpinnerClosed() {
                updateEntity();
            }
        });
         */
    }

    @Override
    protected void baseBindEntity(T entity) {
        super.baseBindEntity(entity);

        Log.i(TAG, "bind");
        updateTopicNameSpinner();

        String messageType = entity.topic.type;
        String topicName = entity.topic.name;

        topicNameTextView.setSelection(topicNameList.indexOf(topicName));
        topicTypeTextView.setSelection(topicTypeList.indexOf(messageType));
    }

    @Override
    protected void baseUpdateEntity() {
        super.baseUpdateEntity();

        Log.i(TAG, "updateEntity");

        if (topicTypeTextView.getSelectedItem() != null) {
            entity.topic.type = topicTypeTextView.getSelectedItem().toString();
        }

        if (topicNameTextView.getSelectedItem() != null) {
            entity.topic.name = topicNameTextView.getSelectedItem().toString();
        }
    }

    void updateTopicNameSpinner() {
        // Get the list with all suitable topics
        topicNameList = new ArrayList<>();

        for (Topic rosTopic: mViewModel.getTopicList()) {
            if (rosTopic.type.equals(entity.topic.type)) {
                topicNameList.add(rosTopic.name);
            }
        }

        topicNameAdapter.clear();
        topicNameAdapter.addAll(topicNameList);
    }

    protected void setTopicTypeList(List<String> list) {
        this.topicTypeList = list;
    }

    @Override
    public void onSpinnerOpened(CustomSpinner spinner) {

    }

    @Override
    public void onSpinnerItemSelected(CustomSpinner spinner, Integer position) {
        if (position == null) {
            // Nothing selected
            return;
        }

        spinner.setSelection(position);
    }

    @Override
    public void onSpinnerClosed(CustomSpinner spinner) {

    }
}
