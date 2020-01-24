package com.schneewittchen.rosandroidlib.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.schneewittchen.rosandroidlib.configuration.Configuration;
import com.schneewittchen.rosandroidlib.widgets.model.GridMapWidget;
import com.schneewittchen.rosandroidlib.widgets.model.JoystickWidget;
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
public class ConfigurationParser {

    public void test(){
        // Create Test
        ArrayList<Configuration> configurations = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            Configuration configuration = new Configuration();
            configuration.name += i;

            configuration.widgets.add(new JoystickWidget());
            configurations.add(configuration);
        }


        RuntimeTypeAdapterFactory<Widget> widgetAdapterFactory = RuntimeTypeAdapterFactory
                .of(Widget.class, "type")
                .registerSubtype(JoystickWidget.class)
                .registerSubtype(GridMapWidget.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Configuration.class, new ConfigurationSerializer())
                .registerTypeAdapterFactory(widgetAdapterFactory)
                .create();

        String json = gson.toJson(configurations);
        this.print(json);

        // Deserialization
        Type collectionType = new TypeToken<ArrayList<Configuration>>(){}.getType();
        ArrayList<Configuration> output = gson.fromJson(json, collectionType );

        System.out.println(output.get(1).name);
        System.out.println(output.get(1).widgets.get(0).getClass());
    }

    public void print(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);

        String prettyJsonString = gson.toJson(jsonElement);
        System.out.println(prettyJsonString);
    }
}
