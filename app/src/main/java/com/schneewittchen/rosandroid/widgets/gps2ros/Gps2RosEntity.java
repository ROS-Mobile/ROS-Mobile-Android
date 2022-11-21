package com.schneewittchen.rosandroid.widgets.gps2ros;

import com.schneewittchen.rosandroid.model.entities.widgets.PublisherWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import sensor_msgs.NavSatFix;
import std_msgs.Bool;

/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */

public class Gps2RosEntity extends PublisherWidgetEntity {

    public String text;
    public int rotation;
    public boolean buttonPressed;

    public Gps2RosEntity() {
        this.width = 4;
        this.height = 4;
        this.topic = new Topic("gps_android", NavSatFix._TYPE);
        this.immediatePublish = true;
        //this.publishRate = 20f;
        this.text = "Publish GPS 2 ROS";
        this.rotation = 0;
        this.buttonPressed = false;
    }
}
