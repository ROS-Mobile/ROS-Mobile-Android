package com.schneewittchen.rosandroidlib.parser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.schneewittchen.rosandroidlib.configuration.Configuration;
import com.schneewittchen.rosandroidlib.widgets.model.Widget;

import java.io.IOException;
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
public class WidgetAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        // If the class that type token represents is a subclass of Base
        // then return your special adapter
        if(Widget.class.isAssignableFrom(typeToken.getRawType())) {
            return (TypeAdapter<T>) customTypeAdapter;
        }
        return null;
    }

    private TypeAdapter<Widget> customTypeAdapter = new TypeAdapter<Widget>() {
        @Override
        public void write(JsonWriter out, Widget widget) throws IOException {
            out.beginObject();
            out.value(widget.getType());
            out.endObject();
        }

        @Override
        public Widget read(JsonReader in) throws IOException {
            // Deserializing to subclasses not interesting yet.
            // Actually it is impossible if the JSON does not contain
            // information about the subclass to which to deserialize
            return null;
        }

    };
}
