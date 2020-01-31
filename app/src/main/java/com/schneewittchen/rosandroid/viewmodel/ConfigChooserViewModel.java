package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class ConfigChooserViewModel extends AndroidViewModel {

    ConfigRepository configRepo;


    public ConfigChooserViewModel(@NonNull Application application) {
        super(application);

        configRepo = ConfigRepositoryImpl.getInstance(application);
    }


    public void addConfig() {
        configRepo.createConfig();
    }

}
