package com.schneewittchen.rosandroid.widgets.joystick;

import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;

import java.util.Locale;


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
public class JoystickDetailVH extends BaseDetailViewHolder<JoystickEntity> {

    private TextInputEditText topicNameEditText;
    private TextInputEditText topicTypeEditText;

    private Spinner xDirSpinner;
    private Spinner xAxisSpinner;
    private EditText xScaleLeft;
    private EditText xScaleRight;
    private TextView xScaleMiddle;

    private Spinner yDirSpinner;
    private Spinner yAxisSpinner;
    private EditText yScaleLeft;
    private EditText yScaleRight;
    private TextView yScaleMiddle;

    private ArrayAdapter<CharSequence> xDirAdapter;
    private ArrayAdapter<CharSequence> xAxisAdapter;
    private ArrayAdapter<CharSequence> yDirAdapter;
    private ArrayAdapter<CharSequence> yAxisAdapter;


    public JoystickDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    public void initView(View parentView) {
        topicNameEditText = parentView.findViewById(R.id.topicNameEditText);
        topicTypeEditText = parentView.findViewById(R.id.topicTypeEditText);

        xDirSpinner = parentView.findViewById(R.id.xDirSpinner);
        xAxisSpinner = parentView.findViewById(R.id.xAxisSpinner);
        xScaleLeft = parentView.findViewById(R.id.xScaleLeft);
        xScaleRight = parentView.findViewById(R.id.xScaleRight);
        xScaleMiddle = parentView.findViewById(R.id.xScaleMiddle);

        yDirSpinner = parentView.findViewById(R.id.yDirSpinner);
        yAxisSpinner = parentView.findViewById(R.id.yAxisSpinner);
        yScaleLeft = parentView.findViewById(R.id.yScaleLeft);
        yScaleRight = parentView.findViewById(R.id.yScaleRight);
        yScaleMiddle = parentView.findViewById(R.id.yScaleMiddle);

        // Init spinner
        xDirAdapter = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.geometry_msg_twist_dir, android.R.layout.simple_spinner_dropdown_item);
        xAxisAdapter = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.geometry_msg_twist_axis, android.R.layout.simple_spinner_dropdown_item);
        yDirAdapter = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.geometry_msg_twist_dir, android.R.layout.simple_spinner_dropdown_item);
        yAxisAdapter = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.geometry_msg_twist_axis, android.R.layout.simple_spinner_dropdown_item);

        xDirSpinner.setAdapter(xDirAdapter);
        xAxisSpinner.setAdapter(xAxisAdapter);
        yDirSpinner.setAdapter(yDirAdapter);
        yAxisSpinner.setAdapter(yAxisAdapter);
    }

    @Override
    public void bindEntity(JoystickEntity entity) {
        topicNameEditText.setText(entity.topic.name);
        topicTypeEditText.setText(entity.topic.type);

        String[] xAxisMapping = entity.xAxisMapping.split("/");

        xDirSpinner.setSelection(xDirAdapter.getPosition(xAxisMapping[0]));
        xAxisSpinner.setSelection(xAxisAdapter.getPosition(xAxisMapping[1]));

        String[] yAxisMapping = entity.yAxisMapping.split("/");
        yDirSpinner.setSelection(yDirAdapter.getPosition(yAxisMapping[0]));
        yAxisSpinner.setSelection(yAxisAdapter.getPosition(yAxisMapping[1]));

        xScaleLeft.setText(String.format(Locale.US, "%.2f", entity.xScaleLeft));
        xScaleRight.setText(String.format(Locale.US, "%.2f", entity.xScaleRight));
        xScaleMiddle.setText(String.format(Locale.US, "%.2f", (entity.xScaleRight + entity.xScaleLeft) / 2));
        yScaleLeft.setText(String.format(Locale.US, "%.2f", entity.yScaleLeft));
        yScaleRight.setText(String.format(Locale.US, "%.2f", entity.yScaleRight));
        yScaleMiddle.setText(String.format(Locale.US, "%.2f", (entity.yScaleRight + entity.yScaleLeft) / 2));
    }

    @Override
    public void updateEntity() {
        // Update Topic name
        Editable newTopicName = topicNameEditText.getText();

        if(newTopicName != null && newTopicName.length() > 0) {
            entity.topic.name = newTopicName.toString();
        } else {
            topicNameEditText.setText(entity.topic.name);
        }

        // Update Topic Type
        Editable newTopicType = topicTypeEditText.getText();

        if(newTopicType != null && newTopicType.length() > 0) {
            entity.topic.type = newTopicType.toString();
        }else {
            topicTypeEditText.setText(entity.topic.type);
        }

        // Update joystick parameters
        entity.xAxisMapping = xDirSpinner.getSelectedItem() + "/" + xAxisSpinner.getSelectedItem();
        entity.yAxisMapping = yDirSpinner.getSelectedItem() + "/" + yAxisSpinner.getSelectedItem();

        for (String str: new String[]{"xScaleLeft", "xScaleRight", "yScaleLeft", "yScaleRight"}) {
            try {
                EditText editText = (EditText) this.getClass().getDeclaredField(str).get(this);

                assert editText != null;
                float value = Float.parseFloat(editText.getText().toString());

                entity.getClass().getField(str).set(entity, value);

            } catch (Exception ignored) {
            }
        }
    }
}
