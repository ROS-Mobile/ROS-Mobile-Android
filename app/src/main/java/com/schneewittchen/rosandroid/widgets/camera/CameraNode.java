package com.schneewittchen.rosandroid.widgets.camera;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import sensor_msgs.Image;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 27.04.20
 * @updated on
 * @modified by
 */
public class CameraNode extends BaseNode {

    public CameraNode(BaseEntity widget) {
        super(widget);
    }


    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<Image> subscriber = connectedNode.newSubscriber(
                widget.subscriber.topic, widget.subscriber.messageType
        );
        subscriber.addMessageListener(new MessageListener<Image>() {
            @Override
            public void onNewMessage(sensor_msgs.Image image) {
                int height = image.getHeight();
                int width = image.getWidth();
                String encoding = image.getEncoding();
                byte bigEndian = image.getIsBigendian();
                int step = image.getStep();
                byte[] dataArray = image.getData().array();

                CameraData data = new CameraData(height, width, encoding, bigEndian, step, dataArray);
                data.setId(widget.id);
                listener.onNewData(data);
            }
        });
    }

    @Override
    public void onNewData(BaseData data) {
    }
}