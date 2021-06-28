package com.schneewittchen.rosandroid.model.repositories.rosRepo.node;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.PublisherLayerEntity;

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
public class PubNode extends AbstractNode {

    private Publisher<Message> publisher;
    private BaseData lastData;
    private Timer pubTimer;
    private long pubPeriod = 100L;
    private boolean immediatePublish = true;


    @Override
    public void onStart(ConnectedNode parentNode) {
        publisher = parentNode.newPublisher(topic.name, topic.type);

        this.createAndStartSchedule();
    }

    /**
     * Call this method to publish a ROS message.
     *
     * @param data Data to publish
     */
    public void setData(BaseData data) {
        this.lastData = data;

        if (immediatePublish) {
            publish();
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

    private void createAndStartSchedule() {
        if (pubTimer != null) {
            pubTimer.cancel();
        }

        if (immediatePublish) {
            return;
        }

        pubTimer = new Timer();
        pubTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                publish();
            }
        }, pubPeriod, pubPeriod);
    }

    private void publish() {
        if (publisher == null) {
            return;
        }
        if (lastData == null) {
            return;
        }

        Message message = lastData.toRosMessage(publisher, widget);
        publisher.publish(message);
    }

    @Override
    public void setWidget(BaseEntity widget) {
        super.setWidget(widget);

        if (!(widget instanceof PublisherLayerEntity)) {
            return;
        }

        PublisherLayerEntity pubEntity = (PublisherLayerEntity) widget;

        this.setImmediatePublish(pubEntity.immediatePublish);
        this.setFrequency(pubEntity.publishRate);
    }
}
