package com.schneewittchen.rosandroid.widgets.gps2ros;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.widgets.joystick.JoystickEntity;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;

import sensor_msgs.NavSatFix;


/**
 * TODO: Description
 *
 * @author Gennaro Raiola
 * @version 0.0.1
 * @created on 19.11.22
 */
public class Gps2RosData extends BaseData {

    double latitude;
    double longitude;

    public Gps2RosData(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {

        Gps2RosEntity gps2rosWidget = (Gps2RosEntity) widget;

        sensor_msgs.NavSatFix message = (NavSatFix) publisher.newMessage();

        message.setLatitude(latitude);
        message.setLongitude(longitude);

        return message;
    }
}
