package com.schneewittchen.rosandroid.model.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.schneewittchen.rosandroid.model.entities.ConfigEntity;
import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.WidgetEntity;
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.LambdaTask;

import java.util.List;
import java.util.Random;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 31.01.20
 * @updated on 05.02.20
 * @modified by
 */
@Database(entities =
        {ConfigEntity.class, MasterEntity.class, WidgetEntity.class},
        version = 2,
        exportSchema = false)
public abstract class ConfigDatabase extends RoomDatabase {

    // Static instance -----------------------------------------------------------------------------

    private static final String TAG = ConfigDatabase.class.getCanonicalName();
    private static ConfigDatabase instance;

    public static synchronized ConfigDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ConfigDatabase.class, Constants.dbName)
                    .addCallback(sRoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }


    // Methods -------------------------------------------------------------------------------------

    public abstract ConfigDao configDao();
    public abstract MasterDao masterDao();
    public abstract WidgetDao widgetDao();


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

    public void updataMaster(MasterEntity master) {
        new LambdaTask(() -> masterDao().update(master)).execute();
    }

    public LiveData<MasterEntity> getMaster(long id) {
        return masterDao().getMaster(id);
    }


    // Widget methods ------------------------------------------------------------------------------

    public void addWidget(WidgetEntity widget) {
        new LambdaTask(() -> widgetDao().insert(widget)).execute();
    }

    public void updataWidget(WidgetEntity widget) {
        new LambdaTask(() -> widgetDao().update(widget)).execute();
    }

    public void deleteWidget(WidgetEntity widget) {
        new LambdaTask(() -> {
            int id = widgetDao().delete(widget);
            System.out.println("Id deleted: " + id);
        }).execute();
    }

    public LiveData<List<WidgetEntity>> getWidgets(long id) {
        return widgetDao().getWidgets(id);
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(instance).execute();
                }
            };

    /**
     * Delete and Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        // TODO: Delete this and run only if new created db
        private final ConfigDao configDao;
        private final MasterDao masterDao;
        private final WidgetDao widgetDao;


        PopulateDbAsync(ConfigDatabase db) {
            configDao = db.configDao();
            masterDao = db.masterDao();
            widgetDao = db.widgetDao();
        }


        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            configDao.deleteAll();
            masterDao.deleteAll();
            widgetDao.deleteAll();

            // Create master data
            MasterEntity master = new MasterEntity();

            Random random = new Random();
            master.ip = "123.456.789.01";
            master.port = 99999;
            master.notificationTickerTitle = "Ticker name";
            master.notificationTitle = "Title name";

            // Create configuration data
            ConfigEntity newConfig = new ConfigEntity();

            newConfig.creationTime = System.nanoTime();
            newConfig.lastUsed = System.nanoTime();
            newConfig.name = "New Config";
            newConfig.isFavourite = false;
            newConfig.master = master;

            configDao.insertComplete(newConfig);

            return null;
        }
    }

}
