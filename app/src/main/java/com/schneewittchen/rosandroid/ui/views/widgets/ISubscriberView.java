package com.schneewittchen.rosandroid.ui.views.widgets;

import org.ros.internal.message.Message;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 */
public interface ISubscriberView {

     void onNewMessage(Message message);
}
