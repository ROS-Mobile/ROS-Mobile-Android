package com.schneewittchen.rosandroid.viewmodel;


import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.schneewittchen.rosandroidlib.RosRepo;
import com.schneewittchen.rosandroidlib.model.entities.Configuration;
import com.schneewittchen.rosandroidlib.model.repos.ConfigRepository;
import com.schneewittchen.rosandroidlib.model.repos.ConfigRepositoryImpl;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 10.01.20
 * @updated on 28.01.20
 * @modified by
 */
public class MasterConfigViewModel extends AndroidViewModel {

    RosRepo rosRepo;
    ConfigRepository configRepo;

    LiveData<Configuration> currentConfig;
    MutableLiveData<String> deviceIpLiveData;
    MutableLiveData<String> networkSSIDLiveData;
    MediatorLiveData<String> mNotificationTitle;
    MediatorLiveData<String> mTickerTitle;
    MediatorLiveData<String> mMasterIp;
    MediatorLiveData<String> mMasterPort;


    public MasterConfigViewModel(@NonNull Application application) {
        super(application);

        rosRepo = RosRepo.getInstance();
        configRepo = ConfigRepositoryImpl.getInstance();


        mNotificationTitle = new MediatorLiveData<>();
        mTickerTitle = new MediatorLiveData<>();
        mMasterIp = new MediatorLiveData<>();
        mMasterPort = new MediatorLiveData<>();

        currentConfig = configRepo.getCurrentConfig();

        mNotificationTitle.addSource(currentConfig, configuration ->
                mNotificationTitle.setValue(configuration.master.notificationTitle));

        mTickerTitle.addSource(currentConfig, configuration ->
                mTickerTitle.setValue(configuration.master.notificationTickerTitle));

        mMasterIp.addSource(currentConfig, configuration ->
                mMasterIp.setValue(configuration.master.ip));

        mMasterPort.addSource(currentConfig, configuration ->
                mMasterPort.setValue(Integer.toString(configuration.master.port)));
    }


    public void connectToMaster() {
        rosRepo.connectToMaster();
    }

    public void setMasterIp(String ipString) {
        System.out.println("Set Master IP: " + ipString);
        // TODO: Set master ip in current config
    }

    public void setMasterPort(String portString) {
        System.out.println("Set Master port: " + portString);
        // TODO: Set master port in current config
    }

    public void useIpWithAffixes(boolean useAffixes) {

    }

    public LiveData<String> getDeviceIp(){
        if (deviceIpLiveData == null) {
            deviceIpLiveData = new MutableLiveData<>();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceIpLiveData.postValue("My Test IP 123");
            }
        }, 5000);

        return deviceIpLiveData;
    }

    public LiveData<String> getCurrentNetworkSSID(){
        if (networkSSIDLiveData == null) {
            networkSSIDLiveData = new MutableLiveData<>();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                networkSSIDLiveData.postValue("WLANDiesDas");
            }
        }, 8000);


        return networkSSIDLiveData;
    }

    public LiveData<String> getNotificationTitle() {
        return mNotificationTitle;
    }

    public LiveData<String> getTickerTitle() {
        return mTickerTitle;
    }

    public LiveData<String> getMasterIp() {
        return mMasterIp;
    }

    public LiveData<String> getMasterPort() {
        return mMasterPort;
    }


}
