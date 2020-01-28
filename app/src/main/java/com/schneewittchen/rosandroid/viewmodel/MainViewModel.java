package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroidlib.model.repos.ConfigRepository;
import com.schneewittchen.rosandroidlib.model.repos.ConfigRepositoryImpl;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class MainViewModel extends AndroidViewModel {


    ConfigRepository configRepo;
    MediatorLiveData<String> configTitle;


    public MainViewModel(@NonNull Application application) {
        super(application);

        this.init();
    }


    private void init() {
        configRepo = ConfigRepositoryImpl.getInstance();

        configTitle = new MediatorLiveData<>();
        configTitle.addSource(configRepo.getCurrentConfig(),
                configuration -> configTitle.setValue(configuration.name));

        // TODO: Remove Test
        android.os.Handler handler = new Handler();
        handler.postDelayed(() -> configRepo.createConfig(), 5000);
    }

    public LiveData<String> getConfigTitle() {
        return this.configTitle;
    }
}
