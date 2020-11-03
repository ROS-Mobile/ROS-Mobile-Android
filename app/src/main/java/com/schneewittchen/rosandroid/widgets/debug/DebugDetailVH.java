package com.schneewittchen.rosandroid.widgets.debug;

import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.utility.CustomSpinner;

import org.ros.gradle_plugins.RosJavaPlugin;
import org.ros.internal.message.Message;
import org.ros.internal.message.RawMessage;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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
    public DebugDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    // Initialization
    @Override
    public void initView(View view) {
        // Initialize Views
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);
        numberDebugMsgs = view.findViewById(R.id.numberDebug);

        // Initialize Spinner
        nameList = new ArrayList<>();
        adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, nameList);
        topicNameText.setAdapter(adapter);

        // Define action responses
        topicNameText.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            @Override
            public void onSpinnerOpened(CustomSpinner spinner) {
                updateSpinner();
            }

            @Override
            public void onSpinnerItemSelected(CustomSpinner spinner, Integer position) {
                forceWidgetUpdate();
            }

            @Override
            public void onSpinnerClosed(CustomSpinner spinner) {

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
                    forceWidgetUpdate();
                } catch (Exception ignored) {
                }
            }
        });
    }


    @Override
    public void bindEntity(DebugEntity entity) {
        updateSpinner();
        topicNameText.setSelection(nameList.indexOf(entity.topic.name));

        // topicTypeText.setText(entity.topic.type);

        if (entity.validMessage) {
            topicTypeText.setText(entity.topic.type);

        } else {
            String tmp = entity.topic.type + " (unsupported)";

            Spannable spannable = new SpannableString(tmp);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), entity.topic.type.length(),
                    tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            topicTypeText.setText(spannable);
        }

        numberDebugMsgs.setText(Integer.toString(entity.numberMessages));
    }

    @Override
    public void updateEntity() {
        if (topicNameText.getSelectedItem() != null) {
            String currentTopicName = topicNameText.getSelectedItem().toString();
            String currentMessageType = "";

            for (Topic rosTopic : mViewModel.getTopicList()) {
                if (rosTopic.name.equals(currentTopicName)) {
                    currentMessageType = rosTopic.type;
                }
            }

            entity.topic.name = topicNameText.getSelectedItem().toString();
            entity.topic.type = currentMessageType;
        }
    }

    public void updateSpinner() {
        // Get the list
        nameList = new ArrayList<>();

        for (Topic rosTopic: mViewModel.getTopicList()) {
            nameList.add(rosTopic.name);
        }

        nameList.sort(Comparator.naturalOrder());

        adapter.clear();
        adapter.addAll(nameList);
    }
}