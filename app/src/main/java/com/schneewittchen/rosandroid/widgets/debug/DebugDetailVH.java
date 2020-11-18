package com.schneewittchen.rosandroid.widgets.debug;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;
import com.schneewittchen.rosandroid.utility.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class DebugDetailVH extends BaseDetailSubscriberVH<DebugEntity> {

    protected EditText messageNumberEdittext;


    public DebugDetailVH(@NonNull View view, WidgetChangeListener changeListener) {
        super(view, changeListener);
    }


    @Override
    protected void initView(View parentView) {
        messageNumberEdittext = parentView.findViewById(R.id.messageNumberEdittext);
        messageNumberEdittext.setOnEditorActionListener(this);
    }

    @Override
    protected void bindEntity(DebugEntity entity) {
        messageNumberEdittext.setText(String.valueOf(entity.numberMessages));
    }

    @Override
    protected void updateEntity() {
        entity.numberMessages = Integer.parseInt(messageNumberEdittext.getText().toString());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId){
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_NEXT:
            case EditorInfo.IME_ACTION_PREVIOUS:
                Utils.hideSoftKeyboard(itemView);
                itemView.requestFocus();
                return true;
        }

        return false;
    }

    @Override
    public List<String> getTopicTypes() {
        return new ArrayList<>();
    }

}