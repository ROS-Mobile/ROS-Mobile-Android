package com.schneewittchen.rosandroid.widgets.button;

import android.util.Log;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.joystick.WidgetJoystickEntity;

import org.apache.commons.lang.ObjectUtils;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import java.util.Timer;
import java.util.TimerTask;

public class ButtonNode extends BaseNode<WidgetButtonEntity> {

    private Publisher<std_msgs.Bool> publisher;
    private std_msgs.Bool message;

    private ButtonState currentState, newState;

    @Override
    public void onStart(ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher(widget.subPubNoteEntity.topic, widget.subPubNoteEntity.messageType);
        currentState = newState = ButtonState.Idle;


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                publish();
            }
        }, 100, 100);
    }

    @Override
    public void onNewData(BaseData data) {
        ButtonData buttonData = (ButtonData) data;
        newState = buttonData.state;
    }

    private void publish() {
        if (newState != currentState) {
            currentState = newState;
            message = publisher.newMessage();

            switch (currentState){
                case Pressed:
                    message.setData(true); break;
                default:
                    message.setData(false); break;
            }

            publisher.publish(message);
        }
    }
}
