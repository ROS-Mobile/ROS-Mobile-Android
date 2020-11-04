package com.schneewittchen.rosandroid.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.schneewittchen.rosandroid.model.entities.MasterEntity;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 31.01.20
 * @updated on 01.10.20
 * @modified by Nico Studt
 */
@Dao
public abstract class MasterDao implements BaseDao<MasterEntity>{

    @Query("SELECT * FROM master_table WHERE configId = :configId LIMIT 1")
    abstract LiveData<MasterEntity> getMaster(long configId);

    @Query("DELETE FROM master_table WHERE configId = :configId")
    abstract void delete(long configId);

    @Query("DELETE FROM master_table")
    abstract void deleteAll();
}
