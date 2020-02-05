package com.schneewittchen.rosandroid.model.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
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
@Entity(tableName = "widget_table")
public class WidgetEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "widget_config_id")
    @NonNull
    public long configId;

    @ColumnInfo(name = "creation_time")
    @NonNull
    public long creationTime;

    @ColumnInfo(name = "widget_type")
    @NonNull
    public String type;

    @ColumnInfo(name = "widget_position_x")
    @NonNull
    public int posX;

    @ColumnInfo(name = "widget_position_y")
    @NonNull
    public int posY;

    @ColumnInfo(name = "widget_width")
    @NonNull
    public int width;

    @ColumnInfo(name = "widget_height")
    @NonNull
    public int height;

    @Embedded(prefix = "sub_")
    public SubPubNoteEntity subscriber;

    @Embedded(prefix = "pub_")
    public SubPubNoteEntity publisher;


    public String getType() {
        return "widget";
    }

}
