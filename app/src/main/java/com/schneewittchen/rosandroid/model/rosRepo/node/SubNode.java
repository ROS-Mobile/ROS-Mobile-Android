package com.schneewittchen.rosandroid.model.rosRepo.node;

import org.ros.internal.message.Message;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 16.09.20
 * @updated on
 * @modified by
 */
class SubNode extends AbstractNode {

    List<NodeListener> listeners = new ArrayList<>();


    @Override
    public void onStart(ConnectedNode parentNode) {
        Subscriber<? extends Message> subscriber = parentNode.newSubscriber(topic.getName(), topic.getType());

        subscriber.addMessageListener(data -> {
            for (NodeListener listener: listeners) {
                listener.onData(data);
            }
        });
    }

    public void addListener(NodeListener listener) {
        this.listeners.add(listener);
    }


    public interface NodeListener  {
        void onData(Object data);
    }
}
