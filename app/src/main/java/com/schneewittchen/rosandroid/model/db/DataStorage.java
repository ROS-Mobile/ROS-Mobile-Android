package com.schneewittchen.rosandroid.model.db;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.SSHEntity;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.LambdaTask;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetStorageData;

import java.util.List;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.4
 * @created on 31.01.20
 * @updated on 15.05.20
 * @modified by Nico Studt
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 * @updated on 27.07.20
 * @modified by Nico Studt
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 * @updated on 23.09.20
 * @modified by Nico Studt
 */
@Database(entities =
        {ConfigEntity.class, MasterEntity.class, WidgetStorageData.class, SSHEntity.class},
        version = 6, exportSchema = false)
public abstract class DataStorage extends RoomDatabase {

    private static final String TAG = DataStorage.class.getCanonicalName();
    private static DataStorage instance;
    private static String[] widgetNames;


    public static synchronized DataStorage getInstance(final Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DataStorage.class, Constants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        widgetNames = context.getResources().getStringArray(R.array.widget_names);

        return instance;
    }


    // DAO Methods ---------------------------------------------------------------------------------

    public abstract ConfigDao configDao();
    public abstract MasterDao masterDao();
    public abstract WidgetDao widgetDao();
    public abstract SSHDao sshDao();


    // Config methods ------------------------------------------------------------------------------

    public void addConfig(ConfigEntity config) {
        new LambdaTask(() -> configDao().insert(config)).execute();
    }

    public void updateConfig(ConfigEntity config) {
        new LambdaTask(() -> configDao().update(config)).execute();
    }

    public void deleteConfig(ConfigEntity config) {
        new LambdaTask(() -> configDao().delete(config)).execute();
    }

    public void deleteConfig(long id) {
        new LambdaTask(() -> configDao().removeConfig(id)).execute();
        new LambdaTask(() -> masterDao().delete(id)).execute();
        new LambdaTask(() -> sshDao().delete(id)).execute();
        new LambdaTask(() -> widgetDao().deleteWithConfigId(id)).execute();
    }

    public LiveData<ConfigEntity> getConfig(long id) {
        return configDao().getConfig(id);
    }

    public LiveData<ConfigEntity> getLatestConfig() {
        return configDao().getLatestConfig();
    }

    public ConfigEntity getLatestConfigDirect() {
        return configDao().getLatestConfigDirect();
    }

    public LiveData<List<ConfigEntity>> getAllConfigs() {
        return configDao().getAllConfigs();
    }


    // Master methods ------------------------------------------------------------------------------

    public void addMaster(MasterEntity master) {
        new LambdaTask(() -> masterDao().insert(master)).execute();
    }

    public void updateMaster(MasterEntity master) {
        new LambdaTask(() -> masterDao().update(master)).execute();
    }

    public void deleteMaster(long configId) {
        new LambdaTask(() -> masterDao().delete(configId)).execute();
    }

    public LiveData<MasterEntity> getMaster(long id) {
        return masterDao().getMaster(id);
    }


    // SSH methods ---------------------------------------------------------------------------------

    public void addSSH(SSHEntity ssh) {
        new LambdaTask(() -> sshDao().insert(ssh)).execute();
    }

    public void updateSSH(SSHEntity ssh) {
        new LambdaTask(() -> sshDao().update(ssh)).execute();
    }

    public void deleteSSH(long configId) {
        new LambdaTask(() -> sshDao().delete(configId)).execute();
    }

    public LiveData<SSHEntity> getSSH(long id) {
        return sshDao().getSSH(id);
    }


    // Widget methods ------------------------------------------------------------------------------

    public void addWidget(BaseEntity widget) {
        new LambdaTask(() ->
                widgetDao().insert(widget))
                .execute();
    }

    public void updateWidget(BaseEntity widget) {
        new LambdaTask(() ->
                widgetDao().update(widget))
                .execute();
    }

    public void deleteWidget(BaseEntity widget) {
        new LambdaTask(() ->
                widgetDao().delete(widget))
                .execute();
    }

    public LiveData<BaseEntity> getWidget(long configId, long widgetId) {
        return widgetDao().getWidget(configId, widgetId);
    }


    public LiveData<List<BaseEntity>> getWidgets(long configId) {
        return widgetDao().getWidgets(configId);
    }

    public boolean widgetNameExists(long configId, String name) {
        return widgetDao().exists(configId, name);
    }

}
