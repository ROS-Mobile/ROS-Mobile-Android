package com.schneewittchen.rosandroid.widgets.logger;

import com.schneewittchen.rosandroid.model.entities.SubscriberEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import std_msgs.String;

/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 */

public class LoggerEntity extends SubscriberEntity {

    public java.lang.String text;
    public int rotation;

    public LoggerEntity() {
        this.width=3;
        this.height=1;
        this.topic = new Topic("log", String._TYPE);
        this.text = "A logger";
        this.rotation = 0;
    }

}
