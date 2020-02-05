package com.schneewittchen.rosandroid.model.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetFactory;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 31.01.20
 * @updated on 05.02.20
 * @modified by
 */
@Dao
public abstract class WidgetDao implements BaseDao<WidgetEntity>{

    @Query("SELECT * FROM widget_table WHERE widget_config_id = :configId ORDER BY creation_time DESC")
    protected abstract LiveData<List<WidgetEntity>> getWidgetsFor(long configId);

    public LiveData<List<WidgetEntity>> getWidgets(long configId) {
        MediatorLiveData<List<WidgetEntity>> widgetList = new MediatorLiveData<>();

        widgetList.addSource(getWidgetsFor(configId), widgetEntities -> {
            widgetList.postValue(WidgetFactory.convert(widgetEntities));
        });

        return widgetList;
    }

    @Query("DELETE FROM widget_table")
    abstract void deleteAll();
}
