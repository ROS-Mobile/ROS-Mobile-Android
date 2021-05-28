package com.schneewittchen.rosandroid.model.repositories.rosRepo.node;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.RosData;

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

    private final NodeListener listener;


    public SubNode(NodeListener listener) {
        this.listener = listener;
    }


    @Override
    public void onStart(ConnectedNode parentNode) {
        super.onStart(parentNode);

        try {
            if (this.widget != null) {
                this.widget.validMessage = true;
            }

            Subscriber<? extends Message> subscriber = parentNode.newSubscriber(topic.name, topic.type);

            subscriber.addMessageListener(data -> {
                listener.onNewMessage(new RosData(topic, data));
            });

        } catch(Exception e) {
            if (this.widget != null) {
                this.widget.validMessage = false;
            }
            e.printStackTrace();
        }

    }

    public interface NodeListener  {
        void onNewMessage(RosData message);
    }
}
