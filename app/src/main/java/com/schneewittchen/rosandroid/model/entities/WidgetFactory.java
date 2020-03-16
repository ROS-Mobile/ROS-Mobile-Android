package com.schneewittchen.rosandroid.model.entities;

import android.util.Log;

import com.schneewittchen.rosandroid.widgets.gridmap.WidgetGridMapEntity;
import com.schneewittchen.rosandroid.widgets.joystick.WidgetJoystickEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 03.02.20
 * @updated on 16.02.20
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
            case WidgetEntity.JOYSTICK:
                widget = new WidgetJoystickEntity();
                break;

            case WidgetEntity.MAP:
                // TODO: Set map variables
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
