package com.schneewittchen.rosandroid.model.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 30.01.20
 * @updated on 31.01.20
 * @modified by
 */
@Entity(tableName = "config_table")
public class ConfigEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long creationTime;
    public long lastUsed;
    public String name;
    public boolean isFavourite;

    @Ignore
    public MasterEntity master;

    @Ignore
    public ArrayList<WidgetEntity> widgets;

    public ConfigEntity() {
        widgets = new ArrayList<>();
    }
}