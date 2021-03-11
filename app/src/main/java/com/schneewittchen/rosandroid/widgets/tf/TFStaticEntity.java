package com.schneewittchen.rosandroid.widgets.tf;

import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
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
public class TFStaticEntity extends SubscriberLayerEntity {


    public TFStaticEntity() {
        this.topic = new Topic("tf_static", TFMessage._TYPE);
    }

}