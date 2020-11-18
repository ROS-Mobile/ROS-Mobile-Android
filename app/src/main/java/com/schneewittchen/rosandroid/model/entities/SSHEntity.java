package com.schneewittchen.rosandroid.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 04.06.20
 */

@Entity(tableName = "ssh_table")
public class SSHEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long configId;
    public String ip = "192.168.1.1";
    public int port = 22;
    public String username = "pi";
    public String password = "raspberry";
}