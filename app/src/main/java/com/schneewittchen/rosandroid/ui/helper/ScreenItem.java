package com.schneewittchen.rosandroid.ui.helper;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 19.06.20
 * @updated on
 * @modified by
 */

public class ScreenItem {

    String Title, Description;
    int ScreenImage;

    public ScreenItem(String title, String description, int screenImage) {
        Title = title;
        Description = description;
        ScreenImage = screenImage;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setScreenImage(int screenImage) {
        ScreenImage = screenImage;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getScreenImage() {
        return ScreenImage;
    }
}
