package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroid.model.repositories.WidgetModel;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetJoystickEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.4
 * @created on 10.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class WidgetDetailsViewModel extends AndroidViewModel {


    private ConfigRepository configRepository;
    private MediatorLiveData<List<WidgetEntity>> widgetList;
    private MediatorLiveData<Boolean> widgetsEmpty;
    private LiveData<ConfigEntity> mConfig;


    public WidgetDetailsViewModel(@NonNull Application application) {
        super(application);

        configRepository = ConfigRepositoryImpl.getInstance(application);

        mConfig = configRepository.getCurrentConfig();

        widgetList = new MediatorLiveData<>();
        widgetList.setValue(new ArrayList<>());
        widgetList.addSource(mConfig, configuration -> {
            if (configuration.widgets == null) {
                return;
            }
            System.out.println(configuration.widgets.size());
            widgetList.setValue(configuration.widgets);
        });

        widgetsEmpty = new MediatorLiveData<>();
        widgetsEmpty.addSource(widgetList, widgets -> widgetsEmpty.setValue(widgets.isEmpty()));
    }


    public int[] getAvailableWidgetNames() {
        return WidgetModel.getWidgetNames();
    }

    public void deleteWidget(WidgetEntity widget) {
        configRepository.deleteWidget(widget);
    }

    public void addWidget(String selectedText) {
        if (selectedText.toLowerCase().equals("joystick")){
            configRepository.setWidget(new WidgetJoystickEntity(), 0);
        }
        /*else if (selectedText.toLowerCase().equals("grid map")){
            configRepository.setWidget(new WidgetGridMap(), 0);
        }*/
    }

    public LiveData<List<WidgetEntity>> getAllWidgets() {
        return widgetList;
    }

    public LiveData<Boolean> widgetsEmpty() {
        return widgetsEmpty;
    }
}
