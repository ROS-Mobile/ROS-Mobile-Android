package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.3
 * @created on 10.01.20
 * @updated on 11.04.20
 * @modified by Nico Studt
 */
public class MasterViewModel extends AndroidViewModel {

    private static final String TAG = MasterViewModel.class.getSimpleName();

    private RosDomain rosDomain;

    private MutableLiveData<String> deviceIpLiveData;
    private MutableLiveData<String> networkSSIDLiveData;
    private LiveData<MasterEntity> currentMaster;


    public MasterViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);
        currentMaster = rosDomain.getCurrentMaster();
    }


    public void setMasterIp(String ipString) {
        MasterEntity master = currentMaster.getValue();
        master.ip = ipString;
        rosDomain.updateMaster(master);
    }

    public void setMasterPort(String portString) {
        int port = Integer.parseInt(portString);
        MasterEntity master = currentMaster.getValue();
        master.port = port;
        rosDomain.updateMaster(master);
    }

    public void useIpWithAffixes(boolean useAffixes) {

    }

    public void connectToMaster() {
        rosDomain.connectToMaster();
    }

    public LiveData<MasterEntity> getMaster() {
        return rosDomain.getCurrentMaster();
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

}
