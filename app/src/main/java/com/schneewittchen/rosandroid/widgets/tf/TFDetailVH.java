package com.schneewittchen.rosandroid.widgets.tf;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;
import com.schneewittchen.rosandroid.ui.views.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.utility.Utils;

import java.util.ArrayList;
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

public class TFDetailVH extends BaseDetailSubscriberVH<TFEntity> {

    public TFDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }

    @Override
    public List<String> getTopicTypes() {
        return new ArrayList<>();
    }

    @Override
    public void initView(View view) {

    }

    @Override
    protected void bindEntity(TFEntity entity) {
    }

    @Override
    public void updateEntity() {
    }
}
