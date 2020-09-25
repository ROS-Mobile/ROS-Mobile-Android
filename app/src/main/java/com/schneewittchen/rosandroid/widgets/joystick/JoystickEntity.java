package com.schneewittchen.rosandroid.widgets.joystick;


import com.schneewittchen.rosandroid.model.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.widgets.test.PublisherWidget;

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
public class JoystickEntity extends PublisherWidget {

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
        this.publishRate = 10f;
        this.xAxisMapping = "Angular/Z";
        this.yAxisMapping = "Linear/X";
        this.xScaleLeft = -1;
        this.xScaleRight = 1;
        this.yScaleLeft = -1;
        this.yScaleRight = 1;
    }


    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }

        JoystickEntity other = (JoystickEntity) o;

        return this.xAxisMapping.equals(other.xAxisMapping)
                && this.yAxisMapping.equals(other.yAxisMapping)
                && this.xScaleLeft == other.xScaleLeft
                && this.xScaleRight == other.xScaleRight
                && this.yScaleLeft == other.yScaleLeft
                && this.yScaleRight == other.yScaleRight;
    }

}
