package com.schneewittchen.rosandroid.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.schneewittchen.rosandroid.model.entities.MasterEntity;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
@Dao()
public abstract class WidgetCountDao implements BaseDao<MasterEntity>{

    @Query("UPDATE widget_count_table SET widget_count = widget_count + 1 WHERE widget_name = :name")
    public abstract void incrementValue(String name);

}
