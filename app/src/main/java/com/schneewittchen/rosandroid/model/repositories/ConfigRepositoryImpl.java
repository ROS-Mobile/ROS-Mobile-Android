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
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.gridmap.WidgetGridMapEntity;
import com.schneewittchen.rosandroid.widgets.joystick.WidgetJoystickEntity;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.5
 * @created on 26.01.20
 * @updated on 05.02.20
 * @modified by
 */
public class ConfigRepositoryImpl implements ConfigRepository {

    private static final String TAG = ConfigRepositoryImpl.class.getCanonicalName();
    private static ConfigRepositoryImpl mInstance;

    private ConfigDatabase mConfigDatabase;
    private ConfigModel mConfigModel;
    private MediatorLiveData<Long> mCurrentConfigId;


    private ConfigRepositoryImpl(Application application){
        mConfigDatabase = ConfigDatabase.getInstance(application);
        mConfigModel = ConfigModel.getInstance(application);

        mCurrentConfigId = new MediatorLiveData<>();
        mCurrentConfigId.addSource(mConfigDatabase.getLatestConfig(), config -> {
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
        // TODO: switch config return value id from config at insert
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
    public void createWidget(int widgetType) {
        // TODO: Make generic
        if (mCurrentConfigId.getValue() == null) {
            return;
        }

        BaseEntity widget;

        switch (widgetType) {
            case WidgetEntity.JOYSTICK:
                widget = new WidgetJoystickEntity();
                break;

            case WidgetEntity.MAP:
                widget = new WidgetGridMapEntity();
                break;

            default:
                return;
        }

        widget.configId = mCurrentConfigId.getValue();
        widget.creationTime = System.nanoTime();
        widget.name = widget.getName();
        widget.posX = 0;
        widget.posY = 0;
        widget.width = 1;
        widget.height = 1;

        mConfigDatabase.addWidget(widget);
        Log.i(TAG, "Widget added to database: " + widget);
    }

    @Override
    public void deleteWidget(BaseEntity widget) {
        mConfigDatabase.deleteWidget(widget);

        Log.i(TAG, "Widget deleted");
    }

    @Override
    public void addWidget(BaseEntity widget) {
        mConfigDatabase.addWidget(widget);
    }

    @Override
    public void updateWidget(BaseEntity widget) {
        mConfigDatabase.updataWidget(widget);
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

    @Override
    public LiveData<MasterEntity> getMaster(long configId) {
        return mConfigDatabase.getMaster(configId);
    }

    @Override
    public LiveData<List<BaseEntity>> getWidgets(long id) {
        return mConfigDatabase.getWidgets(id);
    }
}
