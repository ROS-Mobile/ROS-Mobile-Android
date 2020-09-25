package com.schneewittchen.rosandroid.widgets.test;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 23.09.20
 */
public class GsonWidgetParser {

    private static final String TAG = GsonWidgetParser.class.getSimpleName();
    private static GsonWidgetParser instance;

    private  Gson gson;


    public static GsonWidgetParser getInstance() {
        if (instance == null) {
            instance = new GsonWidgetParser();
        }

        return instance;
    }


    public GsonWidgetParser() {
        gson = new GsonBuilder()
                .registerTypeAdapter(BaseWidget.class, new WidgetSerializationAdapter())
                .setPrettyPrinting()
                .create();
    }

    /**
     * Convert a Widget Storage Object list into a Base Widget list.
     * @param storageDataList Storage data list to convert
     * @return Converted widget list
     */
    public List<BaseWidget> convert(List<WidgetStorageData> storageDataList) {
        List<BaseWidget> widgets = new ArrayList<>();

        // Convert each storage object to a widget
        for (WidgetStorageData storageData: storageDataList) {
            BaseWidget widget = convert(storageData);

            if (widget != null) {
                widgets.add(widget);
            }
        }

        return widgets;
    }

    /**
     * Convert a Widget Storage Object into a Base Widget.
     * @param storageData Storage data to convert
     * @return Converted widget
     */
    public BaseWidget convert(WidgetStorageData storageData) {
        try {
            Type type = Class.forName(storageData.typeName);
            BaseWidget widget = gson.fromJson(storageData.data, type);
            widget.id = storageData.id;

            return widget;

        } catch (ClassNotFoundException e) {
            Log.e(TAG, String.format("Conversion error. Class for %s can not be found",
                    storageData.typeName));
        }

        return null;
    }

    /**
     * Convert a Base Widget into a widget storage object.
     * @param widget Widget to convert
     * @return Converted storage data
     */
    public WidgetStorageData convert(BaseWidget widget) {
        WidgetStorageData storageData = new WidgetStorageData();
        storageData.id = widget.id;
        storageData.typeName = widget.getClass().getName();
        storageData.configId = widget.configId;
        storageData.data = gson.toJson(widget);

        return storageData;
    }


}
