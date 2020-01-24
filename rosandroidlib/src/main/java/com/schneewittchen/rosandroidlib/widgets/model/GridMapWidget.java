package com.schneewittchen.rosandroidlib.widgets.model;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.01.20
 * @updated on 21.01.20
 * @modified by
 */
public class GridMapWidget extends Widget {

    public String subscribedTopic = "/occupancy_grid";

    @Override
    public String getType() {
        return "_gridmap";
    }
}
