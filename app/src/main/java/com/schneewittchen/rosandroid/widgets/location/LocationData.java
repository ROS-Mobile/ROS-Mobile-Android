package com.schneewittchen.rosandroid.widgets.location;

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

public class LocationData extends BaseData {

    public double latitude;
    public double longitude;
    public double altitude;
    public String provider;

    public LocationData(double latitude, double longitude, double altitude, String provider) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.provider = provider;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {

        sensor_msgs.NavSatFix message = (NavSatFix) publisher.newMessage();

        message.getHeader().setFrameId(provider);
        message.setLatitude(latitude);
        message.setLongitude(longitude);
        message.setAltitude(altitude);

        return message;
    }
}
