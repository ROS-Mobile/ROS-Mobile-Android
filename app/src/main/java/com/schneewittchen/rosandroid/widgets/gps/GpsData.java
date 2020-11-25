package com.schneewittchen.rosandroid.widgets.gps;

import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;

import sensor_msgs.NavSatFix;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 05.05.20
 * @updated on 05.05.20
 * @modified by
 */

public class GpsData extends BaseData {

    private NavSatFix navSatFix;

    public GpsData(NavSatFix navSatFix) {
        this.navSatFix = navSatFix;
    }

    public double getLat() {
        return navSatFix.getLatitude();
    }

    public double getLon() {
        return navSatFix.getLongitude();
    }
}
