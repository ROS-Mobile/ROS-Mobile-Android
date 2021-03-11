package com.schneewittchen.rosandroid.widgets.tf;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberWidgetView;

import org.ros.internal.message.Message;

import tf2_msgs.TFMessage;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class TFView extends SubscriberWidgetView {

    public static final String TAG = TFView.class.getSimpleName();


    public TFView(Context context) {
        super(context);
    }

    public TFView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNewMessage(Message message) {
        TFMessage tf = (TFMessage) message;
    }


}
