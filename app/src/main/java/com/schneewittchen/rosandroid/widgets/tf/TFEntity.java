package com.schneewittchen.rosandroid.widgets.tf;

import com.schneewittchen.rosandroid.model.entities.SubscriberEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import tf2_msgs.TFMessage;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 07.03.21
 * @updated on
 * @modified by
 */
public class TFEntity extends SubscriberEntity {


    public TFEntity() {
        this.topic = new Topic("tf", TFMessage._TYPE);
    }

}