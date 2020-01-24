package com.schneewittchen.rosandroidlib.widgets.model;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.01.20
 * @updated on 21.01.20
 * @modified by
 */
public class JoystickWidget extends Widget {

    public String publishTopic = "/cmd_vel";

    @Override
    public String getType() {
        return "_joystick";
    }
}
