package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.model.entities.BaseEntity;

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

    private final RosDomain rosDomain;

    private MediatorLiveData<Boolean> widgetsEmpty;
    private BaseEntity lastDeletedWidget;


    public DetailsViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);
    }


    public void createWidget(String selectedText) {
        rosDomain.createWidget(selectedText);
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
