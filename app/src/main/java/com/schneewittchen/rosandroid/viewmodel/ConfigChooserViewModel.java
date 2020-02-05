package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
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
public class ConfigChooserViewModel extends AndroidViewModel {

    private ConfigRepository configRepo;
    private LiveData<List<ConfigEntity>> lastOpenedConfigs;


    public ConfigChooserViewModel(@NonNull Application application) {
        super(application);

        configRepo = ConfigRepositoryImpl.getInstance(application);

        lastOpenedConfigs = configRepo.getAllConfigs();
    }


    public LiveData<List<ConfigEntity>> getLastOpenedConfigs() {
        return this.lastOpenedConfigs;
    }

    public void addConfig() {
        configRepo.createConfig();
    }

    public void chooseConfig(long configId) {
        configRepo.chooseConfig(configId);
    }
}
