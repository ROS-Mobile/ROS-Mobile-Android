package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

import java.util.ArrayList;
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

    public static MutableLiveData<List<Long>> selectedPath;
    private final RosDomain rosDomain;
    private MediatorLiveData<Boolean> widgetsEmpty;
    private BaseEntity lastDeletedWidget;


    public DetailsViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);

        if (selectedPath == null) {
            selectedPath = new MutableLiveData<>();
            selectedPath.setValue(new ArrayList<>());
        }
    }


    public void createWidget(String selectedText) {
        rosDomain.createWidget(getParentId(0), selectedText);
    }

    public void updateWidget(BaseEntity widget) {
        rosDomain.updateWidget(getParentId(1), widget);
    }

    public void deleteWidget(BaseEntity widget) {
        lastDeletedWidget = widget;
        rosDomain.deleteWidget(getParentId(0), widget);
    }

    public void restoreWidget() {
        if (lastDeletedWidget == null) return;
        rosDomain.addWidget(getParentId(0), lastDeletedWidget);
    }

    private Long getParentId(int branch) {
        Long parentId = null;
        List<Long> path = selectedPath.getValue();

        if (path.size() > branch) {
            parentId = path.get(0);
        }

        return parentId;
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
        return rosDomain.getTopicList();
    }

    public LiveData<BaseEntity> getWidget() {
        List<Long> path = selectedPath.getValue();
        return rosDomain.findWidget(path.get(0));
    }

    public void select(Long widgetId) {
        // Delete restore item
        lastDeletedWidget = null;

        // Nothing selected? Has to be root path.
        if (widgetId == null) {
            selectedPath.setValue(new ArrayList<>());
            return;
        }

        List<Long> path = selectedPath.getValue();
        assert path != null;

        path.add(widgetId);
        selectedPath.setValue(path);
    }

    public void popPath(int steps) {
        List<Long> path = selectedPath.getValue();
        assert path != null;

        for (int i = 0; i < steps; i++) {
            if (path.isEmpty()) return;
            path.remove(path.size() - 1);
        }

        selectedPath.setValue(path);
    }

    public LiveData<List<Long>> getWidgetPath() {
        return selectedPath;
    }
}
