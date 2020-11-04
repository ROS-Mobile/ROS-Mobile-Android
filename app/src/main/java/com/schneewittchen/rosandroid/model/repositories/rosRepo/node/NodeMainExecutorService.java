/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.schneewittchen.rosandroid.model.repositories.rosRepo.node;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import org.ros.concurrent.ListenerGroup;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeListener;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.net.URI;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;


/**
 * TODO: Description
 *
 * @author Damon Kohler
 * @version 1.0.0
 * @created on 15.04.20
 * @updated on 15.04.20
 * @modified by Nico Studt
 */
public class NodeMainExecutorService extends Service implements NodeMainExecutor {

    private static final String TAG = "NodeMainExecutorService";

    public static final String ACTION_START = "org.ros.android.ACTION_START_NODE_RUNNER_SERVICE";
    public static final String ACTION_SHUTDOWN = "org.ros.android.ACTION_SHUTDOWN_NODE_RUNNER_SERVICE";

    private final NodeMainExecutor nodeMainExecutor;
    private final IBinder binder;
    private final ListenerGroup<NodeMainExecutorServiceListener> listeners;

    private WakeLock wakeLock;
    private WifiLock wifiLock;
    private URI masterUri;
    private String rosHostname;


    public NodeMainExecutorService() {
        rosHostname = null;
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
        binder = new LocalBinder();
        listeners = new ListenerGroup<>(nodeMainExecutor.getScheduledExecutorService());
    }


    @Override
    public void onCreate() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        assert powerManager != null;

        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ROSANDROID:" +TAG);
        wakeLock.acquire(10*60*1000L /*10 minutes*/);

        int wifiLockType = WifiManager.WIFI_MODE_FULL;

        try {
            wifiLockType = WifiManager.class.getField("WIFI_MODE_FULL_HIGH_PERF").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.w(TAG, "Unable to acquire high performance wifi lock.");
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;

        wifiLock = wifiManager.createWifiLock(wifiLockType, TAG);
        wifiLock.acquire();
    }

    @Override
    public void execute(NodeMain nodeMain, NodeConfiguration nodeConfiguration) {
        execute(nodeMain, nodeConfiguration, null);
    }

    @Override
    public void execute(NodeMain nodeMain, NodeConfiguration nodeConfiguration,
                        Collection<NodeListener> nodeListeneners) {
        nodeMainExecutor.execute(nodeMain, nodeConfiguration, nodeListeneners);
    }

    @Override
    public ScheduledExecutorService getScheduledExecutorService() {
        return nodeMainExecutor.getScheduledExecutorService();
    }

    @Override
    public void shutdownNodeMain(NodeMain nodeMain) {
        nodeMainExecutor.shutdownNodeMain(nodeMain);
    }

    @Override
    public void shutdown() {
        signalOnShutdown();
        stopForeground(true);
        stopSelf();
    }

    public void addListener(NodeMainExecutorServiceListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NodeMainExecutorServiceListener listener) {
        listeners.remove(listener);
    }

    private void signalOnShutdown() {
        listeners.signal(nodeMainExecutorServiceListener ->
                nodeMainExecutorServiceListener.onShutdown(this));
    }

    @Override
    public void onDestroy() {
        nodeMainExecutor.shutdown();

        if (wakeLock.isHeld()) wakeLock.release();
        if (wifiLock.isHeld()) wifiLock.release();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == null) {
            return START_NOT_STICKY;
        }

        switch (intent.getAction()) {
            case ACTION_START:
                Intent notificationIntent = new Intent(this, NodeMainExecutorService.class);
                notificationIntent.setAction(NodeMainExecutorService.ACTION_SHUTDOWN);
                break;

            case ACTION_SHUTDOWN:
                shutdown();
                break;
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public URI getMasterUri() {
        return masterUri;
    }

    public void setMasterUri(URI uri) {
        masterUri = uri;
    }

    public void setRosHostname(String hostname) {
        rosHostname = hostname;
    }

    public String getRosHostname() {
        return rosHostname;
    }


    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public NodeMainExecutorService getService() {
            return NodeMainExecutorService.this;
        }
    }

}