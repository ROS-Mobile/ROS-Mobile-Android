package com.schneewittchen.rosandroid.model.rosRepo.message;

import org.ros.internal.message.Message;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.09.20
 * @updated on
 * @modified by
 */
public class RosMessage {

    private Topic topic;
    private Message message;


    public RosMessage(Topic topic, Message message) {
        this.topic = topic;
        this.message = message;
    }


    public Topic getTopic() {
        return this.topic;
    }

    public Message getMessage() {
        return this.message;
    }
}
