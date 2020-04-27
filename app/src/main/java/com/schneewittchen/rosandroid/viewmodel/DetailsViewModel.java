package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.repositories.WidgetModel;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.0
 * @created on 10.01.20
 * @updated on 27.04.20
 * @modified by Nils Rottmann
 */
public class DetailsViewModel extends AndroidViewModel {

    private static final String TAG = DetailsViewModel.class.getSimpleName();

    private RosDomain rosDomain;

    private MediatorLiveData<Boolean> widgetsEmpty;
    private BaseEntity lastDeletedWidget;


    public DetailsViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);
    }


    public void createWidget(String selectedText) {
        // TODO: Make generic
        if (selectedText.toLowerCase().equals("joystick")) {
            rosDomain.createWidget(WidgetEntity.JOYSTICK);
        } else if (selectedText.toLowerCase().equals("map")) {
            rosDomain.createWidget(WidgetEntity.MAP);
        } else if (selectedText.toLowerCase().equals("camera")) {
            rosDomain.createWidget(WidgetEntity.CAMERA);
        }
    }

    public void updateWidget(BaseEntity widget) {
        rosDomain.updateWidget(widget);
    }

    public void deleteWidget(BaseEntity widget) {
        lastDeletedWidget = widget;
        rosDomain.deleteWidget(widget);
    }

    public void restoreWidget() {
        rosDomain.addWidget(lastDeletedWidget);
    }

    public LiveData<List<BaseEntity>> getCurrentWidgets() {
        return rosDomain.getCurrentWidgets();
    }

    public int[] getAvailableWidgetNames() {
        return WidgetModel.getWidgetNames();
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
