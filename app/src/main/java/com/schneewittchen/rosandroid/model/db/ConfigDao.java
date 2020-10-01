package com.schneewittchen.rosandroid.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1
 * @created on 31.01.20
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 * @updated on 23.09.20
 * @modified by Nico Studt
 */
@Dao
public abstract class ConfigDao implements BaseDao<ConfigEntity>{

    static final String TAG = ConfigDao.class.getCanonicalName();


    @Query("SELECT * FROM config_table")
    abstract LiveData<List<ConfigEntity>> getAllConfigs();

    @Query("SELECT * FROM config_table where id = :id")
    abstract LiveData<ConfigEntity> getConfig(long id);

    @Query("SELECT * FROM config_table ORDER BY creationTime DESC LIMIT 1")
    abstract LiveData<ConfigEntity> getLatestConfig();

    @Query("SELECT * FROM config_table ORDER BY creationTime DESC LIMIT 1")
    abstract ConfigEntity getLatestConfigDirect();

    @Query("DELETE FROM config_table where id = :id")
    abstract void removeConfig(long id);

    @Query("DELETE FROM config_table")
    abstract void deleteAll();

}