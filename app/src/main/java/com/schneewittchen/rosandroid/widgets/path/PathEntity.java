package com.schneewittchen.rosandroid.widgets.path;

import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;


/**
 * Path entity represents a widget which subscribes
 * to a topic with message type "nav_msgs.Path".
 * Usable in 2D widgets.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class PathEntity extends SubscriberLayerEntity {

    public float lineWidth;
    public String lineColor;


    public PathEntity() {
        this.topic = new Topic("/move_base/TebLocalPlannerROS/local_plan", nav_msgs.Path._TYPE);
        this.lineWidth = 4;
        this.lineColor = "ff0000ff";
    }
}
