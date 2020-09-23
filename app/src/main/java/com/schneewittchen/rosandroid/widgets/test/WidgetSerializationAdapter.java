package com.schneewittchen.rosandroid.widgets.test;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


/**
 * TODO: Description
 * Credits to Marcus Brutus.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 23.09.20
 * @updated on
 * @modified by
 */
public class WidgetSerializationAdapter implements JsonSerializer<BaseWidget>, JsonDeserializer<BaseWidget> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE  = "INSTANCE";


    @Override
    public JsonElement serialize(BaseWidget src, Type typeOfSrc,
                                 JsonSerializationContext context) {

        JsonObject retValue = new JsonObject();
        String className = src.getClass().getName();
        retValue.addProperty(CLASSNAME, className);
        JsonElement elem = context.serialize(src);
        retValue.add(INSTANCE, elem);
        return retValue;
    }

    @Override
    public BaseWidget deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> klass;

        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }

        return context.deserialize(jsonObject.get(INSTANCE), klass);
    }
}
