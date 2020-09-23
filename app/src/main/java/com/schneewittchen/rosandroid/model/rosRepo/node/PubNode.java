package com.schneewittchen.rosandroid.model.rosRepo.node;

import org.ros.internal.message.Message;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ROS Node for publishing Messages on a specific topic.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 16.09.20
 * @updated on
 * @modified by
 */
class PubNode extends AbstractNode {

    private Publisher<Message> publisher;
    private Message lastMessage;
    private long pubPeriod = 100L;
    private boolean immediatePublish = true;


    @Override
    public void onStart(ConnectedNode parentNode) {
        publisher = parentNode.newPublisher(topic.getName(), topic.getType());

        // Start timer for sequential publishing
        if (!immediatePublish) {
            this.createAndStartSchedule();
        }
    }


    /**
     * Set publishing frequency.
     * E.g. With a value of 10 the node will publish 10 times per second.
     *
     * @param hz Frequency in hertz
     */
    public void setFrequency(float hz) {
        this.pubPeriod = (long) (1000 / hz);
    }

    /**
     * Enable or disable immediate publishing.
     * In the enabled state the node will create und send a ros message as soon as
     * @link #setData(Object) is called.
     *
     * @param flag Enable immediate publishing
     */
    public void setImmediatePublish(boolean flag) {
        this.immediatePublish = flag;
    }

    /**
     * Call this method to publish a ROS message.
     *
     * @param message Message to publish
     */
    public void setData(Message message) {
        this.lastMessage = message;

        if (immediatePublish) {
            publish();
        }
    }

    private void createAndStartSchedule() {
        Timer pubTimer = new Timer();

        pubTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                publish();
            }
        }, pubPeriod, pubPeriod);
    }

    private void publish() {
        if (lastMessage == null || !publisher.hasSubscribers()) {
            return;
        }

        publisher.publish(lastMessage);
    }
}
