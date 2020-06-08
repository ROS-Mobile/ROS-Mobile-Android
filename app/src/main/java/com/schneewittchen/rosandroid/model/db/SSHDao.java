package com.schneewittchen.rosandroid.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.schneewittchen.rosandroid.model.entities.SSHEntity;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 04.06.20
 * @updated on 04.06.20
 * @modified by
 */
@Dao
public abstract class SSHDao implements BaseDao<SSHEntity>{

    @Query("SELECT * FROM ssh_table WHERE configId = :configId LIMIT 1")
    abstract LiveData<SSHEntity> getSSH(long configId);

    @Query("DELETE FROM ssh_table")
    abstract void deleteAll();
}
