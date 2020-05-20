package com.schneewittchen.rosandroid.model.db;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

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
public abstract class ConfigDao implements BaseDao<ConfigEntity>{

    static final String TAG = ConfigDao.class.getCanonicalName();


    @Transaction
    public void insertComplete(ConfigEntity config) {
        Log.i(TAG, "Insert config: " + config);

        // First save config to get its id
        long configId = insert(config);

        Log.i(TAG, "Inserted with id: " + configId);

        if (config.master != null) {
            // Update masters config id
            config.master.configId = configId;

            // Save master
            insert(config.master);
        }

        for (WidgetEntity widget: config.widgets) {
            insert(widget);
        }
    }

    @Query("SELECT * FROM config_table")
    abstract LiveData<List<ConfigEntity>> getAllConfigs();

    @Query("SELECT * FROM config_table where id = :id")
    abstract LiveData<ConfigEntity> getConfig(long id);

    @Query("SELECT * FROM config_table ORDER BY creationTime DESC LIMIT 1")
    abstract LiveData<ConfigEntity> getLatestConfig();

    @Query("DELETE FROM config_table where id = :id")
    abstract void removeConfig(long id);

    @Query("DELETE FROM config_table")
    abstract void deleteAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(MasterEntity master);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(WidgetEntity widgetEntity);


}