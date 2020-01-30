package com.schneewittchen.rosandroidlib.model.repos;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroidlib.model.entities.Configuration;
import com.schneewittchen.rosandroidlib.model.entities.Master;
import com.schneewittchen.rosandroidlib.model.entities.Widget;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 26.01.20
 * @updated on 26.01.20
 * @modified by
 */
public interface ConfigRepository {

    void chooseConfig(long configId);

    void createConfig(Context context);

    void createConfig();

    void removeConfig(long configId);

    void addConfig(Configuration config);

    void setMaster(Master master, long configId);

    void setConfig(Configuration config, long configId);

    void setWidget(Widget widget, long configId);

    void deleteWidget(Widget widget);

    Configuration getNewConfig();

    LiveData<List<Configuration>> getAllConfigs();

    LiveData<Configuration> getCurrentConfig();


}
