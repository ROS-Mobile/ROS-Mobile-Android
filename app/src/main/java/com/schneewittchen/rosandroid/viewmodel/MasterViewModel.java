package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.rosRepo.connection.ConnectionType;
import com.schneewittchen.rosandroid.utility.Utils;


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
        setWifiSSID();
        setIpText();
        rosDomain.connectToMaster();
    }

    public void disconnectFromMaster() {
        rosDomain.disconnectFromMaster();
    }

    public LiveData<MasterEntity> getMaster() {
        return rosDomain.getCurrentMaster();
    }

    public LiveData<ConnectionType> getRosConnection() {
        return rosDomain.getRosConnection();
    }

    public LiveData<String> getDeviceIp(){
        if (deviceIpLiveData == null) {
            deviceIpLiveData = new MutableLiveData<>();
        }

        setIpText();

        return deviceIpLiveData;
    }

    public LiveData<String> getCurrentNetworkSSID(){
        if (networkSSIDLiveData == null) {
            networkSSIDLiveData = new MutableLiveData<>();
        }

        setWifiSSID();

        return networkSSIDLiveData;
    }

    private void setIpText() {
        String ssid = Utils.getIPAddress(true);

        deviceIpLiveData.postValue(ssid);
    }

    private void setWifiSSID() {
        WifiManager wifiManager = (WifiManager) getApplication().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        String ssid = Utils.getWifiSSID(wifiManager);

        if (ssid == null) {
            ssid = "None";
        }

        networkSSIDLiveData.postValue(ssid);
    }

}
