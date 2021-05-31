package com.schneewittchen.rosandroid.widgets.rqtplot;

import org.ros.message.Duration;
import org.ros.message.Time;

import java.util.ArrayList;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.05.21
 */
public class PlotDataList extends ArrayList<PlotDataList.PlotData> {

    float maxTime = 10;
    int maxNum = 1000;
    double minValue = Double.MAX_VALUE;
    double maxValue = -Double.MAX_VALUE;
    Time latestTime;


    public void add(double value, Time time) {
        this.add(new PlotData(value, time));
        this.updateMinMax(value);
        this.cleanUp();
    }

    private void cleanUp() {
        while(!this.isEmpty()) {
            if (size() > maxNum || this.get(0).secsToLatest() > maxTime) {
                this.remove(0);
            } else {
                break;
            }
        }
    }

    private void updateMinMax(double value) {
        this.minValue = Math.min(value, minValue);
        this.maxValue = Math.max(value, maxValue);

        if (!isEmpty()) {
            this.latestTime = this.get(size()-1).time;
        }
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public double getMinValue() {
        return this.minValue;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
        this.cleanUp();
    }


    public class PlotData {
        public double value;
        public Time time;

        public PlotData(double value, Time time) {
            this.value = value;
            this.time = time;
        }

        public float secsToLatest() {
            Duration diff = latestTime.subtract(time);
            return diff.secs + diff.nsecs / 1_000_000_000f;
        }
    }
}
