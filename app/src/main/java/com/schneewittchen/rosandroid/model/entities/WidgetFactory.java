package com.schneewittchen.rosandroid.model.entities;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 03.02.20
 * @updated on 05.02.20
 * @modified by
 */
public class WidgetFactory {

    private static final String TAG = WidgetFactory.class.getCanonicalName();


    public static List<WidgetEntity> convert(List<WidgetEntity> widgetParentList) {
        List<WidgetEntity> widgetList = new ArrayList<>();

        for (WidgetEntity widget: widgetParentList) {
            widgetList.add(convert(widget));
        }

        return  widgetList;
    }

    public static WidgetEntity convert(WidgetEntity widgetParent) {
        WidgetEntity widget = null;

        switch (widgetParent.type) {
            case "Joystick":
                widget = new WidgetJoystickEntity();
                break;

            case "Grid Map":
                widget = new WidgetGridMapEntity();
                break;

            default:
                Log.i(TAG, "Cant convert from type: " + widgetParent.type);
        }

        widget.id = widgetParent.id;
        widget.type = widgetParent.type;
        widget.configId = widgetParent.configId;
        widget.creationTime = widgetParent.creationTime;
        widget.posX = widgetParent.posX;
        widget.posY = widgetParent.posY;
        widget.width = widgetParent.width;
        widget.height = widgetParent.height;
        widget.publisher = widgetParent.publisher;
        widget.subscriber = widgetParent.subscriber;

        return widget;
    }
}
