package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroid.model.WidgetModel;
import com.schneewittchen.rosandroidlib.model.entities.Configuration;
import com.schneewittchen.rosandroidlib.model.entities.Widget;
import com.schneewittchen.rosandroidlib.model.entities.WidgetJoystick;
import com.schneewittchen.rosandroidlib.model.repos.ConfigRepository;
import com.schneewittchen.rosandroidlib.model.repos.ConfigRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 10.01.20
 * @updated on 28.01.20
 * @modified by
 */
public class WidgetDetailsViewModel extends AndroidViewModel {


    private ConfigRepository configRepository;
    private MediatorLiveData<List<Widget>> widgetList;
    private MediatorLiveData<Boolean> widgetsEmpty;
    private LiveData<Configuration> mConfig;


    public WidgetDetailsViewModel(@NonNull Application application) {
        super(application);

        configRepository = ConfigRepositoryImpl.getInstance();

        mConfig = configRepository.getCurrentConfig();

        widgetList = new MediatorLiveData<>();
        widgetList.setValue(new ArrayList<>());
        widgetList.addSource(mConfig, configuration -> {
            System.out.println(configuration.widgets.size());
            widgetList.setValue(configuration.widgets);
        });

        widgetsEmpty = new MediatorLiveData<>();
        widgetsEmpty.addSource(widgetList, widgets -> widgetsEmpty.setValue(widgets.isEmpty()));
    }


    public int[] getAllWidgetNames() {
        return WidgetModel.getWidgetNames();
    }

    public void deleteWidget(Widget widget) {
    }

    public void addWidget(String selectedText) {
        if (selectedText.toLowerCase().equals("joystick")){
            configRepository.setWidget(new WidgetJoystick(), 0);
        }
    }

    public LiveData<List<Widget>> getAllWidgets() {
        return widgetList;
    }

    public LiveData<Boolean> widgetsEmpty() {
        return widgetsEmpty;
    }
}
