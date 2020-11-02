package com.schneewittchen.rosandroid.widgets.gridmap;

import android.view.View;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;

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
public class GridMapDetailVH extends BaseDetailSubscriberVH<GridMapEntity> {

    public GridMapDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
        this.setTopicTypeList(Arrays.asList(view.getResources().getStringArray(R.array.gridmap_msg_types)));
    }

    @Override
    protected void initView(View parentView) {

    }

    @Override
    protected void bindEntity(GridMapEntity entity) {

    }

    @Override
    protected void updateEntity() {

    }

}
