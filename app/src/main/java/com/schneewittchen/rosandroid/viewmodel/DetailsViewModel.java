package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.model.repositories.WidgetModel;
import com.schneewittchen.rosandroid.widgets.test.BaseWidget;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.1.1
 * @created on 10.01.20
 * @updated on 15.05.20
 * @modified by Nico Studt
 * @updated on 24.09.20
 * @modified by Nico Studt
 */
public class DetailsViewModel extends AndroidViewModel {

    private static final String TAG = DetailsViewModel.class.getSimpleName();

    private RosDomain rosDomain;

    private MediatorLiveData<Boolean> widgetsEmpty;
    private BaseWidget lastDeletedWidget;


    public DetailsViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);
    }


    public void createWidget(String selectedText) {
        rosDomain.createWidget(selectedText);
    }

    public void updateWidget(BaseWidget widget) {
        rosDomain.updateWidget(widget);
    }

    public void deleteWidget(BaseWidget widget) {
        lastDeletedWidget = widget;
        rosDomain.deleteWidget(widget);
    }

    public void restoreWidget() {
        rosDomain.addWidget(lastDeletedWidget);
    }

    public LiveData<List<BaseWidget>> getCurrentWidgets() {
        return rosDomain.getCurrentWidgets();
    }

    public int getAvailableWidgetNames() {
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

    public List<Topic> getTopicList() {
        return rosDomain.getTopicList(); }
}
