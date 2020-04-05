package com.schneewittchen.rosandroid.model.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 26.01.20
 * @updated on 31.01.20
 * @modified by
 */
public interface ConfigRepository {

    void chooseConfig(long configId);

    void createConfig(Context context);

    void createConfig();

    void removeConfig(long configId);

    void addConfig(ConfigEntity config);

    void setMaster(MasterEntity master, long configId);

    void setConfig(ConfigEntity config, long configId);



    ConfigEntity getNewConfig();

    LiveData<List<ConfigEntity>> getAllConfigs();

    LiveData<Long> getCurrentConfigId();

    LiveData<ConfigEntity> getConfig(long id);

    LiveData<ConfigEntity> getCurrentConfig();

    LiveData<MasterEntity> getMaster(long configId);

    LiveData<List<BaseEntity>> getWidgets(long id);


    void createWidget(int widgetType);

    void deleteWidget(BaseEntity widget);

    void addWidget(BaseEntity widget);

    void updateWidget(BaseEntity widget);
}
