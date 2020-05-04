package com.schneewittchen.rosandroid.model.entities;

import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 03.02.20
 * @updated on 27.04.20
 * @modified by Nils Rottmann
 */
public class WidgetFactory {

    private static final String TAG = WidgetFactory.class.getCanonicalName();

    public static List<BaseEntity> convert(List<WidgetEntity> widgetParentList) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<BaseEntity> widgetList = new ArrayList<>();

        for (WidgetEntity widget: widgetParentList) {
            widgetList.add(convert(widget));
        }

        return  widgetList;
    }

    public static BaseEntity convert(WidgetEntity widgetParent) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String prefix = "com.schneewittchen.rosandroid.widgets.";
        String className = prefix + widgetParent.type.toLowerCase() + ".Widget" + widgetParent.type + "Entity";
        Class<?> clazz = Class.forName(className);
        Constructor<?> ctor = clazz.getConstructor();
        BaseEntity widget = (BaseEntity) ctor.newInstance();

        widget.id = widgetParent.id;
        widget.type = widgetParent.type;
        widget.name = widgetParent.name;
        widget.configId = widgetParent.configId;
        widget.creationTime = widgetParent.creationTime;
        widget.posX = widgetParent.posX;
        widget.posY = widgetParent.posY;
        widget.width = widgetParent.width;
        widget.height = widgetParent.height;
        widget.subPubNoteEntity = widgetParent.subPubNoteEntity;

        return widget;
    }
}
