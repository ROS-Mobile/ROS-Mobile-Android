package com.schneewittchen.rosandroid.widgets.joystick;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 13.02.20
 * @updated on 18.04.20
 * @modified by
 */
public class JoystickDetailVH extends BaseDetailViewHolder<WidgetJoystickEntity> {

    EditText topicNameText;

    Spinner xDirSpinner;
    Spinner xAxisSpinner;
    EditText xScaleLeft;
    EditText xScaleRight;
    TextView xScaleMiddle;

    Spinner yDirSpinner;
    Spinner yAxisSpinner;
    EditText yScaleLeft;
    EditText yScaleRight;
    TextView yScaleMiddle;

    ArrayAdapter<CharSequence> xDirAdapter;
    ArrayAdapter<CharSequence> xAxisAdapter;
    ArrayAdapter<CharSequence> yDirAdapter;
    ArrayAdapter<CharSequence> yAxisAdapter;


    public JoystickDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public void init(View view) {
        topicNameText = view.findViewById(R.id.topicNameText);

        xDirSpinner = view.findViewById(R.id.xDirSpinner);
        xAxisSpinner = view.findViewById(R.id.xAxisSpinner);
        xScaleLeft = view.findViewById(R.id.xScaleLeft);
        xScaleRight = view.findViewById(R.id.xScaleRight);
        xScaleMiddle = view.findViewById(R.id.xScaleMiddle);

        yDirSpinner = view.findViewById(R.id.yDirSpinner);
        yAxisSpinner = view.findViewById(R.id.yAxisSpinner);
        yScaleLeft = view.findViewById(R.id.yScaleLeft);
        yScaleRight = view.findViewById(R.id.yScaleRight);
        yScaleMiddle = view.findViewById(R.id.yScaleMiddle);

        // Init spinner
        xDirAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.geometry_msg_twist_dir, android.R.layout.simple_spinner_dropdown_item);
        xAxisAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.geometry_msg_twist_axis, android.R.layout.simple_spinner_dropdown_item);
        yDirAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.geometry_msg_twist_dir, android.R.layout.simple_spinner_dropdown_item);
        yAxisAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.geometry_msg_twist_axis, android.R.layout.simple_spinner_dropdown_item);

        xDirSpinner.setAdapter(xDirAdapter);
        xAxisSpinner.setAdapter(xAxisAdapter);
        yDirSpinner.setAdapter(yDirAdapter);
        yAxisSpinner.setAdapter(yAxisAdapter);
    }

    @Override
    public void bind(WidgetJoystickEntity entity) {
        topicNameText.setText(entity.subPubNoteEntity.topic);

        String[] xAxisMapping = entity.xAxisMapping.split("/");

        xDirSpinner.setSelection(xDirAdapter.getPosition(xAxisMapping[0]));
        xAxisSpinner.setSelection(xAxisAdapter.getPosition(xAxisMapping[1]));

        String[] yAxisMapping = entity.yAxisMapping.split("/");
        yDirSpinner.setSelection(yDirAdapter.getPosition(yAxisMapping[0]));
        yAxisSpinner.setSelection(yAxisAdapter.getPosition(yAxisMapping[1]));

        xScaleLeft.setText(String.valueOf(entity.xScaleLeft));
        xScaleRight.setText(String.valueOf(entity.xScaleRight));
        yScaleLeft.setText(String.valueOf(entity.yScaleLeft));
        yScaleRight.setText(String.valueOf(entity.yScaleRight));
    }

    @Override
    public void updateEntity() {
        entity.subPubNoteEntity.messageType = geometry_msgs.Twist._TYPE;
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();
        entity.xAxisMapping = xDirSpinner.getSelectedItem() + "/" + xAxisSpinner.getSelectedItem();
        entity.yAxisMapping = yDirSpinner.getSelectedItem() + "/" + yAxisSpinner.getSelectedItem();
        entity.xScaleLeft = Float.parseFloat(xScaleLeft.getText().toString());
        entity.xScaleRight = Float.parseFloat(xScaleRight.getText().toString());
        entity.yScaleLeft = Float.parseFloat(yScaleLeft.getText().toString());
        entity.yScaleRight = Float.parseFloat(yScaleRight.getText().toString());
    }
}
