package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

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
 * @updated on 06.02.20
 * @modified by
 */
public class GridViewModel extends AndroidViewModel {

    private static final String TAG = GridViewModel.class.getCanonicalName();
    private ConfigRepository configRepository;

    private LiveData<List<WidgetEntity>> currentWidgets;


    public GridViewModel(@NonNull Application application) {
        super(application);

        configRepository = ConfigRepositoryImpl.getInstance(application);

        currentWidgets = Transformations.switchMap(configRepository.getCurrentConfigId(),
                configId -> configRepository.getWidgets(configId));
    }

    public LiveData<List<WidgetEntity>> getCurrentWidgets() {
        return this.currentWidgets;
    }

}
