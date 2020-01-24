package com.schneewittchen.rosandroid.viewmodel;

import android.database.Observable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.schneewittchen.rosandroid.model.WidgetModel;
import com.schneewittchen.rosandroidlib.widgets.model.JoystickWidget;
import com.schneewittchen.rosandroidlib.widgets.model.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class WidgetDetailsViewModel extends ViewModel {


    private MutableLiveData<List<Widget>> widgetList;
    private MediatorLiveData<Boolean> widgetsEmpty;


    public WidgetDetailsViewModel() {

        widgetList = new MutableLiveData<>(new ArrayList<>());
        widgetsEmpty = new MediatorLiveData<>();
        widgetsEmpty.addSource(widgetList, widgets -> widgetsEmpty.setValue(widgets.isEmpty()));
    }


    public int[] getAllWidgetNames() {
        return WidgetModel.getWidgetNames();
    }

    public void deleteWidget(Widget widget) {
        List<Widget> widgets = widgetList.getValue();
        widgets.remove(widget);
        widgetList.setValue(widgets);
    }

    public void addWidget(String selectedText) {
        if (selectedText.toLowerCase().equals("joystick")){
            List<Widget> widgets = widgetList.getValue();
            widgets.add(new JoystickWidget());
            widgetList.setValue(widgets);
        }
    }

    public LiveData<List<Widget>> getAllWidgets() {
        return widgetList;
    }

    public LiveData<Boolean> widgetsEmpty() {
        return widgetsEmpty;
    }
}
