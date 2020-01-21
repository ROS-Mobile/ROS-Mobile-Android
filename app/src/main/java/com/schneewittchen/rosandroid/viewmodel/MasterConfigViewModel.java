package com.schneewittchen.rosandroid.viewmodel;


import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.schneewittchen.rosandroidlib.RosRepo;



/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 10.01.20
 * @updated on 19.01.20
 * @modified by
 */
public class MasterConfigViewModel extends ViewModel {

    RosRepo rosRepo;
    MutableLiveData<String> deviceIpLiveData;
    MutableLiveData<String> networkSSIDLiveData;


    public MasterConfigViewModel(){
        rosRepo = RosRepo.getInstance();
    }


    public void connectToMaster() {
        rosRepo.connectToMaster();
    }

    public void setMasterIp(String ip) {
        rosRepo.setMasterAddress(ip, 11311);
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
}
