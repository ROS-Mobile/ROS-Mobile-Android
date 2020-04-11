package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 10.01.20
 * @updated on 05.02.20
 * @modified by
 */
public class ConfigurationsViewModel extends AndroidViewModel {

    private ConfigRepository configRepository;
    private LiveData<List<ConfigEntity>> lastOpenedConfigs;
    private LiveData<ConfigEntity> currentConfig;
    private MediatorLiveData<String> currentConfigTitle;


    public ConfigurationsViewModel(@NonNull Application application) {
        super(application);

        configRepository = ConfigRepositoryImpl.getInstance(application);

        lastOpenedConfigs = configRepository.getAllConfigs();
        currentConfigTitle = new MediatorLiveData<>();

        currentConfig = Transformations.switchMap(configRepository.getCurrentConfigId(),
                configId -> configRepository.getConfig(configId));


        currentConfigTitle.addSource(currentConfig, configuration -> {
            if (configuration != null) {
                currentConfigTitle.postValue(configuration.name);
            }
        });
    }


    public LiveData<List<ConfigEntity>> getLastOpenedConfigs() {
        return this.lastOpenedConfigs;
    }

    public void addConfig() {
        configRepository.createConfig();
    }

    public void chooseConfig(long configId) {
        configRepository.chooseConfig(configId);
    }

    public LiveData<String> getConfigTitle() {
        return currentConfigTitle;
    }
}
