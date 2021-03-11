package com.schneewittchen.rosandroid.ui.general;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 17.03.20
 * @updated on 17.03.20
 * @modified by
 */
public class Position {

    public int x;
    public int y;
    public int width;
    public int height;


    public Position() {
        this(0, 0, 0, 0);
    }

    public Position(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
