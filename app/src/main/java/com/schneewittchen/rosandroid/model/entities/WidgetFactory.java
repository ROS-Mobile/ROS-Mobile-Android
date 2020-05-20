package com.schneewittchen.rosandroid.model.entities;

import android.util.Log;

import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Converter for widget entities.
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 03.02.20
 * @updated on 10.05.20
 * @modified by Nico Studt
 */
public class WidgetFactory {

    private static final String TAG = WidgetFactory.class.getCanonicalName();
    private static final String WIDGET_PATH = "com.schneewittchen.rosandroid.widgets.";


    /**
     * Convert every widget item to its assigned Base Entity.
     * The data also gets copied.
     *
     * @param widgetParentList List of widget entities
     * @return List of converted widgets to base entities
     */
    public static List<BaseEntity> convert(List<WidgetEntity> widgetParentList){
        List<BaseEntity> widgetList = new ArrayList<>();

        for (WidgetEntity widget: widgetParentList) {
            try {
                BaseEntity entity = convert(widget);
                entity.insert(widget);
                widgetList.add(entity);

            } catch (Exception e) {
                Log.e(TAG, "Cant convert widget. Error: " + e.getCause());
            }
        }

        return  widgetList;
    }

    /**
     * Convert a widget entity (data class) to its assignet base entity based on its type.
     *
     * @param widgetParent Widget entity
     * @return Converted base entity
     */
    private static BaseEntity convert(WidgetEntity widgetParent)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
                    InvocationTargetException, InstantiationException {

        // Get class and copy widget parent to widget
        String className = WIDGET_PATH + widgetParent.type.toLowerCase()
                        + ".Widget" + widgetParent.type + "Entity";
        Class<?> subclass = Class.forName(className);
        Constructor<?> constructor = subclass.getConstructor();

        return (BaseEntity) constructor.newInstance();
    }

}
