package com.schneewittchen.rosandroidlib.model.entities;

import java.util.ArrayList;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.01.20
 * @updated on 24.01.20
 * @modified by
 */
public class Configuration {

    public long id;
    public long creationTime;
    public String name;
    public boolean isFavourite;
    public Master master;
    public ArrayList<Widget> widgets;

    public Configuration () {
        this.widgets = new ArrayList<>();
    }
}
