package com.schneewittchen.rosandroid.widgets.logger;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;

import androidx.annotation.NonNull;

public class LoggerDetailVH extends BaseDetailViewHolder<WidgetLoggerEntity> {
    private EditText topicNameText;
    private Spinner labelTextRotationSpinner;

    private ArrayAdapter<CharSequence> rotationAdapter;

    public LoggerDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.loggerTopicNameText);
        labelTextRotationSpinner = view.findViewById(R.id.loggerTextRotation);

        // Init spinner
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rotation, android.R.layout.simple_spinner_dropdown_item);

        labelTextRotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bind(WidgetLoggerEntity entity) {
        topicNameText.setText(entity.subPubNoteEntity.topic);
        labelTextRotationSpinner.setSelection(rotationAdapter.getPosition(Utils.numberToDegrees(entity.rotation)));
    }

    @Override
    protected void updateEntity() {
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();
        entity.rotation = Utils.degreesToNumber(labelTextRotationSpinner.getSelectedItem().toString());
    }
}
