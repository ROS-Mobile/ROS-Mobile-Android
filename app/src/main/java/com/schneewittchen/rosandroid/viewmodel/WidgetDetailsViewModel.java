package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.repositories.WidgetModel;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.6
 * @created on 10.01.20
 * @updated on 16.02.20
 * @modified by
 */
public class WidgetDetailsViewModel extends AndroidViewModel {

    private static final String TAG = WidgetDetailsViewModel.class.getCanonicalName();
    private ConfigRepository configRepository;

    private LiveData<List<BaseEntity>> currentWidgets;
    private MediatorLiveData<Boolean> widgetsEmpty;
    private BaseEntity lastDeletedWidget;


    public WidgetDetailsViewModel(@NonNull Application application) {
        super(application);

        configRepository = ConfigRepositoryImpl.getInstance(application);

        currentWidgets = Transformations.switchMap(configRepository.getCurrentConfigId(),
                configId -> configRepository.getWidgets(configId));
    }


    public int[] getAvailableWidgetNames() {
        return WidgetModel.getWidgetNames();
    }

    public void updateWidget(BaseEntity widget) {
        configRepository.updateWidget(widget);
    }

    public void deleteWidget(BaseEntity widget) {
        lastDeletedWidget = widget;
        configRepository.deleteWidget(widget);
    }

    public void restoreWidget() {
        configRepository.addWidget(lastDeletedWidget);
    }

    public void createWidget(String selectedText) {
        // TODO: Make generic
        if (selectedText.toLowerCase().equals("joystick")){
            configRepository.createWidget(WidgetEntity.JOYSTICK);
        }else if (selectedText.toLowerCase().equals("map")){
            configRepository.createWidget(WidgetEntity.MAP);
        }
    }

    public LiveData<List<BaseEntity>> getCurrentWidgets() {
        return this.currentWidgets;
    }

    public LiveData<Boolean> widgetsEmpty() {
        if (widgetsEmpty == null) {
            widgetsEmpty = new MediatorLiveData<>();

            widgetsEmpty.addSource(getCurrentWidgets(), widgets ->
                    widgetsEmpty.postValue(widgets.isEmpty()));
        }

        return widgetsEmpty;
    }
}
