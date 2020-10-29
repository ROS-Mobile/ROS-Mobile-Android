package com.schneewittchen.rosandroid.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import org.ros.internal.message.Message;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 02.10.20
 * @updated on
 * @modified by
 */
public abstract class SubscriberView extends BaseView {

    public SubscriberView(Context context) {
        super(context);
    }

    public SubscriberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SubscriberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public abstract void onNewMessage(Message message);
}
