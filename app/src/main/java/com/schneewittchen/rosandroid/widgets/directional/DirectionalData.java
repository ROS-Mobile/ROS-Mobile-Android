package com.schneewittchen.rosandroid.widgets.directional;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickEntity;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;

import geometry_msgs.Twist;
import geometry_msgs.Vector3;

import android.util.Log;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.20
 * @updated on 17.03.20
 * @modified by
 */
public class DirectionalData extends BaseData {

    public static final String TAG = DirectionalData.class.getSimpleName();

    private double getSpeed(String sense, double value) {
        if (sense.equals("Positive") && value >= 0) {
            return value;
        } else if (sense.equals("Positive") && value < 0) {
            return -value;
        } else if (value < 0) {
            return value;
        } else {
            return -value;
        }
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {

        DirectionalEntity dirWidget = (DirectionalEntity) widget;

        Log.d("name", dirWidget.name);
        Log.d("topic", dirWidget.topic.name);
        Log.d("position", Integer.toString(dirWidget.posX));
        Log.d("axis", dirWidget.axis);
        Log.d("dir", dirWidget.direction);
        Log.d("sense", dirWidget.sense);

        geometry_msgs.Twist message = (Twist) publisher.newMessage();
        Vector3 dirVector;

        double speed = 0.3;
        if (dirWidget.direction.equals(("Linear"))) {
            dirVector = message.getLinear();
        } else {
            dirVector = message.getAngular();
        }
        if (dirWidget.speed > 0) {
            speed = dirWidget.speed;
        }
        String sense = dirWidget.sense;

        switch (dirWidget.axis) {
            case "X":
                dirVector.setX(getSpeed(sense, speed));
                break;
            case "Y":
                dirVector.setY(getSpeed(sense, speed));
                break;
            case "Z":
                dirVector.setZ(getSpeed(sense, speed));
                break;
        }

        return message;
    }
}
