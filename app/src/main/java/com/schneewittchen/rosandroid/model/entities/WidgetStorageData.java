package com.schneewittchen.rosandroid.model.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * TODO: Description
 *
 * Replaced version of Base Entity.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 23.09.20
 * @updated on
 * @modified by
 */

@Entity(tableName = "widget_table")
public class WidgetStorageData {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "type_name")
    @NonNull
    public String typeName;

    @ColumnInfo(name = "widget_config_id")
    @NonNull
    public long configId;

    @ColumnInfo(name = "data")
    @NonNull
    public String data;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;
}
