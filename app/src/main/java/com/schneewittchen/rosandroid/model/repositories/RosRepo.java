package com.schneewittchen.rosandroid.model.repositories;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.util.Preconditions;

import com.schneewittchen.rosandroid.utility.Utils;
import com.schneewittchen.rosandroid.widgets.base.BaseEntity;
import com.schneewittchen.rosandroid.widgets.base.WidgetData;
import com.schneewittchen.rosandroid.widgets.base.WidgetNode;

import org.ros.address.InetAddressFactory;
import org.ros.android.NodeMainExecutorService;
import org.ros.android.NodeMainExecutorServiceListener;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.1
 * @created on 16.01.20
 * @updated on 31.01.20
 * @modified by
 */
public class RosRepo {


    private static RosRepo rosRepo;

    private WeakReference<Context> contextReference;
    private URI masterURI;
    private String notificationTickerTitle;

    //The notification title is displayed outside of the application as a notification
    private String notificationTitle;
    private boolean shutdownSignalReceived;

    private Intent serviceIntent;
    private NodeMainExecutorServiceConnection nodeMainExecutorServiceConnection;
    private NodeMainExecutorService nodeMainExecutorService;
    private NodeConfiguration nodeConfiguration;
    private Handler nodeHandler;
    private ArrayList<NodeMain> nodesWaitList;
    private HashMap<Long, WidgetNode> widgetNodes;


    public static RosRepo getInstance(){
        if(rosRepo == null){
            rosRepo = new RosRepo();
        }

        return rosRepo;
    }


    private RosRepo(){
        nodesWaitList = new ArrayList<>();
        widgetNodes = new HashMap<>();
    }


    public void connectToMaster(){
        nodeMainExecutorServiceConnection = new NodeMainExecutorServiceConnection(masterURI);
        this.bindNodeMainExecutorService();
    }

    public void disconnectFromMaster(){
        nodeConfiguration = null;
    }

    public void registerNode(BaseEntity widgetEntity) {
        Class<? extends WidgetNode> clazz = widgetEntity.getNodeType();

        try {
            Constructor<? extends WidgetNode> cons  = clazz.getConstructor();
            WidgetNode widgetNode = cons.newInstance();
            registerNode(widgetNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerNode(NodeMain node){
        if (nodeConfiguration != null) {
            nodeMainExecutorService.execute(node, nodeConfiguration);

        } else {
            nodesWaitList.add(node);
        }
    }

    public void unregisterNode(NodeMain node){
        if (nodeConfiguration != null) {
            nodeMainExecutorService.shutdownNodeMain(node);

        } else {
            // TODO: Add to waiting list
        }
    }

    public String getDeviceIp(){
        return Utils.getIPAddress(true);
    }

    public void setNotificationTickerTitle(String title){
        this.notificationTickerTitle = title;
    }

    public void setNotificationTitle(String title){
        this.notificationTitle = title;
    }

    public void setContext(Context context){
        contextReference = new WeakReference<>(context);
    }

    public void setMasterAddress(String address){
        this.setMasterURI(URI.create(address));
    }

    public void setMasterAddress(String ip, int port){
        System.out.println(ip + " " + port);

        String masterString = String.format("http://%s:%d/", ip, port);
        this.setMasterURI(URI.create(masterString));
    }

    public void setMasterURI(URI masterURI){
        this.masterURI = masterURI;
    }

    public void destroyService(){
        // Check if context alive
        Preconditions.checkArgument(contextReference.get() != null);
        Context context = contextReference.get();

        context.stopService(serviceIntent);
    }


    private void testConnection(String ip, int port){
        try {
            InetAddress local_network_address;
            Socket socket = new Socket(ip, port);
            local_network_address = socket.getLocalAddress();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindNodeMainExecutorService() {
        // Check if context alive
        Context context = contextReference.get();

        // Create service intent
        serviceIntent = new Intent(context, NodeMainExecutorService.class);
        serviceIntent.setAction(NodeMainExecutorService.ACTION_START);
        serviceIntent.putExtra(NodeMainExecutorService.EXTRA_NOTIFICATION_TICKER, notificationTickerTitle);
        serviceIntent.putExtra(NodeMainExecutorService.EXTRA_NOTIFICATION_TITLE, notificationTitle);

        // Start service and check state
        context.startService(serviceIntent);

        boolean binding = context.bindService(serviceIntent,
                            nodeMainExecutorServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private String getDefaultHostAddress() {
        return InetAddressFactory.newNonLoopback().getHostAddress();
    }

    private void initNodes() {
        // Create node config
        nodeConfiguration = NodeConfiguration.newPublic(this.getDeviceIp(), masterURI);

        nodeHandler = new Handler();

        final Runnable runnable = new Runnable() {
            public void run() {
                initNodes(nodeMainExecutorService);
            }
        };

        nodeHandler.post(runnable);
    }

    private void initNodes(NodeMainExecutor nodeMainExecutor){
        if (nodeConfiguration == null)
            return;

        for (NodeMain nodeMain: nodesWaitList){
            this.registerNode(nodeMain);
        }

        nodesWaitList.clear();
    }

    public void informWidgetDataChange(WidgetData data) {
        WidgetNode node = widgetNodes.get(data.id);

        if(node == null) {
            return;
        }

        node.onNewData(data);
    }


    private final class NodeMainExecutorServiceConnection implements ServiceConnection {

        NodeMainExecutorServiceListener serviceListener;
        URI customMasterUri;


        NodeMainExecutorServiceConnection(URI customUri) {
            customMasterUri = customUri;
        }


        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            System.out.println("Service connected");
            nodeMainExecutorService = ((NodeMainExecutorService.LocalBinder) binder).getService();

            if (customMasterUri != null) {
                nodeMainExecutorService.setMasterUri(customMasterUri);
                nodeMainExecutorService.setRosHostname(getDefaultHostAddress());
            }

            serviceListener = nodeMainExecutorService -> {
                System.out.println("On shutdown");
                shutdownSignalReceived = true;
            };

            nodeMainExecutorService.addListener(serviceListener);
            initNodes();
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
