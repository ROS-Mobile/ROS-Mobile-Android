package com.schneewittchen.rosandroid.model.repositories;

import android.app.Application;
import android.content.Context;
import android.icu.text.SymbolTable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.db.ConfigDatabase;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.SSHEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.lang.reflect.Constructor;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.6
 * @created on 26.01.20
 * @updated on 20.05.20
 * @modified by Nico Studt
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 */
public class ConfigRepositoryImpl implements ConfigRepository {

    private static final String TAG = ConfigRepositoryImpl.class.getSimpleName();
    private static ConfigRepositoryImpl mInstance;

    private ConfigDatabase mConfigDatabase;
    private ConfigModel mConfigModel;
    private MediatorLiveData<Long> mCurrentConfigId;


    private ConfigRepositoryImpl(Application application){
        mConfigDatabase = ConfigDatabase.getInstance(application);
        mConfigModel = ConfigModel.getInstance(application);

        mCurrentConfigId = new MediatorLiveData<>();
        mCurrentConfigId.addSource(mConfigDatabase.getLatestConfig(), config -> {
            Log.i(TAG, "New Config: " + config);

            if(config != null)
                mCurrentConfigId.postValue(config.id);
        });
    }


    public static ConfigRepositoryImpl getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new ConfigRepositoryImpl(application);
        }

        return mInstance;
    }


    // CONFIGS -------------------------------------------------------------------------------------

    @Override
    public void chooseConfig(long configId) {
        if(mCurrentConfigId.getValue() == null || mCurrentConfigId.getValue() != configId){
            mCurrentConfigId.postValue(configId);
        }
    }

    @Override
    public void createConfig(Context context) {

    }

    @Override
    public void createConfig() {
        ConfigEntity config = mConfigModel.getNewConfig();
        mConfigDatabase.addConfig(config);
    }

    @Override
    public void removeConfig(long configId) {
        mConfigDatabase.deleteConfig(configId);
    }

    @Override
    public void addConfig(ConfigEntity config) {
    }

    @Override
    public void setConfig(ConfigEntity config, String configId) {

    }

    @Override
    public void updateConfig(ConfigEntity config) {
        mConfigDatabase.updateConfig(config);
    }

    @Override
    public LiveData<Long> getCurrentConfigId() {
        return mCurrentConfigId;
    }

    @Override
    public LiveData<ConfigEntity> getConfig(long id) {
        return mConfigDatabase.getConfig(id);
    }

    @Override
    public LiveData<ConfigEntity> getCurrentConfig() {
        return Transformations.switchMap(mCurrentConfigId, id ->
                mConfigDatabase.getConfig(id));
    }

    @Override
    public ConfigEntity getNewConfig() {
        return null;
    }

    @Override
    public LiveData<List<ConfigEntity>> getAllConfigs() {
        return mConfigDatabase.getAllConfigs();
    }


    // WIDGETS -------------------------------------------------------------------------------------

    @Override
    public void createWidget(String widgetType) {
        if (mCurrentConfigId.getValue() == null) {
            return;
        }

        String prefix = "com.schneewittchen.rosandroid.widgets.";
        String className = prefix + widgetType.toLowerCase() + ".Widget" + widgetType + "Entity";

        try {
            Class<?> subclass = Class.forName(className);
            Constructor<?> ctor = subclass.getConstructor();
            BaseEntity widget = (BaseEntity) ctor.newInstance();

            widget.configId = mCurrentConfigId.getValue();
            widget.creationTime = System.nanoTime();
            widget.name = widget.getName(); //+ Integer.toString(mConfigDatabase.getLatestConfig().getValue().widgetCount);

            mConfigDatabase.addWidget(widget);
            Log.i(TAG, "Widget added to database: " + widget);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addWidget(BaseEntity widget) {
        mConfigDatabase.addWidget(widget);
    }

    @Override
    public void updateWidget(BaseEntity widget) {
        mConfigDatabase.updateWidget(widget);
    }

    @Override
    public void deleteWidget(BaseEntity widget) {
        mConfigDatabase.deleteWidget(widget);

        Log.i(TAG, "Widget deleted");
    }

    @Override
    public LiveData<List<BaseEntity>> getWidgets(long id) {
        return mConfigDatabase.getWidgets(id);
    }


    // Masters -------------------------------------------------------------------------------------

    @Override
    public void setMaster(MasterEntity master, String configId) {
        master.ip = configId;
    }

    @Override
    public void updateMaster(MasterEntity master) {
        mConfigDatabase.updateMaster(master);
    }

    @Override
    public LiveData<MasterEntity> getMaster(long configId) {
        return mConfigDatabase.getMaster(configId);
    }

    // SSH -------------------------------------------------------------------------------------

    @Override
    public void setSSH(SSHEntity ssh, String configId) {
        ssh.ip = configId;
    }

    @Override
    public void updateSSH(SSHEntity ssh) {
        mConfigDatabase.updateSSH(ssh);
    }

    @Override
    public LiveData<SSHEntity> getSSH(long configId) {
        return mConfigDatabase.getSSH(configId);
    }
}
