package com.schneewittchen.rosandroid.widgets.costmap;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.model.entities.SubscriberEntity;

import nav_msgs.OccupancyGrid;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 14.09.2020
 * @updated on 23.10.2020
 * @modified by Nico Studt
 */
public class CostMapEntity extends SubscriberEntity {
    
    public CostMapEntity() {
        this.width = 6;
        this.height = 6;
        this.topic = new Topic("move_base/global_costmap/costmap", OccupancyGrid._TYPE);
    }
    
}
