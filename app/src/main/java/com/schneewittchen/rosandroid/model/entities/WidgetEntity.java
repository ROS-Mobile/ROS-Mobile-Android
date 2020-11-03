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
 * @version 1.0.2
 * @created on 30.01.20
 * @updated on 27.04.20
 * @modified by Nils Rottmann
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

    @ColumnInfo(name = "widget_name")
    @NonNull
    public String name;

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

    @Embedded(prefix = "subPubEntity")
    public SubPubNoteEntity subPubNoteEntity;

    // Joystick specifics

    @ColumnInfo(name = "x_axis_mapping")
    public String xAxisMapping;

    @ColumnInfo(name = "y_axis_mapping")
    public String yAxisMapping;

    @ColumnInfo(name = "x_scale_left")
    public float xScaleLeft;

    @ColumnInfo(name = "x_scale_right")
    public float xScaleRight;

    @ColumnInfo(name = "y_scale_left")
    public float yScaleLeft;

    @ColumnInfo(name = "y_scale_right")
    public float yScaleRight;

    // Labeled widgets specifics

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "rotation")
    public int rotation;
}
