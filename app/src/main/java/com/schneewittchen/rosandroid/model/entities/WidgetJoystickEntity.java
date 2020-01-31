package com.schneewittchen.rosandroid.model.entities;

import androidx.room.Entity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
@Entity(tableName = "Joystick_table")
public class WidgetJoystickEntity extends WidgetEntity {

    @Override
    public String getType() {
        return "Joystick";
    }
}
