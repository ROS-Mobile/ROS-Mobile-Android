package com.schneewittchen.rosandroid.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.ui.general.DataListener;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 02.10.20
 * @updated on
 * @modified by
 */
public abstract class PublisherView extends BaseView {

    DataListener dataListener;


    public PublisherView(Context context) {
        super(context);
    }

    public PublisherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PublisherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void publishViewData(BaseData data) {
        if(dataListener == null) {
            return;
        }


        data.setTopic(widgetEntity.topic);
        dataListener.onNewWidgetData(data);
    }

    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    public void removeDataListener() {
        this.dataListener = null;
    }
}
