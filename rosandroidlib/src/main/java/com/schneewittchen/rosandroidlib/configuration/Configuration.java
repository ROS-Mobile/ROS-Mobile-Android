package com.schneewittchen.rosandroidlib.configuration;

import com.schneewittchen.rosandroidlib.widgets.model.Widget;

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
public class Configuration {

    public String name = "default";
    public boolean isFavourite;
    public ArrayList<Widget> widgets;
    public Master master;


    public Configuration(){
        widgets = new ArrayList<>();
        master = new Master();
    }

}
