package com.schneewittchen.rosandroidlib.model.repos;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.schneewittchen.rosandroidlib.model.ConfigModel;
import com.schneewittchen.rosandroidlib.model.entities.Configuration;
import com.schneewittchen.rosandroidlib.model.entities.Master;
import com.schneewittchen.rosandroidlib.model.entities.Widget;
import com.schneewittchen.rosandroidlib.utility.ListLiveData;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 26.01.20
 * @updated on 28.01.20
 * @modified by
 */
public class ConfigRepositoryImpl implements ConfigRepository {

    private static ConfigRepositoryImpl mInstance;

    private ConfigModel mConfigModel;
    private MutableLiveData<Long> mCurrentConfigId;
    private ListLiveData<Configuration> mAllConfigs;
    private MediatorLiveData<Configuration> mCurrentConfig;


    private ConfigRepositoryImpl(){
        mConfigModel = ConfigModel.getInstance();

        mAllConfigs = mConfigModel.getAllConfigurations();
        mCurrentConfig = new MediatorLiveData<>();
        mCurrentConfigId = new MutableLiveData<>();

        mCurrentConfig.addSource(mCurrentConfigId, id -> {
            if (mAllConfigs.getValue() == null){
                return;
            }

            for (Configuration configuration: mAllConfigs.getValue()) {
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

            for (Configuration configuration: configs) {
                if (configuration.id == id) {
                    mCurrentConfig.setValue(configuration);
                }
            }
        });

        mCurrentConfigId.setValue(0L);
    }


    public static ConfigRepositoryImpl getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigRepositoryImpl();
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
        Configuration config = mConfigModel.getNewConfig();
        mConfigModel.addConfiguration(config);
    }

    @Override
    public void removeConfig(long configId) {

    }

    @Override
    public void addConfig(Configuration config) {
    }

    @Override
    public void setMaster(Master master, long configId) {

    }

    @Override
    public void setConfig(Configuration config, long configId) {

    }

    @Override
    public void setWidget(Widget widget, long configId) {
        Configuration config = mCurrentConfig.getValue();
        config.widgets.add(widget);
        mCurrentConfig.setValue(config);
    }

    @Override
    public void deleteWidget(Widget widget) {
        Configuration config = mCurrentConfig.getValue();
        config.widgets.remove(widget);
        mCurrentConfig.setValue(config);
    }

    @Override
    public LiveData<Configuration> getCurrentConfig() {
        return mCurrentConfig;
    }

    @Override
    public Configuration getNewConfig() {
        return null;
    }

    @Override
    public LiveData<List<Configuration>> getAllConfigs() {
        return mConfigModel.getAllConfigurations();
    }
}
