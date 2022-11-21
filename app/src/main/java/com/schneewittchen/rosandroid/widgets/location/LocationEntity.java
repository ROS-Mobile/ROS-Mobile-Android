package com.schneewittchen.rosandroid.widgets.location;

import com.schneewittchen.rosandroid.model.entities.widgets.PublisherWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import sensor_msgs.NavSatFix;

/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */

public class LocationEntity extends PublisherWidgetEntity {

    public String text;
    public int rotation;
    public boolean buttonPressed;

    public LocationEntity() {
        this.width = 4;
        this.height = 4;
        this.topic = new Topic("location", NavSatFix._TYPE);
        this.immediatePublish = true;
        //this.publishRate = 20f;
        this.text = "Publish phone's location to ROS";
        this.rotation = 0;
        this.buttonPressed = false;
    }
}
