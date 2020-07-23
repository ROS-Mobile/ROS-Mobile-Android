package com.schneewittchen.rosandroid.model.entities;

import android.content.res.Resources;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.schneewittchen.rosandroid.R;

import java.util.ArrayList;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 30.01.20
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 */
@Entity(tableName = "config_table")
public class ConfigEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long creationTime;
    public long lastUsed;
    public String name;
    public boolean isFavourite;
    public int widgetCount;

    @Ignore
    public MasterEntity master;

    @Ignore
    public SSHEntity ssh;

    @Ignore
    public ArrayList<WidgetEntity> widgets;

    public ConfigEntity() {
        widgets = new ArrayList<>();
    }
}