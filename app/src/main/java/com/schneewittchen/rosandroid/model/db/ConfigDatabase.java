package com.schneewittchen.rosandroid.model.db;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.SSHEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetCountEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.LambdaTask;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.3
 * @created on 31.01.20
 * @updated on 15.05.20
 * @modified by Nico Studt
 * @updated on 04.06.20
 * @modified by Nils Rottmann
 * @updated on 27.07.20
 * @modified by Nico Studt
 * @updated on 27.07.20
 * @modified by Nils Rottmann
 */
@Database(entities =
        {ConfigEntity.class, MasterEntity.class, WidgetEntity.class, SSHEntity.class, WidgetCountEntity.class},
        version = 2,
        exportSchema = false)
public abstract class ConfigDatabase extends RoomDatabase {

    // Static instance -----------------------------------------------------------------------------

    private static final String TAG = ConfigDatabase.class.getCanonicalName();
    private static ConfigDatabase instance;
    private static String[] widgetNames;

    public static synchronized ConfigDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ConfigDatabase.class, Constants.dbName)
                    .addCallback(sRoomDatabaseCallback)
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
        new LambdaTask(() -> configDao().insertComplete(config)).execute();
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

    public void addWidget(WidgetEntity widget) {
        new LambdaTask(() -> widgetDao().insert(widget)).execute();
        new LambdaTask(() -> widgetCountDao().incrementValue(widget.configId, widget.type)).execute();
    }

    public void updateWidget(WidgetEntity widget) {
        new LambdaTask(() -> widgetDao().update(widget)).execute();
    }

    public void deleteWidget(WidgetEntity widget) {
        new LambdaTask(() -> {
            int id = widgetDao().delete(widget);
            // System.out.println("Id deleted: " + id);
        }).execute();
    }

    public LiveData<List<BaseEntity>> getWidgets(long id) {
        return widgetDao().getWidgets(id);
    }

    // Widget Count methods ------------------------------------------------------------------------

    public WidgetCountEntity getWidgetCount(long id, String type) {
        return widgetCountDao().getWidgetCountEntity(id, type);
    }


    // General database methods --------------------------------------------------------------------
    // Populate database
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    // new PopulateDbAsync(instance).execute();
                }

                /*
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(instance).execute();
                }
                */

            };

    /**
     * Delete and Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        // TODO: Delete this and run only if new created db
        private final ConfigDao configDao;
        private final MasterDao masterDao;
        private final SSHDao sshDao;
        private final WidgetDao widgetDao;
        private final WidgetCountDao widgetCountDao;


        PopulateDbAsync(ConfigDatabase db) {
            configDao = db.configDao();
            masterDao = db.masterDao();
            sshDao = db.sshDao();
            widgetDao = db.widgetDao();
            widgetCountDao = db.widgetCountDao();
        }


        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            configDao.deleteAll();
            masterDao.deleteAll();
            sshDao.deleteAll();
            widgetDao.deleteAll();
            widgetCountDao.deleteAll();

            // Create master data
            MasterEntity master = new MasterEntity();

            master.ip = "192.168.0.0";
            master.port = 11311;

            // Create ssh
            SSHEntity ssh = new SSHEntity();

            ssh.ip = "192.168.1.1";
            ssh.port = 22;
            ssh.username = "pi";
            ssh.password = "raspberry";

            // Create widgetCount
            ArrayList<WidgetCountEntity> widgetCountEntityList = new ArrayList<WidgetCountEntity>();
            for (int i=0; i<widgetNames.length; i++) {
                WidgetCountEntity widgetCountEntity = new WidgetCountEntity();
                widgetCountEntity.type = widgetNames[i];
                widgetCountEntity.count = 0;
                widgetCountEntityList.add(widgetCountEntity);
            }

            // Create configuration data
            ConfigEntity newConfig = new ConfigEntity();

            newConfig.creationTime = System.nanoTime();
            newConfig.lastUsed = System.nanoTime();
            newConfig.name = "Unnamed Config";
            newConfig.isFavourite = false;
            newConfig.master = master;
            newConfig.ssh = ssh;
            newConfig.widgetCounts = widgetCountEntityList;

            configDao.insertComplete(newConfig);

            return null;
        }
    }
}
