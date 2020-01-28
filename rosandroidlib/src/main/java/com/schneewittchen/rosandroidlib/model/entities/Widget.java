package com.schneewittchen.rosandroidlib.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 25.01.20
 * @updated on 25.01.20
 * @modified by
 */
public abstract class Widget {

    public int posX;
    public int posY;
    public int width;
    public int height;

    public List<SubPubNode> subscriber;
    public List<SubPubNode> publisher;


    public Widget() {
        this.subscriber = new ArrayList<>();
        this.publisher = new ArrayList<>();
    }


    public abstract String getType();
}
