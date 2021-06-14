package com.schneewittchen.rosandroid.model.repositories;

import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroid.model.entities.SSHEntity;


/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 04.06.20
 * @updated on
 * @modified by
 */

public interface SshRepository {

    void startSession();

    void stopSession();

    LiveData<Boolean> isConnected();

    void sendMessage(String message);

    void abort();

    LiveData<String> getOutputData();

    void updateSSH(SSHEntity ssh);

    LiveData<SSHEntity> getCurrentSSH();
}
