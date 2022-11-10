package com.schneewittchen.rosandroid.widgets.phonegps;

import com.schneewittchen.rosandroid.model.entities.widgets.PublisherWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import sensor_msgs.NavSatFix;
import sensor_msgs.NavSatStatus;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.1
 * @created on 31.01.20
 * @updated on 10.05.20
 * @modified by Nico Studt
 */
public class PhoneGpsEntity extends PublisherWidgetEntity {

    public String xAxisMapping;
    public String yAxisMapping;
    public float xScaleLeft;
    public float xScaleRight;
    public float yScaleLeft;
    public float yScaleRight;
    public boolean rectangularLimits;

    public PhoneGpsEntity() {
        this.width = 4;
        this.height = 4;
        this.topic = new Topic("android/gps", NavSatFix._TYPE);
        this.immediatePublish = false;
        this.publishRate = 20f;
    }

}
