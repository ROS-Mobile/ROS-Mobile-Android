package com.schneewittchen.rosandroid.widgets.joystick;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.WidgetNode;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 13.03.20
 * @updated on 13.03.20
 * @modified by
 */
public class JoystickNode extends WidgetNode implements JoystickView.UpdateListener{

    float lastX, lastY;
    private geometry_msgs.Twist currentTwistMessage;


    public JoystickNode(WidgetEntity widget) {
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
                publisher.publish(currentTwistMessage);
            }
        }, 100, 100);
    }

    @Override
    public void onUpdate(float x, float y) {
        currentTwistMessage.getLinear().setX(x);
        currentTwistMessage.getLinear().setY(y);
    }

    @Override
    public void onNewData(BaseData data) {

    }
}
