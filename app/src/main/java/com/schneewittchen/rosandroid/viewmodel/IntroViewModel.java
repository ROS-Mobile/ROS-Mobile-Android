package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;

/**
 * TODO: Description
 *
 * @author Nils Rottmann
 * @version 1.0.0
 * @created on 22.06.20
 * @updated on
 * @modified by
 */

public class IntroViewModel extends AndroidViewModel {

    private static final String TAG = IntroViewModel.class.getSimpleName();

    ConfigRepository configRepo;
    private LiveData<ConfigEntity> currentConfig;

    public IntroViewModel(@NonNull Application application) {
        super(application);
        configRepo = ConfigRepositoryImpl.getInstance(getApplication());
    }

    public void setConfigName(String configName) {
        ConfigEntity config = currentConfig.getValue();
        config.name = configName;
        configRepo.updateConfig(config);
    }
}
