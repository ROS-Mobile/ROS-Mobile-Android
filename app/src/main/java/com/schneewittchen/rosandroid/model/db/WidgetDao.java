package com.schneewittchen.rosandroid.model.db;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.general.GsonWidgetParser;
import com.schneewittchen.rosandroid.model.entities.WidgetStorageData;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 31.01.20
 * @updated on 09.05.20
 * @modified by Nico Studt
 * @updated on 23.09.20
 * @modified by Nico Studt
 */
@Dao
public abstract class WidgetDao implements BaseDao<WidgetStorageData>{

    public static String TAG = WidgetDao.class.getSimpleName();


    //TODO: Update test to real classes
    @Query("SELECT * FROM widget_table WHERE widget_config_id = :configId")
    protected abstract LiveData<List<WidgetStorageData>> getWidgetsFor(long configId);

    @Query("DELETE FROM widget_table WHERE id = :id")
    abstract int deleteById(long id);
    
    @Query("DELETE FROM widget_table WHERE widget_config_id = :id")
    abstract int deleteWithConfigId(long id);

    @Query("DELETE FROM widget_table")
    abstract void deleteAll();

    @Query("SELECT EXISTS (SELECT 1 FROM widget_table WHERE widget_config_id = :configId AND name = :name)")
    public abstract boolean exists(long configId, String name);

    @Query("SELECT * FROM widget_table WHERE widget_config_id = :configId AND id = :widgetId")
    abstract LiveData<WidgetStorageData> getWidgetIntern(long configId, long widgetId);


    public LiveData<BaseEntity> getWidget(long configId, long widgetId) {
        MediatorLiveData<BaseEntity> widget = new MediatorLiveData<>();

        widget.addSource(getWidgetIntern(configId, widgetId), data -> {
            widget.postValue(GsonWidgetParser.getInstance().convert(data));
        });

        return widget;
    }

    public LiveData<List<BaseEntity>> getWidgets(long configId) {
        MediatorLiveData<List<BaseEntity>> widgetList = new MediatorLiveData<>();

        widgetList.addSource(getWidgetsFor(configId), widgetEntities ->
                widgetList.postValue(GsonWidgetParser.getInstance().convert(widgetEntities)));

        return widgetList;
    }

    public void insert(BaseEntity widget) {
        WidgetStorageData storageData = GsonWidgetParser.getInstance().convert(widget);
        this.insert(storageData);
    }

    public void update(BaseEntity widget) {
        WidgetStorageData storageData = GsonWidgetParser.getInstance().convert(widget);
        this.update(storageData);
    }

    public int delete(BaseEntity widget) {
        return this.deleteById(widget.id);
    }
}
