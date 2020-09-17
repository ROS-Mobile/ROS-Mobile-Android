package com.schneewittchen.rosandroid.widgets.gridmap;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailSubscriberVH;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;

import java.util.Arrays;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 13.02.20
 * @updated on 13.05.20
 * @modified by Nico Studt
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */
public class GridMapDetailVH extends BaseDetailSubscriberVH<WidgetGridMapEntity> {

    public GridMapDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
        this.setTopicTypeList(Arrays.asList(view.getResources().getStringArray(R.array.gridmap_msg_types)));
    }

}
