package com.schneewittchen.rosandroid.model.rosRepo;

import org.ros.internal.message.Message;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 16.09.20
 * @updated on
 * @modified by
 */
public abstract class WidgetData {

    public abstract Message toMessage();
    public abstract WidgetData fromMessage(Message message);
}
