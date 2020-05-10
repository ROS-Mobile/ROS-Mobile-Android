package com.schneewittchen.rosandroid.widgets.gps;

import com.schneewittchen.rosandroid.widgets.base.BaseData;

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

    public double lat;
    public double lon;

    public GpsData(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

}
