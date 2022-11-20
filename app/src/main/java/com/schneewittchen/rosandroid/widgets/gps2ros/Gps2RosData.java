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

    double latitude;
    double longitude;

    public Gps2RosData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {

        sensor_msgs.NavSatFix message = (NavSatFix) publisher.newMessage();

        message.setLatitude(latitude);
        message.setLongitude(longitude);

        return message;
    }
}
