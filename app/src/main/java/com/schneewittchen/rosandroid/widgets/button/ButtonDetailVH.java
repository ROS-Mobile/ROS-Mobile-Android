package com.schneewittchen.rosandroid.widgets.button;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.PublisherWidgetViewHolder;
import com.schneewittchen.rosandroid.utility.Utils;

import java.util.Collections;
import java.util.List;

import std_msgs.Bool;


/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 * @updated on 20.03.2021
 * @modified by Nico Studt
 */
public class ButtonDetailVH extends PublisherWidgetViewHolder {

    private EditText textText;
    private Spinner rotationSpinner;
    private ArrayAdapter<CharSequence> rotationAdapter;


    @Override
    public void initView(View view) {
        textText = view.findViewById(R.id.btnTextTypeText);
        rotationSpinner = view.findViewById(R.id.btnTextRotation);

        // Init spinner
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.button_rotation, android.R.layout.simple_spinner_dropdown_item);

        rotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        ButtonEntity buttonEntity = (ButtonEntity) entity;

        textText.setText(buttonEntity.text);
        String degrees = Utils.numberToDegrees(buttonEntity.rotation);
        rotationSpinner.setSelection(rotationAdapter.getPosition(degrees));
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        ButtonEntity buttonEntity = (ButtonEntity)entity;

        buttonEntity.text = textText.getText().toString();
        String degrees = rotationSpinner.getSelectedItem().toString();
        buttonEntity.rotation = Utils.degreesToNumber(degrees);
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(Bool._TYPE);
    }
}
