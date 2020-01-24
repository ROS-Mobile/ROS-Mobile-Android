package com.schneewittchen.rosandroidlib.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.schneewittchen.rosandroidlib.configuration.Configuration;
import com.schneewittchen.rosandroidlib.widgets.model.Widget;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.01.20
 * @updated on 21.01.20
 * @modified by
 */
public class ConfigurationSerializer implements JsonSerializer<Configuration>{

    @Override
    public JsonElement serialize(Configuration src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject obj = new JsonObject();

        obj.addProperty("name", src.name);
        obj.addProperty("isFavourite", src.isFavourite);
        obj.add("master", context.serialize(src.master));

        TypeToken<ArrayList<Widget>> typeDescription = new TypeToken<ArrayList<Widget>>() {};
        JsonElement widgets = context.serialize(src.widgets, typeDescription.getType());
        obj.add("widgets", widgets);

        return obj;

    }
}
