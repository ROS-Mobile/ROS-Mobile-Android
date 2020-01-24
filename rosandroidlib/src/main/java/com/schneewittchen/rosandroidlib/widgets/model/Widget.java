package com.schneewittchen.rosandroidlib.widgets.model;

/**
 * Base widget class
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 21.01.20
 * @updated on 21.01.20
 * @modified by
 */
public abstract class Widget {

    protected int posX;
    protected int posY;
    protected int width;
    protected int height;

    public abstract String getType();

    public int[] getLocation(){
        return new int[]{posX, posY, width, height};
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
