package com.schneewittchen.rosandroid.model.entities;

import androidx.room.Embedded;
import androidx.room.PrimaryKey;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 30.01.20
 * @updated on 31.01.20
 * @modified by
 */
public abstract class WidgetEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long configId;
    public int posX;
    public int posY;
    public int width;
    public int height;

    @Embedded
    public SubPubNoteEntity subscriber;

    @Embedded
    public SubPubNoteEntity publisher;


    public abstract String getType();

}
