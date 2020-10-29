package com.schneewittchen.rosandroid.model.db;

import androidx.room.Dao;
import androidx.room.Query;

import com.schneewittchen.rosandroid.model.entities.WidgetCountEntity;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 26.07.20
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 */
@Dao()
public abstract class WidgetCountDao implements BaseDao<WidgetCountEntity>{

    @Query("UPDATE widget_count_table SET widget_count = widget_count + 1 WHERE widget_type = :type AND widget_config_id =:configId")
    public abstract void incrementValue(long configId, String type);

    @Query("SELECT * FROM widget_count_table WHERE widget_config_id = :configId AND widget_type = :type LIMIT 1")
    public abstract WidgetCountEntity getWidgetCount(long configId, String type);

    public WidgetCountEntity getWidgetCountEntity(long configId, String type) {
        return getWidgetCount(configId,type);
    }

    @Query("DELETE FROM widget_count_table")
    abstract void deleteAll();
}
