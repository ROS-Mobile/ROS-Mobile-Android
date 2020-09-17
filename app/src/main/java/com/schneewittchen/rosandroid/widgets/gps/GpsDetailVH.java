package com.schneewittchen.rosandroid.widgets.gps;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailSubscriberVH;
import com.schneewittchen.rosandroid.widgets.base.BaseDetailViewHolder;
import com.schneewittchen.rosandroid.widgets.base.DetailListener;
import com.schneewittchen.rosandroid.widgets.camera.WidgetCameraEntity;
import com.schneewittchen.rosandroid.widgets.gridmap.WidgetGridMapEntity;

import java.util.Arrays;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 17.09.20
 * @modified by Nils Rottmann
 */

public class GpsDetailVH extends BaseDetailSubscriberVH<WidgetGpsEntity> {

    public GpsDetailVH(@NonNull View view, DetailListener updateListener) {
        super(view, updateListener);
        this.setTopicTypeList(Arrays.asList(view.getResources().getStringArray(R.array.gps_msg_types)));
    }

}
