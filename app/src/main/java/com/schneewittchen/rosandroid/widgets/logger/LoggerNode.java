package com.schneewittchen.rosandroid.widgets.logger;

import android.util.Log;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import std_msgs.String;

public class LoggerNode extends BaseNode<WidgetLoggerEntity> {
    private  static  final java.lang.String TAG = LoggerNode.class.getSimpleName();

    private Subscriber<String> subscriber;

    @Override
    public void onStart(ConnectedNode connectedNode) {
        subscriber = connectedNode.newSubscriber(widget.subPubNoteEntity.topic, widget.subPubNoteEntity.messageType);
        subscriber.addMessageListener(data -> {
            listener.onNewData(new LoggerData(data.getData(), widget.id));
        });
    }

    @Override
    public void onNewData(BaseData data) {

    }
}
