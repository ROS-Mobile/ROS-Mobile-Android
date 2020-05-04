package com.schneewittchen.rosandroid.widgets.gridmap;

import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 20.04.20
 * @updated on 20.04.20
 * @modified by
 */
public class GridMapNode extends BaseNode {

    public GridMapNode(BaseEntity widget) {
        super(widget);
    }


    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<nav_msgs.OccupancyGrid> subscriber = connectedNode.newSubscriber(
                widget.subPubNoteEntity.topic, widget.subPubNoteEntity.messageType
        );
        subscriber.addMessageListener(new MessageListener<nav_msgs.OccupancyGrid>() {
            @Override
            public void onNewMessage(nav_msgs.OccupancyGrid occupancyGrid) {
                int width = occupancyGrid.getInfo().getWidth();
                int height = occupancyGrid.getInfo().getHeight();
                byte[] array = occupancyGrid.getData().array();
                float res = occupancyGrid.getInfo().getResolution();
                float x0 = (float) occupancyGrid.getInfo().getOrigin().getPosition().getX();
                float y0 = (float) occupancyGrid.getInfo().getOrigin().getPosition().getY();
                GridMapData data = new GridMapData(width, height, array, res, x0, y0);
                data.setId(widget.id);
                listener.onNewData(data);
            }
        });
    }

    @Override
    public void onNewData(BaseData data) {
    }
}
