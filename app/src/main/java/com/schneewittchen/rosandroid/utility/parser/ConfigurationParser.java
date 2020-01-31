package com.schneewittchen.rosandroid.utility.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetJoystickEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.1
 * @created on 21.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class ConfigurationParser {

    public static List<ConfigEntity> getConfigs(String jsonString) {
        Type collectionType = new TypeToken<ArrayList<ConfigEntity>>(){}.getType();
        return getGson().fromJson(jsonString, collectionType);
    }

    public static String getJson(List<ConfigEntity> configs) {
        return getGson().toJson(configs);
    }


    private static Gson getGson() {
        RuntimeTypeAdapterFactory<WidgetEntity> widgetAdapterFactory = RuntimeTypeAdapterFactory
                .of(WidgetEntity.class, "type")
                .registerSubtype(WidgetJoystickEntity.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(widgetAdapterFactory)
                .create();

        return gson;
    }
}
