package com.schneewittchen.rosandroid.model.repositories;


import com.schneewittchen.rosandroid.R;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 20.01.20
 * @updated on 27.04.20
 * @modified by Nils Rottmann
 */
public class WidgetModel {

    static int[] widget_names = new int[]{
            R.string.widget_joystick_name,
            R.string.widget_map_name,
            R.string.widget_camera_name
    };

    public static int[] getWidgetNames() {
        return widget_names;
    }

    public static int getWidgetLayout(int id){
        switch (id){
            case R.string.widget_map_name:
                return R.layout.fragment_viz;
        }

        return -1;
    }
}
