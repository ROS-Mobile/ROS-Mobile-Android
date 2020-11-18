package com.schneewittchen.rosandroid.model.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.db.DataStorage;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.SSHEntity;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.LambdaTask;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;

import java.util.List;
import java.util.Locale;


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

    private static ConfigRepositoryImpl mInstance;

    private final DataStorage mDataStorage;
    private final MediatorLiveData<Long> mCurrentConfigId;


    private ConfigRepositoryImpl(Application application){
        mDataStorage = DataStorage.getInstance(application);

        mCurrentConfigId = new MediatorLiveData<>();
        mCurrentConfigId.addSource(mDataStorage.getLatestConfig(), config -> {
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
    public void createConfig(String name) {
        ConfigEntity config = new ConfigEntity();
        config.creationTime = System.currentTimeMillis();
        config.lastUsed = config.creationTime;

        if (name != null) {
            config.name = name;
        }

        mDataStorage.addConfig(config);

        new LambdaTask(() -> {
            long configId = mDataStorage.getLatestConfigDirect().id;

            // Create new master connection
            MasterEntity master = new MasterEntity();
            master.configId = configId;
            mDataStorage.addMaster(master);

            // Create new ssh connection
            SSHEntity sshEntity = new SSHEntity();
            sshEntity.configId = configId;
            mDataStorage.addSSH(sshEntity);
        }).execute();
    }

    @Override
    public void removeConfig(long configId) {
        mDataStorage.deleteConfig(configId);
        mDataStorage.deleteMaster(configId);
        mDataStorage.deleteSSH(configId);
    }


    @Override
    public void updateConfig(ConfigEntity config) {
        mDataStorage.updateConfig(config);
    }

    @Override
    public LiveData<Long> getCurrentConfigId() {
        return mCurrentConfigId;
    }

    @Override
    public LiveData<ConfigEntity> getConfig(long id) {
        return mDataStorage.getConfig(id);
    }

    @Override
    public LiveData<ConfigEntity> getCurrentConfig() {
        return Transformations.switchMap(mCurrentConfigId, id ->
                mDataStorage.getConfig(id));
    }

    @Override
    public LiveData<List<ConfigEntity>> getAllConfigs() {
        return mDataStorage.getAllConfigs();
    }


    // WIDGETS -------------------------------------------------------------------------------------

    @Override
    public void createWidget(String widgetType) {
        // Create actual widget object
        String classPath = String.format(Constants.ENTITY_FORMAT, widgetType.toLowerCase(), widgetType);
        Object object = Utils.getObjectFromClassName(classPath);

        if (!(object instanceof BaseEntity)) {
            Log.i(TAG, "Widget can not be created from: " + classPath);
            return;
        }

        BaseEntity widget = (BaseEntity) object;
        long configId = mCurrentConfigId.getValue();

        String widgetName = "";
        for (int count = 1; count > 0; count++) {
            widgetName = String.format(Locale.ENGLISH, Constants.WIDGET_NAMING, widgetType, count);

            if (!mDataStorage.widgetNameExists(configId, widgetName)) {
                break;
            }
        }

        widget.configId = configId;
        widget.creationTime = System.currentTimeMillis();
        widget.name = widgetName;
        widget.type = widgetType;

        mDataStorage.addWidget(widget);
        Log.i(TAG, "Widget added to database: " + widget);

    }

    @Override
    public void addWidget(BaseEntity widget) {
        mDataStorage.addWidget(widget);
    }

    @Override
    public void updateWidget(BaseEntity widget) {
        mDataStorage.updateWidget(widget);
    }

    @Override
    public void deleteWidget(BaseEntity widget) {
        mDataStorage.deleteWidget(widget);

        Log.i(TAG, "Widget deleted");
    }

    @Override
    public LiveData<List<BaseEntity>> getWidgets(long id) {
        return mDataStorage.getWidgets(id);
    }


    // Masters -------------------------------------------------------------------------------------

    @Override
    public void updateMaster(MasterEntity master) {
        mDataStorage.updateMaster(master);
    }

    @Override
    public LiveData<MasterEntity> getMaster(long configId) {
        return mDataStorage.getMaster(configId);
    }

    // SSH -------------------------------------------------------------------------------------

    @Override
    public void setSSH(SSHEntity ssh, String configId) {
        ssh.ip = configId;
    }

    @Override
    public void updateSSH(SSHEntity ssh) {
        mDataStorage.updateSSH(ssh);
    }

    @Override
    public LiveData<SSHEntity> getSSH(long configId) {
        return mDataStorage.getSSH(configId);
    }
}
