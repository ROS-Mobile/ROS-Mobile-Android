package com.schneewittchen.rosandroid.viewmodel;


import android.app.Application;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.repositories.RosRepo;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.3
 * @created on 10.01.20
 * @updated on 06.04.20
 * @modified by Nils Rottmann
 */
public class MasterConfigViewModel extends AndroidViewModel {

    private static final String TAG = MasterConfigViewModel.class.getCanonicalName();

    RosRepo rosRepo;
    ConfigRepository configRepository;

    LiveData<MasterEntity> currentMaster;
    MutableLiveData<String> deviceIpLiveData;
    MutableLiveData<String> networkSSIDLiveData;


    public MasterConfigViewModel(@NonNull Application application) {
        super(application);

        rosRepo = RosRepo.getInstance();
        configRepository = ConfigRepositoryImpl.getInstance(application);

        currentMaster = Transformations.switchMap(configRepository.getCurrentConfigId(),
                configId -> configRepository.getMaster(configId));
    }


    public void connectToMaster() {
        rosRepo.setMasterAddress(currentMaster.getValue().ip, currentMaster.getValue().port);
        rosRepo.connectToMaster();
    }

    public void setMasterIp(String ipString) {
        System.out.println("Set Master IP: " + ipString);
        // TODO: Set master ip in current config
        configRepository.setMaster(currentMaster.getValue(), ipString);
    }

    public void setMasterPort(String portString) {
        System.out.println("Set Master port: " + portString);
        // TODO: Set master port in current config
        configRepository.setPort(currentMaster.getValue(), portString);
    }

    public void useIpWithAffixes(boolean useAffixes) {

    }

    public LiveData<String> getDeviceIp(){
        if (deviceIpLiveData == null) {
            deviceIpLiveData = new MutableLiveData<>();
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> deviceIpLiveData.postValue("My Test IP 123"), 5000);

        return deviceIpLiveData;
    }

    public LiveData<String> getCurrentNetworkSSID(){
        if (networkSSIDLiveData == null) {
            networkSSIDLiveData = new MutableLiveData<>();
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> networkSSIDLiveData.postValue("WLANDiesDas"), 8000);


        return networkSSIDLiveData;
    }

    public LiveData<MasterEntity> getMaster() {
        return this.currentMaster;
    }
}
