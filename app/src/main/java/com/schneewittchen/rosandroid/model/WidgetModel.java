package com.schneewittchen.rosandroid.model;


import com.schneewittchen.rosandroid.R;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 20.01.20
 * @updated on 24.01.20
 * @modified by
 */
public class WidgetModel {

    static int[] widget_names = new int[]{
            R.string.widget_joystick,
            R.string.widget_gridmap
    };

    public static int[] getWidgetNames() {
        return widget_names;
    }

    public static int getWidgetLayout(int id){
        switch (id){
            case R.string.widget_gridmap:
                return R.layout.fragment_grid;
        }

        return -1;
    }
}
