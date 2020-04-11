package com.schneewittchen.rosandroid.widgets.joystick;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.20
 * @updated on 17.03.20
 * @modified by
 */
public class JoystickData extends BaseData {

    public float x;
    public float y;


    public JoystickData(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
