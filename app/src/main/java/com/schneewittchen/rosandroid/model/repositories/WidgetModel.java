package com.schneewittchen.rosandroid.model.repositories;


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
            R.string.widget_joystick_name,
            R.string.widget_map_name
    };

    public static int[] getWidgetNames() {
        return widget_names;
    }

    public static int getWidgetLayout(int id){
        switch (id){
            case R.string.widget_map_name:
                return R.layout.fragment_grid;
        }

        return -1;
    }
}
