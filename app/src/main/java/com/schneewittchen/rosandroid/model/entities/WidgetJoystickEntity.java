package com.schneewittchen.rosandroid.model.entities;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class WidgetJoystickEntity extends WidgetEntity {

    public WidgetJoystickEntity() {
        this.setType(WidgetEntity.JOYSTICK);
    }


    @Override
    public String getName() {
        return "Joystick";
    }
}
