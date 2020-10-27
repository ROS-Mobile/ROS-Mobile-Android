package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 10.01.20
 * @updated on 15.05.20
 * @modified by Nico Studt
 */
public class ConfigurationsViewModel extends AndroidViewModel {

    private static final String TAG = ConfigurationsViewModel.class.getSimpleName();

    private final RosDomain rosDomain;
    private final ConfigRepository configRepository;
    private MediatorLiveData<String> currentConfigTitle;
    private LiveData<ConfigEntity> currentConfig;
    private MediatorLiveData<List<String>> lastOpenedConfigNames;
    private MediatorLiveData<List<String>> favoriteConfigNames;
    private LiveData<List<ConfigEntity>> configList;


    public ConfigurationsViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);
        configRepository = ConfigRepositoryImpl.getInstance(application);
        this.initListeners();
    }


    private void initListeners() {
        configList = configRepository.getAllConfigs();
        currentConfig = configRepository.getCurrentConfig();

        currentConfigTitle = new MediatorLiveData<>();
        currentConfigTitle.addSource(currentConfig, configuration -> {
            if (configuration != null) {
                currentConfigTitle.postValue(configuration.name);
            }else {
                currentConfigTitle.postValue(null);
            }
        });

        // Sort config comparators
        Comparator<ConfigEntity> compareByLastOpened = (ConfigEntity c1, ConfigEntity c2) ->
                Long.compare(c2.creationTime, c1.creationTime);

        lastOpenedConfigNames = new MediatorLiveData<>();
        lastOpenedConfigNames.addSource(configList, configEntities -> {
            Collections.sort(configEntities, compareByLastOpened);
            List<String> nameList = new ArrayList<>();

            for (ConfigEntity configEntity: configEntities) {
                nameList.add(configEntity.name);
            }

            lastOpenedConfigNames.postValue(nameList);
        });

        favoriteConfigNames = new MediatorLiveData<>();
        favoriteConfigNames.addSource(configList, configEntities -> {
            List<String> nameList = new ArrayList<>();

            for (ConfigEntity configEntity: configEntities) {
                if (configEntity.isFavourite) {
                    nameList.add(configEntity.name);
                }
            }

            favoriteConfigNames.postValue(nameList);
        });
    }

    public void renameConfig(String newName) {
        if (currentConfig.getValue() == null) {
            return;
        }

        newName = newName.trim();

        ConfigEntity config = currentConfig.getValue();
        config.name = newName;
        configRepository.updateConfig(config);
    }

    public void deleteConfig() {
        if (currentConfig.getValue() == null) {
            return;
        }

        Log.i(TAG, "Delete current config");
        configRepository.removeConfig(currentConfig.getValue().id);
    }

    public void addConfig() {
        configRepository.createConfig(null);
    }

    public void chooseConfig(String configName) {
        if(configList.getValue() == null)
            return;

        for (ConfigEntity config: configList.getValue()) {
            if (config.name.equals(configName)) {
                rosDomain.disconnectFromMaster();
                configRepository.chooseConfig(config.id);
                return;
            }
        }
    }

    public LiveData<String> getConfigTitle() {
        return currentConfigTitle;
    }

    public LiveData<List<String>> getLastOpenedConfigNames() {
        return this.lastOpenedConfigNames;
    }

    public LiveData<List<String>> getFavoriteConfigNames() {
        return this.favoriteConfigNames;
    }


}
