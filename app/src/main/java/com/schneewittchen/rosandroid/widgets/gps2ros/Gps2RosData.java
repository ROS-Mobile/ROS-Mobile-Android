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

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {

        sensor_msgs.NavSatFix message = (NavSatFix) publisher.newMessage();

        /*JoystickEntity joyWidget = (JoystickEntity) widget;

        float xAxisValue = joyWidget.xScaleLeft  + (joyWidget.xScaleRight - joyWidget.xScaleLeft) * ((x+1) /2f);
        float yAxisValue = joyWidget.yScaleLeft  + (joyWidget.yScaleRight - joyWidget.yScaleLeft) * ((y+1) /2f);

        geometry_msgs.Twist message = (Twist) publisher.newMessage();

        for (int i = 0; i < 2; i++) {
            String[] splitMapping = (i == 0? joyWidget.xAxisMapping : joyWidget.yAxisMapping).split("/");
            float value = i == 0? xAxisValue : yAxisValue;

            Vector3 dirVector;
            if (splitMapping[0].equals("Linear")) {
                dirVector = message.getLinear();
            } else {
                dirVector = message.getAngular();
            }

            switch (splitMapping[1]) {
                case "X":
                    dirVector.setX(value);
                    break;
                case "Y":
                    dirVector.setY(value);
                    break;
                case "Z":
                    dirVector.setZ(value);
                    break;
            }
        }*/

        return message;
    }
}
