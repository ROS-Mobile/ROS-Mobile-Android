package com.schneewittchen.rosandroid.widgets.gps2ros;

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

import sensor_msgs.NavSatFix;

/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */
public class Gps2RosDetailVH extends PublisherWidgetViewHolder {

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
    public void bindEntity(BaseEntity entity) {
        Gps2RosEntity gps2rosEntity = (Gps2RosEntity) entity;

        textText.setText(gps2rosEntity.text);
        String degrees = Utils.numberToDegrees(gps2rosEntity.rotation);
        rotationSpinner.setSelection(rotationAdapter.getPosition(degrees));
    }

    @Override
    public void updateEntity(BaseEntity entity) {
        Gps2RosEntity gps2rosEntity = (Gps2RosEntity) entity;

        gps2rosEntity.text = textText.getText().toString();
        String degrees = rotationSpinner.getSelectedItem().toString();
        gps2rosEntity.rotation = Utils.degreesToNumber(degrees);
    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(NavSatFix._TYPE);
    }

}
