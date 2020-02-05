package com.schneewittchen.rosandroid.model.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.utility.ListLiveData;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.4
 * @created on 24.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class ConfigModel {

    private static ConfigModel mInstance;

    private WeakReference<Application> appRef;
    private ListLiveData<ConfigEntity> mConfigs;


    public static ConfigModel getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new ConfigModel(application);
        }

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

    public void loadConfigurations(Context context) {
        Resources res = context.getResources();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }


    /**
     * Create a blank configartion without resource adaption.
     *
     * @return Default configuration
     */
    public ConfigEntity getNewConfig() {

        // Create master data
        MasterEntity master = new MasterEntity();

        Random random = new Random();
        master.ip = "123.456.789.01";
        master.port = random.nextInt(99999);
        master.notificationTickerTitle = "Ticker name";
        master.notificationTitle = "Title name";

        // Create configuration data
        ConfigEntity newConfig = new ConfigEntity();

        newConfig.creationTime = System.nanoTime();
        newConfig.name = "New Config";
        newConfig.isFavourite = false;
        newConfig.master = master;

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
        master.notificationTickerTitle = res.getString(R.string.default_master_ticker_title);
        master.notificationTitle = res.getString(R.string.default_master_notification_title);

        // Create configuration data
        ConfigEntity newConfig = new ConfigEntity();

        newConfig.id = getMaxId();
        newConfig.creationTime = System.nanoTime();
        newConfig.name = res.getString(R.string.default_config_name);
        newConfig.isFavourite = false;
        newConfig.master = master;

        return newConfig;
    }

    private long getMaxId () {
        return 0;
    }
}
