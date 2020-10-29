package com.schneewittchen.rosandroid.widgets.gps;

import android.view.View;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;

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
public class GpsDetailVH extends BaseDetailSubscriberVH<GpsEntity> {

    public GpsDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
        this.setTopicTypeList(Arrays.asList(view.getResources().getStringArray(R.array.gps_msg_types)));
    }

}
