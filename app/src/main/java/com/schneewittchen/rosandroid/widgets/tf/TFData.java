package com.schneewittchen.rosandroid.widgets.tf;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;

import std_msgs.Bool;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class TFData extends BaseData {

    public boolean pressed;

    public TFData(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {
        Bool message = (Bool) publisher.newMessage();
        message.setData(pressed);
        return message;
    }
}
