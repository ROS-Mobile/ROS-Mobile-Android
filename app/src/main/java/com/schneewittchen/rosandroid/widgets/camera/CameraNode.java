package com.schneewittchen.rosandroid.widgets.camera;

import android.nfc.Tag;
import android.util.Log;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 27.04.20
 * @updated on 13.05.20
 * @modified by Nico Studt
 */
public class CameraNode extends BaseNode {

    private static final String TAG = CameraNode.class.getSimpleName();
    private boolean compressed = true;

    @Override
    public void onStart(ConnectedNode connectedNode) {
        if (widget.subPubNoteEntity.messageType.equals("sensor_msgs/CompressedImage")) {
            Subscriber<CompressedImage> subscriber = connectedNode.newSubscriber(
                    widget.subPubNoteEntity.topic,
                    widget.subPubNoteEntity.messageType
            );
            subscriber.addMessageListener(image -> {
                CameraData data = new CameraData(image);
                data.setId(widget.id);
                listener.onNewData(data);
            });
        } else if (widget.subPubNoteEntity.messageType.equals("sensor_msgs/Image")) {
            Subscriber<Image> subscriber = connectedNode.newSubscriber(
                    widget.subPubNoteEntity.topic,
                    widget.subPubNoteEntity.messageType
            );
            subscriber.addMessageListener(image -> {
                CameraData data = new CameraData(image);
                data.setId(widget.id);
                listener.onNewData(data);
            });
        } else {
            Log.i(TAG, "No valid msg!");
        }
    }

    @Override
    public void onNewData(BaseData data) {
    }
}