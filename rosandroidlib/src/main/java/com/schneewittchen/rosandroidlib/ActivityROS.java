package com.schneewittchen.rosandroidlib;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Preconditions;

import org.ros.address.InetAddressFactory;
import org.ros.android.MasterChooser;
import org.ros.android.NodeMainExecutorService;
import org.ros.android.NodeMainExecutorServiceListener;
import org.ros.android.RosActivity;
import org.ros.exception.RosRuntimeException;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;


public abstract class ActivityROS extends AppCompatActivity {


    protected static final int MASTER_CHOOSER_REQUEST_CODE = 0;

    private final NodeMainExecutorServiceConnection nodeMainExecutorServiceConnection;
    private final String notificationTicker;
    private final String notificationTitle;
    private Class<?> masterChooserActivity = MasterChooser.class;
    private int masterChooserRequestCode = MASTER_CHOOSER_REQUEST_CODE;
    protected NodeMainExecutorService nodeMainExecutorService;
    private Boolean shutdownSignalReceived = false;


    /**
     * Standard constructor.
     * Use this constructor to proceed using the standard {@link MasterChooser}.
     *
     * @param notificationTicker Title to use in Ticker notifications.
     * @param notificationTitle  Title to use in notifications.
     */
    protected ActivityROS(String notificationTicker, String notificationTitle) {
        this(notificationTicker, notificationTitle, null);

        RosActivity rosActivity;

    }

    /**
     * Custom Master URI constructor.
     * Use this constructor to skip launching {@link MasterChooser}.
     *
     * @param notificationTicker Title to use in Ticker notifications.
     * @param notificationTitle  Title to use in notifications.
     * @param customMasterUri    URI of the ROS master to connect to.
     */
    protected ActivityROS(String notificationTicker, String notificationTitle, URI customMasterUri) {
        super();
        this.notificationTicker = notificationTicker;
        this.notificationTitle = notificationTitle;
        nodeMainExecutorServiceConnection = new NodeMainExecutorServiceConnection(customMasterUri);
    }

    /**
     * Custom MasterChooser constructor.
     * Use this constructor to specify which {@link Activity} should be started in place of {@link MasterChooser}.
     * The specified activity shall return a result that can be handled by a custom callback.
     * See {@link #setOnActivityResultCallback(OnActivityResultCallback)} for more information about
     * how to handle custom request codes and results.
     *
     * @param notificationTicker Title to use in Ticker notifications.
     * @param notificationTitle  Title to use in notifications.
     * @param activity           {@link Activity} to launch instead of {@link MasterChooser}.
     * @param requestCode        Request identifier to start the given {@link Activity} for a result.
     */
    protected ActivityROS(String notificationTicker, String notificationTitle, Class<?> activity, int requestCode) {
        this(notificationTicker, notificationTitle);
        masterChooserActivity = activity;
        masterChooserRequestCode = requestCode;
    }


    /**
     * Default Activity Result callback - compatible with standard {@link MasterChooser}
     */
    private OnActivityResultCallback onActivityResultCallback = new OnActivityResultCallback() {
        @Override
        public void execute(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                if (requestCode == MASTER_CHOOSER_REQUEST_CODE) {
                    String host;
                    String networkInterfaceName = data.getStringExtra("ROS_MASTER_NETWORK_INTERFACE");
                    // Handles the default selection and prevents possible errors
                    if (networkInterfaceName == null || networkInterfaceName.equals("")) {
                        host = getDefaultHostAddress();
                    } else {
                        try {
                            NetworkInterface networkInterface = NetworkInterface.getByName(networkInterfaceName);
                            host = InetAddressFactory.newNonLoopbackForNetworkInterface(networkInterface).getHostAddress();
                        } catch (SocketException e) {
                            throw new RosRuntimeException(e);
                        }
                    }
                    nodeMainExecutorService.setRosHostname(host);
                    if (data.getBooleanExtra("ROS_MASTER_CREATE_NEW", false)) {
                        nodeMainExecutorService.startMaster(data.getBooleanExtra("ROS_MASTER_PRIVATE", true));
                    } else {
                        URI uri;
                        try {
                            uri = new URI(data.getStringExtra("ROS_MASTER_URI"));
                        } catch (URISyntaxException e) {
                            throw new RosRuntimeException(e);
                        }
                        nodeMainExecutorService.setMasterUri(uri);
                    }
                    // Run init() in a new thread as a convenience since it often requires network access.
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            ActivityROS.this.init(nodeMainExecutorService);
                            return null;
                        }
                    }.execute();
                } else {
                    // Without a master URI configured, we are in an unusable state.
                    nodeMainExecutorService.forceShutdown();
                }
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        bindNodeMainExecutorService();
    }

    protected void bindNodeMainExecutorService() {
        Intent intent = new Intent(this, NodeMainExecutorService.class);
        intent.setAction(NodeMainExecutorService.ACTION_START);
        intent.putExtra(NodeMainExecutorService.EXTRA_NOTIFICATION_TICKER, notificationTicker);
        intent.putExtra(NodeMainExecutorService.EXTRA_NOTIFICATION_TITLE, notificationTitle);
        startService(intent);
        Preconditions.checkState(
                bindService(intent, nodeMainExecutorServiceConnection, BIND_AUTO_CREATE),
                "Failed to bind NodeMainExecutorService.");
    }

    @Override
    protected void onDestroy() {
        finishActivity(MASTER_CHOOSER_REQUEST_CODE);
        unbindService(nodeMainExecutorServiceConnection);
        nodeMainExecutorService.
                removeListener(nodeMainExecutorServiceConnection.getServiceListener());
        if (!shutdownSignalReceived) {
            //shutdown if RosActivity is not finished by a shutdown signal
            nodeMainExecutorService.forceShutdown();
        }
        super.onDestroy();
    }

    protected void init() {
        // Run init() in a new thread as a convenience since it often requires
        // network access.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ActivityROS.this.init(nodeMainExecutorService);
                return null;
            }
        }.execute();
    }

    /**
     * This method is called in a background thread once this {@link Activity} has
     * been initialized with a master {@link URI} via the {@link MasterChooser}
     * and a {@link NodeMainExecutorService} has started. Your {@link NodeMain}s
     * should be started here using the provided {@link NodeMainExecutor}.
     *
     * @param nodeMainExecutor the {@link NodeMainExecutor} created for this {@link Activity}
     */
    protected abstract void init(NodeMainExecutor nodeMainExecutor);

    public void startMasterChooser() {
        Preconditions.checkState(getMasterUri() == null);
        // Call this method on super to avoid triggering our precondition in the
        // overridden startActivityForResult().
        super.startActivityForResult(new Intent(this, masterChooserActivity), masterChooserRequestCode);
    }

    public URI getMasterUri() {
        Preconditions.checkNotNull(nodeMainExecutorService);
        return nodeMainExecutorService.getMasterUri();
    }

    public String getRosHostname() {
        Preconditions.checkNotNull(nodeMainExecutorService);
        return nodeMainExecutorService.getRosHostname();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Preconditions.checkArgument(requestCode != masterChooserRequestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (onActivityResultCallback != null) {
            onActivityResultCallback.execute(requestCode, resultCode, data);
        }
    }

    private String getDefaultHostAddress() {
        return InetAddressFactory.newNonLoopback().getHostAddress();
    }

    public interface OnActivityResultCallback {
        void execute(int requestCode, int resultCode, Intent data);
    }

    /**
     * Set a callback that will be called onActivityResult.
     * Custom callbacks should be able to handle custom request codes configured
     * in custom Activity constructor {@link #ActivityROS(String, String, Class, int)}.
     *
     * @param callback Action that will be performed when this Activity gets a result.
     */
    public void setOnActivityResultCallback(OnActivityResultCallback callback) {
        onActivityResultCallback = callback;
    }


    private final class NodeMainExecutorServiceConnection implements ServiceConnection {

        private NodeMainExecutorServiceListener serviceListener;
        private URI customMasterUri;

        public NodeMainExecutorServiceConnection(URI customUri) {
            super();
            customMasterUri = customUri;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            nodeMainExecutorService = ((NodeMainExecutorService.LocalBinder) binder).getService();

            if (customMasterUri != null) {
                nodeMainExecutorService.setMasterUri(customMasterUri);
                nodeMainExecutorService.setRosHostname(getDefaultHostAddress());
            }

            serviceListener = new NodeMainExecutorServiceListener() {
                @Override
                public void onShutdown(NodeMainExecutorService nodeMainExecutorService) {
                    // We may have added multiple shutdown listeners and we only want to
                    // call finish() once.
                    if (!ActivityROS.this.isFinishing()) {
                        shutdownSignalReceived = true;
                        ActivityROS.this.finish();
                    }
                }
            };
            nodeMainExecutorService.addListener(serviceListener);
            if (getMasterUri() == null) {
                startMasterChooser();
            } else {
                init();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            nodeMainExecutorService.removeListener(serviceListener);
            serviceListener = null;
        }

        public NodeMainExecutorServiceListener getServiceListener() {
            return serviceListener;
        }

    }

}
