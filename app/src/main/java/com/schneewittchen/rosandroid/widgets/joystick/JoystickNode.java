package com.schneewittchen.rosandroid.widgets.joystick;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import java.util.Timer;
import java.util.TimerTask;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 13.03.20
 * @updated on 15.04.20
 * @modified by
 */
public class JoystickNode extends BaseNode {

    private float lastX, lastY;
    private geometry_msgs.Twist currentTwistMessage;


    public JoystickNode(BaseEntity widget) {
        super(widget);
    }


    @Override
    public void onStart(ConnectedNode connectedNode) {
        Publisher<geometry_msgs.Twist> publisher = connectedNode.newPublisher(widget.publisher.topic,
                                                                        widget.publisher.messageType);
        currentTwistMessage = publisher.newMessage();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTwistMessage.getLinear().setX(lastX);
                currentTwistMessage.getLinear().setY(lastY);
                publisher.publish(currentTwistMessage);
            }
        }, 100, 100);
    }


    @Override
    public void onNewData(BaseData data) {
        JoystickData joystickData = (JoystickData) data;
        lastX = joystickData.x;
        lastY = joystickData.y;
    }
}
