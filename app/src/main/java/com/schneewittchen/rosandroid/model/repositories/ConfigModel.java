package com.schneewittchen.rosandroid.model.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.SSHEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetCountEntity;
import com.schneewittchen.rosandroid.utility.ListLiveData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.4
 * @created on 24.01.20
 * @updated on 20.05.20
 * @modified by Nico Studt
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 */
public class ConfigModel {

    private static ConfigModel mInstance;

    private WeakReference<Application> appRef;
    private ListLiveData<ConfigEntity> mConfigs;
    private static String[] widgetNames;

    public static ConfigModel getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new ConfigModel(application);
        }

        widgetNames = application.getResources().getStringArray(R.array.widget_names);

        return mInstance;
    }


    private ConfigModel(Application application) {
        this.appRef = new WeakReference<>(application);
    }


    public void createNewConfig(Context context) {
        ConfigEntity newConfig = getNewConfig(context);
    }

    public void addConfiguration(ConfigEntity config) {
        mConfigs.add(config);
    }

    public void removeConfig(long configId) {
        List<ConfigEntity> configs = mConfigs.getValue();

        for (int i = 0; i < configs.size(); i++) {
            ConfigEntity config = configs.get(i);

            if (config.id == configId){
                configs.remove(config);
                break;
            }
        }

        mConfigs.setValue(configs);
    }


    /**
     * Create a blank configartion without resource adaption.
     *
     * @return Default configuration
     */
    public ConfigEntity getNewConfig() {

        // Create master data
        MasterEntity master = new MasterEntity();

        master.ip = "192.168.0.0";
        master.port = 11311;

        // Create ssh data
        SSHEntity ssh = new SSHEntity();

        ssh.ip = "192.168.1.1";
        ssh.port = 22;
        ssh.username = "pi";
        ssh.password = "raspberry";

        // Create widgetCount
        ArrayList<WidgetCountEntity> widgetCountEntityList = new ArrayList<WidgetCountEntity>();
        for (int i=0; i<widgetNames.length; i++) {
            WidgetCountEntity widgetCountEntity = new WidgetCountEntity();
            widgetCountEntity.type = widgetNames[i];
            widgetCountEntity.count = 0;
            widgetCountEntityList.add(widgetCountEntity);
        }

        // Create configuration data
        ConfigEntity newConfig = new ConfigEntity();

        newConfig.creationTime = System.nanoTime();
        newConfig.name = "New Config";
        newConfig.isFavourite = false;
        newConfig.master = master;
        newConfig.ssh = ssh;
        newConfig.widgetCounts = widgetCountEntityList;

        return newConfig;
    }

    /**
     * Create a blank configartion with respect to the application context
     * and for example users language (if available in the resources).
     *
     * @param context Application context
     * @return Default configuration
     */
    public ConfigEntity getNewConfig(Context context) {
        Resources res = context.getResources();

        // Create master data
        MasterEntity master = new MasterEntity();

        master.ip = res.getString(R.string.default_master_ip);
        master.port = 12345;

        // Create ssh data
        SSHEntity ssh = new SSHEntity();

        ssh.ip = "192.168.1.1";
        ssh.port = 22;
        ssh.username = "pi";
        ssh.password = "raspberry";

        // Create widgetCount
        ArrayList<WidgetCountEntity> widgetCountEntityList = new ArrayList<WidgetCountEntity>();
        for (int i=0; i<widgetNames.length; i++) {
            WidgetCountEntity widgetCountEntity = new WidgetCountEntity();
            widgetCountEntity.type = widgetNames[i];
            widgetCountEntity.count = 0;
            widgetCountEntityList.add(widgetCountEntity);
        }

        // Create configuration data
        ConfigEntity newConfig = new ConfigEntity();

        newConfig.id = getMaxId();
        newConfig.creationTime = System.nanoTime();
        newConfig.name = res.getString(R.string.default_config_name);
        newConfig.isFavourite = false;
        newConfig.master = master;
        newConfig.ssh = ssh;
        newConfig.widgetCounts = widgetCountEntityList;


        return newConfig;
    }

    private long getMaxId () {
        return 0;
    }
}
