package com.schneewittchen.rosandroid.ui.views.details;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.viewmodel.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.21
 */
public class PublisherViewHolder implements IBaseViewHolder, TextView.OnEditorActionListener{

    public static final String TAG = PublisherViewHolder.class.getSimpleName();

    private TextInputEditText topicNameEditText;
    private TextInputEditText topicTypeEditText;
    private List<Topic> availableTopics;
    private List<String> topicNameItemList;
    public List<String> topicTypes;
    public DetailsViewModel viewModel;


    @Override
    public void baseInitView(View widgetView) {
        // Initialize Topic Edittext
        topicNameEditText = widgetView.findViewById(R.id.topicNameEditText);
        topicTypeEditText = widgetView.findViewById(R.id.topicTypeEditText);

        topicNameEditText.setOnEditorActionListener(this);
        topicTypeEditText.setOnEditorActionListener(this);

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


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId){
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_PREVIOUS:
                Utils.hideSoftKeyboard(v);
                v.clearFocus();
                return true;
        }

        return false;
    }
}
