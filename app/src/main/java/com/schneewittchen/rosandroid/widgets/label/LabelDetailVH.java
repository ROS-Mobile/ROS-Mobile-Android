package com.schneewittchen.rosandroid.widgets.label;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;

import androidx.annotation.NonNull;

public class LabelDetailVH extends BaseDetailViewHolder<WidgetLabelEntity> {
    private EditText labelTextText;
    private Spinner labelTextRotationSpinner;

    private ArrayAdapter<CharSequence> rotationAdapter;

    public LabelDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public void init(View view) {
        labelTextText = view.findViewById(R.id.labelText);
        labelTextRotationSpinner = view.findViewById(R.id.labelTextRotation);

        // Init spinner
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rotation, android.R.layout.simple_spinner_dropdown_item);

        labelTextRotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bind(WidgetLabelEntity entity) {
        labelTextText.setText(entity.text);
        labelTextRotationSpinner.setSelection(rotationAdapter.getPosition(Utils.numberToDegrees(entity.rotation)));
    }

    @Override
    protected void updateEntity() {
        entity.text = labelTextText.getText().toString();
        entity.rotation = Utils.degreesToNumber(labelTextRotationSpinner.getSelectedItem().toString());
    }
}
