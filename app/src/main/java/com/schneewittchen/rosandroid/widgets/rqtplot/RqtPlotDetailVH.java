package com.schneewittchen.rosandroid.widgets.rqtplot;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberWidgetViewHolder;
import com.schneewittchen.rosandroid.utility.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 29.05.21
 */
public class RqtPlotDetailVH extends SubscriberWidgetViewHolder implements TextView.OnEditorActionListener {

    private static final String TAG = RqtPlotDetailVH.class.getSimpleName();

    private TextInputEditText fieldEditText;


    @Override
    public void initView(View view) {
        fieldEditText = view.findViewById(R.id.fieldEditText);
        fieldEditText.setOnEditorActionListener(this);
    }

    @Override
    protected void bindEntity(BaseEntity entity) {
        RqtPlotEntity plotEntity = (RqtPlotEntity) entity;
        fieldEditText.setText(plotEntity.fieldPath);
    }

    @Override
    protected void updateEntity(BaseEntity entity) {
        RqtPlotEntity plotEntity = (RqtPlotEntity) entity;

        if (fieldEditText.getText() == null)
            return;

        plotEntity.fieldPath = fieldEditText.getText().toString().trim();

    }

    @Override
    public List<String> getTopicTypes() {
        return new ArrayList<>();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId){
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_PREVIOUS:
                Utils.hideSoftKeyboard(v);
                v.clearFocus();
                this.forceWidgetUpdate();
                return true;
        }

        return false;
    }
}
