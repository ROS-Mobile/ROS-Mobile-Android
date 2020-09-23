package com.schneewittchen.rosandroid.model.db;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T obj);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(T obj);

    @Delete
    int delete(T obj);
}
