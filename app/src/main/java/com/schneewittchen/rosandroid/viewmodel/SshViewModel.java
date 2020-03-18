package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;
import com.schneewittchen.rosandroid.model.repositories.SshRepository;
import com.schneewittchen.rosandroid.model.repositories.SshRepositoryImpl;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 18.03.20
 * @updated on
 * @modified by
 */

public class SshViewModel extends AndroidViewModel {

    SshRepositoryImpl sshRepositoryImpl;

    public SshViewModel(@NonNull Application application) {
        super(application);
        sshRepositoryImpl = sshRepositoryImpl.getInstance();
    }

    public void connectViaSSH(String username, String password, String ipAddress, int port) {
        sshRepositoryImpl.startSession(username, password, ipAddress, port);
    }

    public void stopSsh() {
        sshRepositoryImpl.stopSession();
    }

    public LiveData<Boolean> isConnected() {
        return sshRepositoryImpl.isConnected();
    }

    public void sendViaSSH(String message) {
        sshRepositoryImpl.sendMessage(message);
    }

    public LiveData<String> getOutputData() {
        return sshRepositoryImpl.getOutputData();
    }

}
