package com.schneewittchen.rosandroid.widgets.gltest;

import android.view.View;

import androidx.annotation.NonNull;

import com.schneewittchen.rosandroid.ui.fragments.details.WidgetChangeListener;
import com.schneewittchen.rosandroid.ui.views.BaseDetailSubscriberVH;

import java.util.Arrays;
import java.util.List;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 07.03.2021
 */
public class GLTestDetailVH extends BaseDetailSubscriberVH<GLTestEntity> {

    public static final String TAG = GLTestDetailVH.class.getSimpleName();


    public GLTestDetailVH(@NonNull View view, WidgetChangeListener updateListener) {
        super(view, updateListener);
    }


    @Override
    protected void initView(View parentView) {

    }

    @Override
    protected void bindEntity(GLTestEntity entity) {

    }

    @Override
    protected void updateEntity() {

    }

    @Override
    public List<String> getTopicTypes() {
        return Arrays.asList(Image._TYPE);
    }

}
