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
import com.schneewittchen.rosandroid.utility.Constants;
import com.schneewittchen.rosandroid.utility.LambdaTask;

import java.util.List;
import java.util.Random;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 31.01.20
 * @updated on 31.01.20
 * @modified by
 */
@Database(entities =
        {ConfigEntity.class, MasterEntity.class},
        version = 1,
        exportSchema = false)
public abstract class ConfigDatabase extends RoomDatabase {

    // Static instance -----------------------------------------------------------------------------

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


    public void insertCompleteConfig(ConfigEntity config) {
        new LambdaTask(() -> configDao().insertComplete(config)).execute();
    }

    public void insertJustConfigEntity(ConfigEntity config) {
        new LambdaTask(() -> configDao().insert(config)).execute();
    }

    public void update(ConfigEntity... configs) {
        new LambdaTask(() -> configDao().update(configs[0])).execute();
    }

    public void delete(ConfigEntity... configs) {
        new LambdaTask(() -> configDao().delete(configs[0])).execute();
    }

    public LiveData<MasterEntity> getMaster(long id) {
        return masterDao().getMaster(id);
    }

    public LiveData<ConfigEntity> getLatestConfig() {
        return configDao().getLatestConfig();
    }

    public LiveData<List<ConfigEntity>> getAllConfigs() {
        return configDao().getAllConfigs();
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


        PopulateDbAsync(ConfigDatabase db) {
            configDao = db.configDao();
            masterDao = db.masterDao();
        }


        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            configDao.deleteAll();
            masterDao.deleteAll();

            // Create master data
            MasterEntity master = new MasterEntity();

            Random random = new Random();
            master.ip = "123.456.789.01";
            master.port = random.nextInt(99999);
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
