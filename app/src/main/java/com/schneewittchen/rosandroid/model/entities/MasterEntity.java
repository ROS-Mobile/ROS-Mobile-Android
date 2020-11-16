package com.schneewittchen.rosandroid.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 30.01.20
 * @updated on 31.01.20
 * @modified by
 */
@Entity(tableName = "master_table")
public class MasterEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long configId;
    public String ip = "192.168.0.0";
    public int port = 11311;
}
