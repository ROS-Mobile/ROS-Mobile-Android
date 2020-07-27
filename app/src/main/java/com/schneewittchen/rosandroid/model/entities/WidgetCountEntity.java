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
 * @version 1.0.0
 * @created on 26.07.20
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 */
@Entity(tableName = "widget_count_table")
public class WidgetCountEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "widget_config_id")
    @NonNull
    public long configId;

    @ColumnInfo(name = "widget_type")
    public String type;

    @ColumnInfo(name = "widget_count")
    public long count;
}
