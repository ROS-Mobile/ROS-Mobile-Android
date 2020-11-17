package com.rosmobile.rosandroid.widgets.joystick;

import com.rosmobile.rosandroid.model.repositories.rosRepo.message.Topic;
import com.rosmobile.rosandroid.model.entities.PublisherEntity;

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
public class JoystickEntity extends PublisherEntity {

    public String xAxisMapping;
    public String yAxisMapping;
    public float xScaleLeft;
    public float xScaleRight;
    public float yScaleLeft;
    public float yScaleRight;


    public JoystickEntity() {
        this.width = 4;
        this.height = 4;
        this.topic = new Topic("cmd_vel", Twist._TYPE);
        this.immediatePublish = false;
        this.publishRate = 20f;
        this.xAxisMapping = "Angular/Z";
        this.yAxisMapping = "Linear/X";
        this.xScaleLeft = 1;
        this.xScaleRight = -1;
        this.yScaleLeft = -1;
        this.yScaleRight = 1;
    }

}
