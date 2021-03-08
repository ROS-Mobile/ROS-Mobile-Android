package com.schneewittchen.rosandroid.widgets.gltest;

import com.schneewittchen.rosandroid.model.entities.SubscriberEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import geometry_msgs.PointStamped;
import geometry_msgs.PoseStamped;
import geometry_msgs.PoseWithCovarianceStamped;
import sensor_msgs.Image;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 07.03.2021
 */
public class GLTestEntity extends SubscriberEntity {


    public GLTestEntity() {
        this.width = 8;
        this.height = 8;
        this.topic = new Topic("initialpose", PoseWithCovarianceStamped._TYPE);
    }
}

