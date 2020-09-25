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
import com.schneewittchen.rosandroid.model.entities.WidgetCountEntity;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.LambdaTask;
import com.schneewittchen.rosandroid.widgets.test.BaseWidget;
import com.schneewittchen.rosandroid.widgets.test.WidgetStorageData;

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
        {ConfigEntity.class, MasterEntity.class, WidgetStorageData.class, SSHEntity.class, WidgetCountEntity.class},
        version = 3, exportSchema = false)
public abstract class ConfigDatabase extends RoomDatabase {

    private static final String TAG = ConfigDatabase.class.getCanonicalName();
    private static ConfigDatabase instance;
    private static String[] widgetNames;


    public static synchronized ConfigDatabase getInstance(final Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ConfigDatabase.class, Constants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        widgetNames = context.getResources().getStringArray(R.array.widget_names);

        return instance;
    }


    // Methods -------------------------------------------------------------------------------------

    public abstract ConfigDao configDao();
    public abstract MasterDao masterDao();
    public abstract WidgetDao widgetDao();
    public abstract WidgetCountDao widgetCountDao();
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
    }

    public LiveData<ConfigEntity> getConfig(long id) {
        return configDao().getConfig(id);
    }

    public LiveData<ConfigEntity> getLatestConfig() {
        return configDao().getLatestConfig();
    }

    public LiveData<List<ConfigEntity>> getAllConfigs() {
        return configDao().getAllConfigs();
    }


    // Master methods ------------------------------------------------------------------------------

    public void updateMaster(MasterEntity master) {
        new LambdaTask(() -> masterDao().update(master)).execute();
    }

    public LiveData<MasterEntity> getMaster(long id) {
        return masterDao().getMaster(id);
    }


    // SSH methods ------------------------------------------------------------------------------

    public void updateSSH(SSHEntity ssh) {
        new LambdaTask(() -> sshDao().update(ssh)).execute();
    }

    public LiveData<SSHEntity> getSSH(long id) {
        return sshDao().getSSH(id);
    }


    // Widget methods ------------------------------------------------------------------------------

    public void addWidget(BaseWidget widget) {
        new LambdaTask(() ->
                widgetDao().insert(widget))
                .execute();
        new LambdaTask(() ->
                widgetCountDao()
                        .incrementValue(widget.configId, widget.getClass().getSimpleName()))
                .execute();

    }

    public void updateWidget(BaseWidget widget) {
        new LambdaTask(() ->
                widgetDao().update(widget))
                .execute();
    }

    public void deleteWidget(BaseWidget widget) {
        new LambdaTask(() ->
                widgetDao().delete(widget))
                .execute();
    }

    public LiveData<List<BaseWidget>> getWidgets(long id) {
        return widgetDao().getWidgets(id);
    }


    // Widget Count methods ------------------------------------------------------------------------

    public WidgetCountEntity getWidgetCount(long id, String className) {
        return widgetCountDao().getWidgetCountEntity(id, className);
    }

}
