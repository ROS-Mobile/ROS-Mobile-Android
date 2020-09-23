package com.schneewittchen.rosandroid.model.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.db.ConfigDatabase;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.SSHEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetCountEntity;
import com.schneewittchen.rosandroid.widgets.test.BaseWidget;

import java.lang.reflect.Constructor;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.7
 * @created on 26.01.20
 * @updated on 20.05.20
 * @modified by Nico Studt
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 * @updated on 23.09.20
 * @modified by Nico Studt
 */
public class ConfigRepositoryImpl implements ConfigRepository {

    private static final String TAG = ConfigRepositoryImpl.class.getSimpleName();
    private static final String WIDGET_PREFIX = "com.schneewittchen.rosandroid.widgets.";

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
    public void createFirstConfig(String name) {
        ConfigEntity config = mConfigModel.getNewConfig();
        config.name = name;
        mConfigDatabase.addConfig(config);
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

        // TODO: Load widget count from widget_count_dao and extend name
        long indexCurrentWidget = 0;
        WidgetCountEntity widgetCount = mConfigDatabase.getWidgetCount(mCurrentConfigId.getValue(), widgetType);
        if(widgetCount != null) {
            indexCurrentWidget = widgetCount.count;
        }

        String className = WIDGET_PREFIX + widgetType.toLowerCase() + ".Widget" + widgetType + "Entity";

        try {
            Class<?> subclass = Class.forName(className);
            Constructor<?> constructor = subclass.getConstructor();
            BaseWidget widget = (BaseWidget) constructor.newInstance();

            widget.configId = mCurrentConfigId.getValue();
            widget.creationTime = System.currentTimeMillis();
            widget.name = widgetType + " " + indexCurrentWidget;

            mConfigDatabase.addWidget(widget);
            Log.i(TAG, "Widget added to database: " + widget);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addWidget(BaseWidget widget) {
        mConfigDatabase.addWidget(widget);
    }

    @Override
    public void updateWidget(BaseWidget widget) {
        mConfigDatabase.updateWidget(widget);
    }

    @Override
    public void deleteWidget(BaseWidget widget) {
        mConfigDatabase.deleteWidget(widget);

        Log.i(TAG, "Widget deleted");
    }

    @Override
    public LiveData<List<BaseWidget>> getWidgets(long id) {
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
