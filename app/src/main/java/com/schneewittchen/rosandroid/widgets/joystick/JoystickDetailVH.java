package com.schneewittchen.rosandroid.widgets.joystick;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;

import java.util.Locale;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 13.02.20
 * @updated on 20.05.20
 * @modified by Nico Studt
 */
public class JoystickDetailVH extends BaseDetailViewHolder<WidgetJoystickEntity> {

    private EditText topicNameText;

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

        xScaleLeft.setText(String.format(Locale.US, "%.2f", entity.xScaleLeft));
        xScaleRight.setText(String.format(Locale.US, "%.2f", entity.xScaleRight));
        xScaleMiddle.setText(String.format(Locale.US, "%.2f", (entity.xScaleRight + entity.xScaleLeft) / 2));
        yScaleLeft.setText(String.format(Locale.US, "%.2f", entity.yScaleLeft));
        yScaleRight.setText(String.format(Locale.US, "%.2f", entity.yScaleRight));
        yScaleMiddle.setText(String.format(Locale.US, "%.2f", (entity.yScaleRight + entity.yScaleLeft) / 2));
    }

    @Override
    public void updateEntity() {
        entity.subPubNoteEntity.messageType = geometry_msgs.Twist._TYPE;
        entity.subPubNoteEntity.topic = topicNameText.getText().toString();
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
