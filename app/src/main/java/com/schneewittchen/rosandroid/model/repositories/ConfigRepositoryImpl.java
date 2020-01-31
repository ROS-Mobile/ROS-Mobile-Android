package com.schneewittchen.rosandroid.model.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.schneewittchen.rosandroid.model.db.ConfigDatabase;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.4
 * @created on 26.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class ConfigRepositoryImpl implements ConfigRepository {

    private static ConfigRepositoryImpl mInstance;

    private ConfigDatabase mConfigDatabase;
    private ConfigModel mConfigModel;
    private MutableLiveData<Long> mCurrentConfigId;
    private LiveData<List<ConfigEntity>> mAllConfigs;
    private MediatorLiveData<ConfigEntity> mCurrentConfig;


    private ConfigRepositoryImpl(Application application){
        mConfigDatabase = ConfigDatabase.getInstance(application);
        mConfigModel = ConfigModel.getInstance(application);

        mAllConfigs = mConfigDatabase.getAllConfigs();
        mCurrentConfig = new MediatorLiveData<>();
        mCurrentConfigId = new MutableLiveData<>();

        mCurrentConfig.addSource(mCurrentConfigId, id -> {
            if (mAllConfigs.getValue() == null){
                return;
            }

            for (ConfigEntity configuration: mAllConfigs.getValue()) {
                if (configuration.id == id) {
                    mCurrentConfig.setValue(configuration);
                }
            }
        });

        mCurrentConfig.addSource(mAllConfigs, configs -> {
            if (configs == null || mCurrentConfigId.getValue() == null){
                return;
            }

            long id = mCurrentConfigId.getValue();

            for (ConfigEntity configuration: configs) {
                if (configuration.id == id) {
                    mCurrentConfig.setValue(configuration);
                }
            }
        });

        mCurrentConfigId.setValue(0L);
    }


    public static ConfigRepositoryImpl getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new ConfigRepositoryImpl(application);
        }

        return mInstance;
    }


    @Override
    public void chooseConfig(long configId) {

    }

    @Override
    public void createConfig(Context context) {

    }

    @Override
    public void createConfig() {
        ConfigEntity config = mConfigModel.getNewConfig();
        mConfigDatabase.insertCompleteConfig(config);
    }

    @Override
    public void removeConfig(long configId) {

    }

    @Override
    public void addConfig(ConfigEntity config) {
    }

    @Override
    public void setMaster(MasterEntity master, long configId) {

    }

    @Override
    public void setConfig(ConfigEntity config, long configId) {

    }

    @Override
    public void setWidget(WidgetEntity widget, long configId) {
        ConfigEntity config = mCurrentConfig.getValue();
        config.widgets.add(widget);
        mCurrentConfig.setValue(config);
    }

    @Override
    public void deleteWidget(WidgetEntity widget) {
        ConfigEntity config = mCurrentConfig.getValue();
        config.widgets.remove(widget);
        mCurrentConfig.setValue(config);
    }


    @Override
    public LiveData<ConfigEntity> getCurrentConfig() {
        return mConfigDatabase.getLatestConfig();
    }

    @Override
    public ConfigEntity getNewConfig() {
        return null;
    }

    @Override
    public LiveData<List<ConfigEntity>> getAllConfigs() {
        return mConfigDatabase.getAllConfigs();
    }


    @Override
    public LiveData<MasterEntity> getMasterOfConfig(long configId) {
        return mConfigDatabase.getMaster(configId);
    }
}
