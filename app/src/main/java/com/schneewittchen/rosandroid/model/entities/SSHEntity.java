package com.schneewittchen.rosandroid.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 04.06.20
 * @updated on 04.06.20
 * @modified by
 */

@Entity(tableName = "ssh_table")
public class SSHEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long configId;

    public String ip;
    public int port;
    public String username;
    public String password;
}