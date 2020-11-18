package com.schneewittchen.rosandroid.model.repositories.rosRepo.message;

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
public class RosData {

    private final Topic topic;
    private final Message message;


    public RosData(Topic topic, Message message) {
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
