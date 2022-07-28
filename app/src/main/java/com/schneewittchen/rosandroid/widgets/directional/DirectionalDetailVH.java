package com.schneewittchen.rosandroid.widgets.directional;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.PublisherWidgetViewHolder;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.button.ButtonEntity;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import geometry_msgs.Twist;
import std_msgs.Bool;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 13.02.20
 * @updated on 20.05.20
 * @modified by Nico Studt
 * @updated on 05.11.2020
 * @modified by Nico Studt
 */
public class DirectionalDetailVH extends PublisherWidgetViewHolder {

    private EditText textText;
    private EditText speedText;
    private Spinner rotationSpinner;
    private Spinner axisSpinner;
    private Spinner senseSpinner;
    private Spinner directionSpinner;
    private ArrayAdapter<CharSequence> rotationAdapter;
    private ArrayAdapter<CharSequence> axisAdapter;
    private ArrayAdapter<CharSequence> senseAdapter;
    private ArrayAdapter<CharSequence> directionAdapter;


    @Override
    public void initView(View view) {
        textText = view.findViewById(R.id.btnTextTypeText);
        speedText = view.findViewById(R.id.speedTextTypeText);

        rotationSpinner = view.findViewById(R.id.btnTextRotation);
        axisSpinner = view.findViewById(R.id.btnTextAxis);
        directionSpinner = view.findViewById(R.id.btnTextDirection);
        senseSpinner = view.findViewById(R.id.btnTextSense);

        // Init spinners
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.button_rotation, android.R.layout.simple_spinner_dropdown_item);
        rotationSpinner.setAdapter(rotationAdapter);

        axisAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.directional_twist_axis, android.R.layout.simple_spinner_dropdown_item);
        axisSpinner.setAdapter(axisAdapter);

        directionAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.directional_twist_dir, android.R.layout.simple_spinner_dropdown_item);
        directionSpinner.setAdapter(directionAdapter);

        senseAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.directional_twist_sense, android.R.layout.simple_spinner_dropdown_item);
        senseSpinner.setAdapter(senseAdapter);
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        DirectionalEntity buttonEntity = (DirectionalEntity) entity;

        textText.setText(buttonEntity.text);
        speedText.setText(String.valueOf(buttonEntity.speed));

        String degrees = Utils.numberToDegrees(buttonEntity.rotation);

        rotationSpinner.setSelection(rotationAdapter.getPosition(degrees));
        axisSpinner.setSelection(axisAdapter.getPosition(buttonEntity.axis));
        directionSpinner.setSelection(directionAdapter.getPosition(buttonEntity.direction));
        senseSpinner.setSelection(senseAdapter.getPosition(buttonEntity.sense));
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        DirectionalEntity buttonEntity = (DirectionalEntity)entity;

        buttonEntity.text = textText.getText().toString();
        if (speedText.getText().length() > 0 ) {
            buttonEntity.speed = Double.parseDouble(speedText.getText().toString());
        }
        String degrees = rotationSpinner.getSelectedItem().toString();
        buttonEntity.rotation = Utils.degreesToNumber(degrees);
        buttonEntity.axis = axisSpinner.getSelectedItem().toString();
        buttonEntity.direction = directionSpinner.getSelectedItem().toString();
        buttonEntity.sense = senseSpinner.getSelectedItem().toString();
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(Bool._TYPE);
    }
}
