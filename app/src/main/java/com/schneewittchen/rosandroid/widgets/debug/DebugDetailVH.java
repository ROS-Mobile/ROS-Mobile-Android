package com.schneewittchen.rosandroid.widgets.debug;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.RosTopic;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on
 * @modified by
 */
public class DebugDetailVH extends BaseDetailViewHolder<WidgetDebugEntity> {

    // Spinner topicNameText;
    Spinner topicNameText;
    EditText topicTypeText;

    public DebugDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);
        topicTypeText = view.findViewById(R.id.topicTypeText);

        topicTypeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        topicNameText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateMessageType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateMessageType();
            }
        });


    }

    @Override
    public void bind(WidgetDebugEntity entity) {
    }

    @Override
    public void updateEntity() {
        updateSpinner();
    }

    public void updateMessageType() {
        String currentTopicName = topicNameText.getSelectedItem().toString();
        String currentMessageType = "";
        for (RosTopic rosTopic: mViewModel.getTopicList()) {
            if(rosTopic.name.equals(currentTopicName)) {
                currentMessageType = rosTopic.type;
            }
        }
        topicTypeText.setText(currentMessageType);
        entity.subPubNoteEntity.topic = topicNameText.getSelectedItem().toString();;
        entity.subPubNoteEntity.messageType = topicTypeText.getText().toString();;
    }

    public void updateSpinner() {
        // Get the list
        List<String> nameList = new ArrayList<>();
        for (RosTopic rosTopic: mViewModel.getTopicList()) {
            nameList.add(rosTopic.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.mViewModel.getApplication().getBaseContext(),
                android.R.layout.simple_spinner_dropdown_item, nameList);
        topicNameText.setAdapter(adapter);
    }
}
