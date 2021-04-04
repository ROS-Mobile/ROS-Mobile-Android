package com.schneewittchen.rosandroid.widgets.label;

import com.schneewittchen.rosandroid.model.entities.widgets.SilentWidgetEntity;


/**
 * TODO: Description
 *
 * @author Dragos Circa
 * @version 1.0.0
 * @created on 02.11.2020
 * @updated on 18.11.2020
 * @modified by Nils Rottmann
 * @updated on 01.04.2021
 * @modified by Nico Studt
 */
public class LabelEntity extends SilentWidgetEntity {

    public String text;
    public int rotation;

    public LabelEntity() {
        this.width = 3;
        this.height = 1;
        this.text = "A label";
        this.rotation = 0;
    }
}
