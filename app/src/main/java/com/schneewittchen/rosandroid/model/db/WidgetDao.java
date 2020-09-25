package com.schneewittchen.rosandroid.model.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.schneewittchen.rosandroid.widgets.test.BaseWidget;
import com.schneewittchen.rosandroid.widgets.test.GsonWidgetParser;
import com.schneewittchen.rosandroid.widgets.test.WidgetStorageData;

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

    //TODO: Update test to real classes
    @Query("SELECT * FROM widget_table_test WHERE widget_config_id = :configId")
    protected abstract LiveData<List<WidgetStorageData>> getWidgetsFor(long configId);

    @Query("DELETE FROM widget_table_test WHERE id = :id")
    abstract int deleteById(long id);

    @Query("DELETE FROM widget_table_test")
    abstract void deleteAll();


    public LiveData<List<BaseWidget>> getWidgets(long configId) {
        MediatorLiveData<List<BaseWidget>> widgetList = new MediatorLiveData<>();

        widgetList.addSource(getWidgetsFor(configId), widgetEntities ->
                widgetList.postValue(GsonWidgetParser.getInstance().convert(widgetEntities)));

        return widgetList;
    }

    public void insert(BaseWidget widget) {
        WidgetStorageData storageData = GsonWidgetParser.getInstance().convert(widget);
        this.insert(storageData);
    }

    public void update(BaseWidget widget) {
        WidgetStorageData storageData = GsonWidgetParser.getInstance().convert(widget);
        this.update(storageData);
    }

    public int delete(BaseWidget widget) {
        return this.deleteById(widget.id);
    }
}
