package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.entities.SSHEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;
import com.schneewittchen.rosandroid.model.repositories.SshRepository;
import com.schneewittchen.rosandroid.model.repositories.SshRepositoryImpl;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 18.03.20
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 */

public class SshViewModel extends AndroidViewModel {

    SshRepositoryImpl sshRepositoryImpl;
    private LiveData<SSHEntity> currentSSH;

    public SshViewModel(@NonNull Application application) {
        super(application);
        sshRepositoryImpl = SshRepositoryImpl.getInstance(application);
        currentSSH = sshRepositoryImpl.getCurrentSSH();
    }

    public void setSshIp(String ipString) {
        SSHEntity ssh = currentSSH.getValue();
        ssh.ip = ipString;
        sshRepositoryImpl.updateSSHConfig(ssh);
    }

    public void setSshPort(int portInt) {
        SSHEntity ssh = currentSSH.getValue();
        ssh.port = portInt;
        sshRepositoryImpl.updateSSHConfig(ssh);
    }

    public void setSshUsername(String usernameString) {
        SSHEntity ssh = currentSSH.getValue();
        ssh.username = usernameString;
        sshRepositoryImpl.updateSSHConfig(ssh);
    }

    public void setSshPassword(String passwordString) {
        SSHEntity ssh = currentSSH.getValue();
        ssh.password = passwordString;
        sshRepositoryImpl.updateSSHConfig(ssh);
    }


    public void connectViaSSH() {
        sshRepositoryImpl.startSession();
    }

    public void stopSsh() {
        sshRepositoryImpl.stopSession();
    }

    public void sendViaSSH(String message) {
        sshRepositoryImpl.sendMessage(message);
    }

    public LiveData<Boolean> isConnected() {
        return sshRepositoryImpl.isConnected();
    }

    public LiveData<String> getOutputData() {
        return sshRepositoryImpl.getOutputData();
    }

    public LiveData<SSHEntity> getSSH() {
        return sshRepositoryImpl.getCurrentSSH();
    }

}
