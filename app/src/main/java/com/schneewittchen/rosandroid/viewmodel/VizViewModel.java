package com.schneewittchen.rosandroid.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepository;
import com.schneewittchen.rosandroid.model.repositories.ConfigRepositoryImpl;
import com.schneewittchen.rosandroid.model.repositories.RosRepo;
import com.schneewittchen.rosandroid.ui.custum_views.WidgetGroup;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.WidgetData;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;

import java.util.ArrayList;
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
public class VizViewModel extends AndroidViewModel {

    private static final String TAG = VizViewModel.class.getSimpleName();
    private ConfigRepository configRepository;
    private RosRepo rosRepo;
    private LiveData<List<BaseEntity>> currentWidgets;


    public VizViewModel(@NonNull Application application) {
        super(application);

        rosRepo = RosRepo.getInstance();
        configRepository = ConfigRepositoryImpl.getInstance(application);

        currentWidgets = Transformations.switchMap(configRepository.getCurrentConfigId(),
                configId -> configRepository.getWidgets(configId));
    }

    public void connectToRos() {
        // TODO: REAL CONNECT
        //rosRepo.setContext(this.getApplication());
        //rosRepo.setMasterAddress();
        //rosRepo.connectToMaster();
    }

    public LiveData<List<BaseEntity>> getCurrentWidgets() {
        return this.currentWidgets;
    }


    public void register(BaseEntity widget) {
        // TODO: Create node
        rosRepo.registerNode(widget);
    }

    public void unregister(BaseEntity widget) {
        rosRepo.unregisterNode(null);
    }

    public void reregister(BaseEntity widget) {
        rosRepo.unregisterNode(null);
        //rosRepo.registerNode(null);
    }

    public void informWidgetDataChange(WidgetData data) {
        rosRepo.informWidgetDataChange(data);
    }
}
