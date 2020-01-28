package com.schneewittchen.rosandroidlib.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.schneewittchen.rosandroidlib.model.ConfigModel;
import com.schneewittchen.rosandroidlib.model.entities.Configuration;
import com.schneewittchen.rosandroidlib.model.entities.Master;
import com.schneewittchen.rosandroidlib.model.entities.Widget;
import com.schneewittchen.rosandroidlib.model.entities.WidgetGridMap;
import com.schneewittchen.rosandroidlib.model.entities.WidgetJoystick;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 21.01.20
 * @updated on 25.01.20
 * @modified by
 */
public class ConfigurationParser {

    public void test(){
        // Create Test
        ArrayList<Configuration> configurations = new ArrayList<>();

        ConfigModel configModel = new ConfigModel();

        for(int i = 0; i < 3; i++){
            Configuration configuration = configModel.getNewConfig();
            configuration.widgets.add(new WidgetJoystick());

            configurations.add(configuration);
        }


        // Serialization
        Gson gson = getGson();

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

    public static List<Configuration> getConfigs(String jsonString) {
        Type collectionType = new TypeToken<ArrayList<Configuration>>(){}.getType();
        return getGson().fromJson(jsonString, collectionType);
    }

    public static String getJson(List<Configuration> configs) {
        return getGson().toJson(configs);
    }


    private static Gson getGson() {
        RuntimeTypeAdapterFactory<Widget> widgetAdapterFactory = RuntimeTypeAdapterFactory
                .of(Widget.class, "type")
                .registerSubtype(WidgetJoystick.class)
                .registerSubtype(WidgetGridMap.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(widgetAdapterFactory)
                .create();

        return gson;
    }
}
