package com.schneewittchen.rosandroid.ui.views.widgets;

import android.content.Context;
import android.util.AttributeSet;

import org.ros.internal.message.Message;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 */
public abstract class SubscriberWidgetView extends WidgetView implements ISubscriberView{


    public SubscriberWidgetView(Context context) {
        super(context);
    }

    public SubscriberWidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onNewMessage(Message message) {
        return;
    }

}
