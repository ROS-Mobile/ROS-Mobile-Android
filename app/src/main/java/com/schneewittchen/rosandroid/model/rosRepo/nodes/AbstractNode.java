package com.schneewittchen.rosandroid.model.rosRepo.nodes;

import android.util.Log;

import com.schneewittchen.rosandroid.ros.Topic;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.09.20
 * @updated on
 * @modified by
 */
public abstract class AbstractNode implements NodeMain {

    public static final String TAG = AbstractNode.class.getSimpleName();

    Topic topic;


    public abstract void onStart(ConnectedNode parentNode);


    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(topic.getName());
    }


    @Override
    public void onShutdown(Node node) {
        Log.i(TAG, "On Shutdown:  " + topic.getName());
    }

    @Override
    public void onShutdownComplete(Node node) {
        Log.i(TAG, "On Shutdown Complete: " + topic.getName());
    }

    @Override
    public void onError(Node node, Throwable throwable) {
        throwable.printStackTrace();
    }
}
