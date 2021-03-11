package com.schneewittchen.rosandroid.widgets.label;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.details.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.utility.Utils;
import androidx.annotation.NonNull;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class LabelDetailVH extends BaseDetailViewHolder<LabelEntity> {
    private EditText labelTextText;
    private Spinner labelTextRotationSpinner;

    private ArrayAdapter<CharSequence> rotationAdapter;

    public LabelDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public void initView(View view) {
        labelTextText = view.findViewById(R.id.labelText);
        labelTextRotationSpinner = view.findViewById(R.id.labelTextRotation);

        // Init spinner
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rotation, android.R.layout.simple_spinner_dropdown_item);

        labelTextRotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bindEntity(LabelEntity entity) {
        labelTextText.setText(entity.text);
        labelTextRotationSpinner.setSelection(rotationAdapter.getPosition(Utils.numberToDegrees(entity.rotation)));
    }

    @Override
    protected void updateEntity() {
        entity.text = labelTextText.getText().toString();
        entity.rotation = Utils.degreesToNumber(labelTextRotationSpinner.getSelectedItem().toString());
    }
}
