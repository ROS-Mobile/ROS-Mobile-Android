package com.schneewittchen.rosandroid.widgets.debug;

import android.os.Message;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 17.08.20
 * @updated on
 * @modified by
 */

public class DebugNode extends BaseNode {

    private static final String TAG = DebugNode.class.getSimpleName();

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<Message> subscriber = connectedNode.newSubscriber(widget.subPubNoteEntity.topic, Subscriber.TOPIC_MESSAGE_TYPE_WILDCARD);
        subscriber.addMessageListener(msgType -> {
            DebugData data = new DebugData(msgType);
            data.setId(widget.id);
            listener.onNewData(data);
        });
    }

    @Override
    public void onNewData(BaseData data) {
    }
}