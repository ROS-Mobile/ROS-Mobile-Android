package com.schneewittchen.rosandroid.widgets.costmap2d;

import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import nav_msgs.OccupancyGrid;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class CostMap2DEntity extends SubscriberLayerEntity {
    
    public CostMap2DEntity() {
        this.topic = new Topic("/move_base/local_costmap/costmap", OccupancyGrid._TYPE);
    }
    
}
