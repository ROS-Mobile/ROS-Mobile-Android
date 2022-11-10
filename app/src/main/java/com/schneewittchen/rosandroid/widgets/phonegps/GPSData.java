package com.schneewittchen.rosandroid.widgets.phonegps;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import org.ros.internal.message.Message;
import org.ros.node.topic.Publisher;

import geometry_msgs.Twist;
import geometry_msgs.Vector3;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.20
 * @updated on 17.03.20
 * @modified by
 */
public class GPSData extends BaseData {

    public static final String TAG = GPSData.class.getSimpleName();
    public float x;
    public float y;


    public GPSData(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Message toRosMessage(Publisher<Message> publisher, BaseEntity widget) {
        PhoneGpsEntity joyWidget = (PhoneGpsEntity) widget;

        float xAxisValue = joyWidget.xScaleLeft + (joyWidget.xScaleRight - joyWidget.xScaleLeft) * ((x + 1) / 2f);
        float yAxisValue = joyWidget.yScaleLeft + (joyWidget.yScaleRight - joyWidget.yScaleLeft) * ((y + 1) / 2f);

        Twist message = (Twist) publisher.newMessage();

        for (int i = 0; i < 2; i++) {
            String[] splitMapping = (i == 0 ? joyWidget.xAxisMapping : joyWidget.yAxisMapping).split("/");
            float value = i == 0 ? xAxisValue : yAxisValue;

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
        }

        return message;
    }
}
