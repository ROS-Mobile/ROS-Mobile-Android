package com.schneewittchen.rosandroid.model.repositories.rosRepo.node;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;


public abstract class BaseData {

    protected Topic topic;


    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget){
        return null;
    }
}
