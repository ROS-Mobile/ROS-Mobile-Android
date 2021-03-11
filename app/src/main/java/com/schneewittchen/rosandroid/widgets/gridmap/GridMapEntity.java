package com.schneewittchen.rosandroid.widgets.gridmap;

import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberWidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 31.01.2020
 * @updated on 13.05.2020
 * @modified by Nico Studt
 * @updated on 27.10.2020
 * @modified by Nico Studt
 */
public class GridMapEntity extends SubscriberWidgetEntity {

    public GridMapEntity() {
        this.width = 6;
        this.height = 6;
        this.topic = new Topic("map", nav_msgs.OccupancyGrid._TYPE);
    }
}
