package com.schneewittchen.rosandroid.domain;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;
import com.schneewittchen.rosandroid.model.repositories.ConnectionType;
import com.schneewittchen.rosandroid.model.repositories.RosRepository;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 07.04.20
 * @updated on 15.04.20
 * @modified by Nico Studt
 */
public class RosDomain {

    private static final String TAG = RosDomain.class.getSimpleName();

    // Singleton instance
    private static RosDomain mInstance;

    // Repositories
    private ConfigRepository configRepository;
    private RosRepository rosRepo;

    // Data objects
    private LiveData<List<BaseEntity>> currentWidgets;
    private LiveData<MasterEntity> currentMaster;


    private RosDomain(@NonNull Application application) {
        this.rosRepo = RosRepository.getInstance(application);
        this.configRepository = ConfigRepositoryImpl.getInstance(application);

        // React on config change and get the new data
        currentWidgets = Transformations.switchMap(configRepository.getCurrentConfigId(),
                configId -> configRepository.getWidgets(configId));

        currentMaster = Transformations.switchMap(configRepository.getCurrentConfigId(),
                configId -> configRepository.getMaster(configId));

        currentWidgets.observeForever(widgets -> rosRepo.updateWidgets(widgets));
        currentMaster.observeForever(master -> {
            Log.i(TAG, "Update master " + master);
            rosRepo.updateMaster(master);
        });
    }


    public static RosDomain getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new RosDomain(application);
        }

        return mInstance;
    }


    public void informWidgetDataChange(BaseData data) {
        rosRepo.informWidgetDataChange(data);
    }

    public void createWidget(int widgetType) {
        configRepository.createWidget(widgetType);
    }

    public void addWidget(BaseEntity widget) {
        configRepository.addWidget(widget);
    }

    public void updateWidget(BaseEntity widget) {
        configRepository.updateWidget(widget);
    }

    public void deleteWidget(BaseEntity widget) {
        configRepository.deleteWidget(widget);
    }

    public LiveData<List<BaseEntity>> getCurrentWidgets() {
        return this.currentWidgets;
    }


    public void updateMaster(MasterEntity master) {
        configRepository.updateMaster(master);
    }

    public void connectToMaster() {
        rosRepo.connectToMaster();
    }

    public void disconnectFromMaster() {
        rosRepo.disconnectFromMaster();
    }

    public LiveData<MasterEntity> getCurrentMaster() {
        return this.currentMaster;
    }

    public LiveData<ConnectionType> getRosConnection() {
        return rosRepo.getRosConnectionStatus();
    }
}
