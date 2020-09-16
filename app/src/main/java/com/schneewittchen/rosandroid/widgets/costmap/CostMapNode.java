package com.schneewittchen.rosandroid.widgets.costmap;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.09.2020
 * @updated
 * @modified
 */
public class CostMapNode extends BaseNode {


    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<nav_msgs.OccupancyGrid> subscriber = connectedNode.newSubscriber(
                widget.subPubNoteEntity.topic,
                widget.subPubNoteEntity.messageType
        );

        subscriber.addMessageListener(occupancyGrid -> {
            CostMapData data = new CostMapData(occupancyGrid);
            data.setId(widget.id);
            listener.onNewData(data);
        });
    }

    @Override
    public void onNewData(BaseData data) {
    }
}
