package com.schneewittchen.rosandroidlib.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.schneewittchen.rosandroidlib.model.entities.Configuration;
import com.schneewittchen.rosandroidlib.model.entities.Widget;

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
    public JsonElement serialize(Configuration config, Type typeOfSrc, JsonSerializationContext context) {

        TypeToken<ArrayList<Widget>> typeDescription = new TypeToken<ArrayList<Widget>>() {};
        JsonElement widgets = context.serialize(config.widgets, typeDescription.getType());

        JsonObject obj = new JsonObject();

        obj.addProperty("id", config.id);
        obj.addProperty("name", config.name);
        obj.addProperty("creationTime", config.creationTime);
        obj.addProperty("isFavourite", config.isFavourite);
        obj.add("master", context.serialize(config.master));
        obj.add("widgets", widgets);

        return obj;

    }
}
