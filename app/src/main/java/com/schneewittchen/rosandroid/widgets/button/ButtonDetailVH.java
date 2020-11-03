package com.schneewittchen.rosandroid.widgets.button;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;
import com.schneewittchen.rosandroid.widgets.joystick.WidgetJoystickEntity;

import androidx.annotation.NonNull;

public class ButtonDetailVH extends BaseDetailViewHolder<WidgetButtonEntity> {
    private EditText topicNameText;

    private EditText textText;
    private Spinner rotationSpinner;

    private ArrayAdapter<CharSequence> rotationAdapter;

    public ButtonDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);

        textText = view.findViewById(R.id.btnTextTypeText);
        rotationSpinner = view.findViewById(R.id.btnTextRotation);

        // Init spinner
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rotation, android.R.layout.simple_spinner_dropdown_item);

        rotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bind(WidgetButtonEntity entity) {
        topicNameText.setText(entity.subPubNoteEntity.topic);

        textText.setText(entity.text);
        rotationSpinner.setSelection(rotationAdapter.getPosition(entity.numberToDegrees(entity.rotation)));
    }

    @Override
    public void updateEntity() {
        entity.subPubNoteEntity.messageType = std_msgs.Bool._TYPE;
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();

        entity.text = textText.getText().toString();
        entity.rotation = entity.degreesToNumber(rotationSpinner.getSelectedItem().toString());
    }
}
