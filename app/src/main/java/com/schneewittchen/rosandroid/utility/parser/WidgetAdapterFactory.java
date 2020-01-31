package com.schneewittchen.rosandroid.utility.parser;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

import java.io.IOException;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 21.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class WidgetAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        // If the class that type token represents is a subclass of Base
        // then return your special adapter
        if(WidgetEntity.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) customTypeAdapter;
        }
        return null;
    }

    private TypeAdapter<WidgetEntity> customTypeAdapter = new TypeAdapter<WidgetEntity>() {
        @Override
        public void write(JsonWriter out, WidgetEntity widget) throws IOException {
            out.beginObject();
            out.value(widget.getType());
            out.endObject();
        }

        @Override
        public WidgetEntity read(JsonReader in) throws IOException {
            // Deserializing to subclasses not interesting yet.
            // Actually it is impossible if the JSON does not contain
            // information about the subclass to which to deserialize
            return null;
        }

    };
}
