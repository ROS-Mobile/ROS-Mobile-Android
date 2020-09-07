package com.schneewittchen.rosandroid.model.repositories;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import com.schneewittchen.rosandroid.model.entities.MasterEntity;
import com.schneewittchen.rosandroid.model.entities.RosTopic;
import com.schneewittchen.rosandroid.ros.ConnectionCheckTask;
import com.schneewittchen.rosandroid.ros.ConnectionListener;
import com.schneewittchen.rosandroid.ros.NodeMainExecutorService;
import com.schneewittchen.rosandroid.ros.NodeMainExecutorServiceListener;
import com.schneewittchen.rosandroid.ui.helper.WidgetDiffCallback;
import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseData;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.BaseNode;
import com.schneewittchen.rosandroid.widgets.base.DataListener;

import org.ros.address.InetAddressFactory;
import org.ros.internal.node.client.MasterClient;
import org.ros.internal.node.response.Response;
import org.ros.internal.node.server.master.MasterServer;
import org.ros.master.client.TopicType;
import org.ros.namespace.GraphName;
import org.ros.node.NodeConfiguration;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The ROS repository is responsible for connecting to the ROS master
 * and creating nodes depending on the respective widgets.
 *
 * @author Nico Studt
 * @version 1.1.2
 * @created on 16.01.20
 * @updated on 20.05.20
 * @modified by Nico Studt
 */
public class RosRepository implements DataListener {

    private static final String TAG = RosRepository.class.getSimpleName();
    private static RosRepository instance;

    private WeakReference<Context> contextReference;

    private MasterEntity master;
    private List<BaseEntity> currentWidgets;
    private HashMap<Long, BaseNode> currentNodes;
    private MutableLiveData<ConnectionType> rosConnected;

    private MutableLiveData<BaseData> receivedData;

    private NodeMainExecutorService nodeMainExecutorService;
    private NodeConfiguration nodeConfiguration;


    /**
     * Default private constructor. Initialize empty lists and maps of intern widgets and nodes.
     */
    private RosRepository(Context context) {
        this.contextReference = new WeakReference<>(context);
        this.currentWidgets = new ArrayList<>();
        this.currentNodes = new HashMap<>();
        this.rosConnected = new MutableLiveData<>(ConnectionType.DISCONNECTED);
        this.receivedData = new MutableLiveData<>();
    }


    /**
     * Return the singleton instance of the repository.
     * @return Instance of this Repository
     */
    public static RosRepository getInstance(Context context){
        if(instance == null){
            instance = new RosRepository(context);
        }

        return instance;
    }

    /**
     * Connect all registered nodes and establish a connection to the ROS master with
     * the connection details given by the already delivered master entity.
     */
    public void connectToMaster() {
        Log.i(TAG, "Connect to Master");

        ConnectionType connectionType = rosConnected.getValue();
        if (connectionType == ConnectionType.CONNECTED || connectionType == ConnectionType.PENDING) {
            return;
        }

        rosConnected.setValue(ConnectionType.PENDING);

        // Check connection
        new ConnectionCheckTask(new ConnectionListener() {

            @Override
            public void onSuccess() {
                bindService();
            }

            @Override
            public void onFailed() {
                rosConnected.postValue(ConnectionType.FAILED);
            }
        }).execute(master);
    }

    /**
     * Disconnect all running nodes and cut the connection to the ROS master.
     */
    public void disconnectFromMaster() {
        Log.i(TAG, "Disconnect from Master");
        if (nodeMainExecutorService == null) {
            return;
        }

        this.unregisterAllNodes();
        nodeMainExecutorService.shutdown();
    }


    /**
     * Change the connection details to the ROS master like the IP or port.
     * @param master Master data
     */
    public void updateMaster(MasterEntity master) {
        Log.i(TAG, "Update Master");

        if(master == null) {
            Log.i(TAG, "Master is null");
            return;
        }

        this.master = master;

        String deviceIp = this.getDeviceIp();
        nodeConfiguration = NodeConfiguration.newPublic(deviceIp, getMasterURI());
    }


    /**
     * React on a widget change. If at least one widget is added, deleted or changed this method
     * should be called.
     * @param widgets Current list of widgets
     */
    public void updateWidgets(List<BaseEntity> widgets) {
        WidgetDiffCallback diffCallback = new WidgetDiffCallback(widgets, this.currentWidgets);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                for (int i=position; i<position+count; i++) {
                    addNode(widgets.get(i));
                }
            }

            @Override
            public void onRemoved(int position, int count) {
                for (int i=position; i<position+count; i++) {
                    removeNode(currentWidgets.get(i));
                }
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) { }

            @Override
            public void onChanged(int position, int count, @Nullable Object payload) {
                for(int i = position; i < position +count; i++) {
                    updateNode(widgets.get(i));
                }

            }
        });

        this.currentWidgets.clear();
        this.currentWidgets.addAll(widgets);

    }


    /**
     * Find the associated node and inform it about the changed data.
     * @param data Widget data that has changed
     */
    public void informWidgetDataChange(BaseData data) {
        BaseNode node = currentNodes.get(data.id);

        if(node != null) {
            node.onNewData(data);
        }
    }

    /**
     * Get the current connection status of the ROS service as a live data.
     * @return Connection status
     */
    public LiveData<ConnectionType> getRosConnectionStatus() {
        return rosConnected;
    }


    private void bindService() {
        Context context = contextReference.get();
        if (context == null) {
            return;
        }

        NodeMainExecutorServiceConnection serviceConnection = new NodeMainExecutorServiceConnection(getMasterURI());

        // Create service intent
        Intent serviceIntent = new Intent(context, NodeMainExecutorService.class);
        serviceIntent.setAction(NodeMainExecutorService.ACTION_START);

        // Start service and check state
        context.startService(serviceIntent);
        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Create a node for a specific widget entity.
     * The node will be created and subsequently registered.
     * @param widget Widget to be added
     */
    private void addNode(BaseEntity  widget) {
        // Create node main from widget
        Class<? extends BaseNode> clazz = widget.getNodeType();

        try {
            Constructor<? extends BaseNode> cons  = clazz.getConstructor();
            BaseNode node = cons.newInstance();
            node.setWidget(widget);
            node.setListener(this);
            currentNodes.put(widget.id, node);
            registerNode(node);

        } catch (Exception e) {
            Log.e(TAG, "Error while adding node: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update a widget and its associated Node by ID in the ROS graph.
     * @param widget Widget to update
     */
    private void updateNode(BaseEntity widget) {
        Log.i(TAG, "Update Node: " + widget.name);
        BaseNode node = currentNodes.get(widget.id);
        assert node != null;

        node.setWidget(widget);
        this.reregisterNode(node);
    }

    /**
     * Remove a widget and its associated Node in the ROS graph.
     * @param widget Widget to remove
     */
    private void removeNode(BaseEntity  widget) {
        BaseNode node = currentNodes.get(widget.id);
        this.currentNodes.remove(widget.id);
        this.unregisterNode(node);
    }

    /**
     * Connect the node to ROS node graph if a connection to the ROS master is running.
     * @param node Node to connect
     */
    private void registerNode(BaseNode node) {
        Log.i(TAG, "register Node");

        if (rosConnected.getValue() != ConnectionType.CONNECTED) {
            Log.w(TAG, "Not connected with master");
            return;
        }

        nodeMainExecutorService.execute(node, nodeConfiguration);
    }

    /**
     * Disconnect the node from ROS node graph if a connection to the ROS master is running.
     * @param node Node to disconnect
     */
    private void unregisterNode(BaseNode node) {
        Log.i(TAG, "unregister Node");

        if (rosConnected.getValue() != ConnectionType.CONNECTED) {
            Log.w(TAG, "Not connected with master");
            return;
        }

        nodeMainExecutorService.shutdownNodeMain(node);
    }

    private void registerAllNodes() {
        for (BaseNode node: currentNodes.values()) {
            this.registerNode(node);
        }
    }

    private void unregisterAllNodes() {
        for (BaseNode node: currentNodes.values()) {
            this.unregisterNode(node);
        }
    }

    /**
     * Result of a change in the internal data of a node header. Therefore it has to be
     * unregistered from the service and reregistered due to the implementation of ROS.
     * @param node Node main to be reregistered
     */
    private void reregisterNode(BaseNode node) {
        Log.i(TAG, "Reregister Node");

        unregisterNode(node);
        registerNode(node);
    }

    private URI getMasterURI() {
        String masterString = String.format("http://%s:%s/", master.ip, master.port);
        return URI.create(masterString);
    }

    private String getDeviceIp(){
        return Utils.getIPAddress(true);
    }

    private String getDefaultHostAddress() {
        return InetAddressFactory.newNonLoopback().getHostAddress();
    }

    public LiveData<BaseData> getData() {
        return receivedData;
    }

    @Override
    public void onNewData(BaseData newData) {
        this.receivedData.postValue(newData);
    }


    private final class NodeMainExecutorServiceConnection implements ServiceConnection {

        NodeMainExecutorServiceListener serviceListener;
        URI customMasterUri;


        NodeMainExecutorServiceConnection(URI customUri) {
            customMasterUri = customUri;
        }


        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.i(TAG, "Service connected");
            nodeMainExecutorService = ((NodeMainExecutorService.LocalBinder) binder).getService();
            nodeMainExecutorService.setMasterUri(customMasterUri);
            nodeMainExecutorService.setRosHostname(getDefaultHostAddress());



            serviceListener = nodeMainExecutorService ->
                    rosConnected.postValue(ConnectionType.DISCONNECTED);

            nodeMainExecutorService.addListener(serviceListener);
            rosConnected.setValue(ConnectionType.CONNECTED);

            registerAllNodes();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            nodeMainExecutorService.removeListener(serviceListener);
        }
    }

    public List<RosTopic> getTopicList() {
        // Get a list with all available topics from the ROS Master
        ArrayList<RosTopic> topicList = new ArrayList<>();
        if (nodeMainExecutorService == null || nodeConfiguration == null) {
            return topicList;
        }
        MasterClient masterClient = new MasterClient(nodeMainExecutorService.getMasterUri());
        GraphName graphName = GraphName.newAnonymous();
        Response<List<TopicType>> responseList = masterClient.getTopicTypes(graphName);
        for (TopicType result: responseList.getResult()) {
            RosTopic rosTopic = new RosTopic();
            rosTopic.name = result.getName();
            rosTopic.type = result.getMessageType();
            topicList.add(rosTopic);
        }
        return topicList;
    }

}
