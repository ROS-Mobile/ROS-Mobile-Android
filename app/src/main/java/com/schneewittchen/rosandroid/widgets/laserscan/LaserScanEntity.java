package com.schneewittchen.rosandroid.widgets.laserscan;

import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import sensor_msgs.LaserScan;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.05.21
 */
public class LaserScanEntity extends SubscriberLayerEntity {

    public LaserScanEntity() {
        this.topic = new Topic("/scan", LaserScan._TYPE);
    }
}
