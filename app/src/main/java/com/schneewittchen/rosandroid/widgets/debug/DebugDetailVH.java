package com.schneewittchen.rosandroid.widgets.debug;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.RosTopic;
import com.schneewittchen.rosandroid.utility.CustomSpinner;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;


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

    // Views
    CustomSpinner topicNameText;
    EditText topicTypeText;
    EditText numberDebugMsgs;

    // Spinner Variables
    List<String> nameList;
    ArrayAdapter<String> adapter;

    // Constructor
    public DebugDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }

    // Initialization
    @Override
    public void init(View view) {
        // Initialize Views
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
        numberDebugMsgs = view.findViewById(R.id.numberDebug);

        // Initialize Spinner
        nameList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, nameList);
        topicNameText.setAdapter(adapter);

        // Define action responses
        topicNameText.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened() {
                updateSpinner();
            }

            @Override
            public void onSpinnerClosed() {
                update();
            }
        });

        numberDebugMsgs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    entity.numberMessages = Integer.parseInt(numberDebugMsgs.getText().toString());
                    update();
                } catch (Exception ignored) {
                }
            }
        });
    }

    @Override
    public void bind(WidgetDebugEntity entity) {
        // Set current selection
        updateSpinner();
        topicNameText.setSelection(nameList.indexOf(entity.subPubNoteEntity.topic));
        if (entity.validMessage) {
            topicTypeText.setText(entity.subPubNoteEntity.messageType);
        } else {
            String tmp = entity.subPubNoteEntity.messageType + " (unsupported)";
            Spannable spannable = new SpannableString(tmp);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), entity.subPubNoteEntity.messageType.length(), tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            topicTypeText.setText(spannable);
        }
        numberDebugMsgs.setText(Integer.toString(entity.numberMessages));
    }

    @Override
    public void updateEntity() {
        if (topicNameText.getSelectedItem() != null) {
            String currentTopicName = topicNameText.getSelectedItem().toString();
            String currentMessageType = "";
            for (RosTopic rosTopic : mViewModel.getTopicList()) {
                if (rosTopic.name.equals(currentTopicName)) {
                    currentMessageType = rosTopic.type;
                }
            }
            entity.subPubNoteEntity.topic = topicNameText.getSelectedItem().toString();
            entity.subPubNoteEntity.messageType = currentMessageType;
        }
    }

    public void updateSpinner() {
        // Get the list
        nameList = new ArrayList<>();
        for (RosTopic rosTopic: mViewModel.getTopicList()) {
            nameList.add(rosTopic.name);
        }
        adapter.clear();
        adapter.addAll(nameList);
    }
}