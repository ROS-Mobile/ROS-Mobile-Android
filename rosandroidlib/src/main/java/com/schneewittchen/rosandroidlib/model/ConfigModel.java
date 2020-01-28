package com.schneewittchen.rosandroidlib.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.schneewittchen.rosandroidlib.R;
import com.schneewittchen.rosandroidlib.model.entities.Configuration;
import com.schneewittchen.rosandroidlib.model.entities.Master;
import com.schneewittchen.rosandroidlib.utility.ListLiveData;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 24.01.20
 * @updated on 28.01.20
 * @modified by
 */
public class ConfigModel {

    private static ConfigModel mInstance;

    private ListLiveData<Configuration> mConfigs;
    private MutableLiveData<Configuration> mCurrentConfig;


    public static ConfigModel getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigModel();
        }

        return mInstance;
    }


    public void createNewConfig(Context context) {
        Configuration newConfig = getNewConfig(context);
    }

    public void addConfiguration(Configuration config) {
        mConfigs.add(config);
    }

    public void removeConfig(long configId) {
        List<Configuration> configs = mConfigs.getValue();

        for (int i = 0; i < configs.size(); i++) {
            Configuration config = configs.get(i);

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
    public Configuration getNewConfig() {

        // Create master data
        Master master = new Master();

        master.ip = "123.456.789.01";
        master.port = 11311;
        master.notificationTickerTitle = "Ticker name";
        master.notificationTitle = "Title name";

        // Create configuration data
        Configuration newConfig = new Configuration();

        newConfig.id = getMaxId();
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
    public Configuration getNewConfig(Context context) {
        Resources res = context.getResources();

        // Create master data
        Master master = new Master();

        master.ip = res.getString(R.string.default_master_ip);
        master.port = 12345;
        master.notificationTickerTitle = res.getString(R.string.default_master_ticker_title);
        master.notificationTitle = res.getString(R.string.default_master_notification_title);

        // Create configuration data
        Configuration newConfig = new Configuration();

        newConfig.id = getMaxId();
        newConfig.creationTime = System.nanoTime();
        newConfig.name = res.getString(R.string.default_config_name);
        newConfig.isFavourite = false;
        newConfig.master = master;

        return newConfig;
    }

    public ListLiveData<Configuration> getAllConfigurations() {
        if (mConfigs == null) {
            mConfigs = new ListLiveData<>();
        }

        return mConfigs;
    }

    private long getMaxId () {
        return 0;
    }
}
