package com.schneewittchen.rosandroid.widgets.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.03.20
 * @updated on 15.03.20
 * @modified by
 */
public class BaseView extends View implements Interactable {

    DataListener dataListener;
    long dataId;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void informDataChange(WidgetData data) {
        if(dataListener != null) {
            data.setId(dataId);
            dataListener.onNewData(data);
        }
    }

    @Override
    public void setData(WidgetData data) {
        // Default data set, but nothing to see here!
    }

    @Override
    public void setDataListener(DataListener listener) {
        this.dataListener = listener;
    }

    @Override
    public void removeDataListener() {
        this.dataListener = null;
    }

    @Override
    public void setDataId(long id) {
        this.dataId = id;
    }

    @Override
    public long getDataId() {
        return this.dataId;
    }

}
