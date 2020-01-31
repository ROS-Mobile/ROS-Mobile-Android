package com.schneewittchen.rosandroid.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.RawQuery;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
@Dao
public abstract class WidgetDao implements BaseDao<WidgetEntity>{

    @RawQuery
    abstract LiveData<List<WidgetEntity>> getWidgets(String query);

}
