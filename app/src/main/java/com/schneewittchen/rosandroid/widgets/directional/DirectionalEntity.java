package com.schneewittchen.rosandroid.widgets.directional;

import com.schneewittchen.rosandroid.model.entities.widgets.PublisherWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import geometry_msgs.Twist;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.1
 * @created on 31.01.20
 * @updated on 10.05.20
 * @modified by Nico Studt
 */
public class DirectionalEntity extends PublisherWidgetEntity {

    public String text;
    public int rotation;
    public String axis;
    public String direction;
    public String sense;
    public double speed;

    public DirectionalEntity() {
        this.width = 2;
        this.height = 2;
        this.topic = new Topic("cmd_vel", Twist._TYPE);
        this.publishRate = 20f;
        this.text = "Move on";
        this.rotation = 0;
        this.immediatePublish = true;
        this.axis = "X";
        this.direction = "Linear";
        this.sense = "Positive";
        this.speed = 0.3;
    }

}
