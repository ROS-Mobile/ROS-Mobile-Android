package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.RosData;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 10.01.20
 * @updated on 21.04.20
 * @modified by Nils Rottmann
 */
public class VizViewModel extends AndroidViewModel {

    private static final String TAG = VizViewModel.class.getSimpleName();

    private final RosDomain rosDomain;


    public VizViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);
    }

    public void updateWidget(BaseEntity widget) {
        rosDomain.updateWidget(null, widget);
    }

    public LiveData<List<BaseEntity>> getCurrentWidgets() {
        return rosDomain.getCurrentWidgets();
    }

    public LiveData<RosData> getData() {
        return this.rosDomain.getData();
    }


    public void publishData(BaseData data) {
        rosDomain.publishData(data);
    }
}
