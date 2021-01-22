package com.schneewittchen.rosandroid.widgets.logger;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;
import com.schneewittchen.rosandroid.utility.Utils;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class LoggerDetailVH extends BaseDetailSubscriberVH<LoggerEntity> {

    private Spinner labelTextRotationSpinner;
    private ArrayAdapter<CharSequence> rotationAdapter;

    public LoggerDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public void initView(View view) {
        labelTextRotationSpinner = view.findViewById(R.id.loggerTextRotation);
        rotationAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.rotation, android.R.layout.simple_spinner_dropdown_item);
        labelTextRotationSpinner.setAdapter(rotationAdapter);
    }

    @Override
    protected void bindEntity(LoggerEntity entity) {
        labelTextRotationSpinner.setSelection(rotationAdapter.getPosition(Utils.numberToDegrees(entity.rotation)));
    }


    @Override
    protected void updateEntity() {
        entity.rotation = Utils.degreesToNumber(labelTextRotationSpinner.getSelectedItem().toString());
    }

    @Override
    public List<String> getTopicTypes() {
        return null;
    }
}
