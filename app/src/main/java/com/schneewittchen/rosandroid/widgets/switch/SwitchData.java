package com.schneewittchen.rosandroid.widgets.button;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;

import std_msgs.Bool;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.05.2022
 */
public class SwitchData extends BaseData {

    public boolean pressed;

    public  SwitchData(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {
        std_msgs.Bool message = (Bool) publisher.newMessage();
        message.setData(pressed);
        return message;
    }
}
