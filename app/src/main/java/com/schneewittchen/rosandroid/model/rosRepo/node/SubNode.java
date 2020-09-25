package com.schneewittchen.rosandroid.model.rosRepo.node;

import org.ros.internal.message.Message;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 16.09.20
 */
public class SubNode extends AbstractNode {

    private NodeListener listener;


    public SubNode(NodeListener listener) {
        this.listener = listener;
    }


    @Override
    public void onStart(ConnectedNode parentNode) {
        super.onStart(parentNode);

        Subscriber<? extends Message> subscriber = parentNode.newSubscriber(topic.name, topic.type);

        subscriber.addMessageListener(data -> {
            listener.onData(data);
        });
    }

    public interface NodeListener  {
        void onData(Message data);
    }
}
