package com.schneewittchen.rosandroid.widgets.gps2ros;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

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

    public double latitude;
    public double longitude;
    public double altitude;
    public String type;

    public Gps2RosData(double latitude, double longitude, double altitude, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.type = type;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {

        sensor_msgs.NavSatFix message = (NavSatFix) publisher.newMessage();

        message.getHeader().setFrameId(type);
        message.setLatitude(latitude);
        message.setLongitude(longitude);
        message.setAltitude(altitude);

        return message;
    }
}
