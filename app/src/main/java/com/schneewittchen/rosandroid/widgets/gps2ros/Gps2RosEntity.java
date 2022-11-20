package com.schneewittchen.rosandroid.widgets.gps2ros;

import com.schneewittchen.rosandroid.model.entities.widgets.PublisherWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import sensor_msgs.NavSatFix;

import androidx.core.app.ActivityCompat;

/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */

public class Gps2RosEntity extends PublisherWidgetEntity {

    public Gps2RosEntity() {
        this.width = 4;
        this.height = 4;
        this.topic = new Topic("gps_android", NavSatFix._TYPE);
        this.immediatePublish = false;
        this.publishRate = 20f;
    }
}
