package com.schneewittchen.rosandroidlib;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import geometry_msgs.Twist;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 16.01.20
 * @updated on 16.01.20
 * @modified by
 */
public class TestListener2 implements MessageListener<Twist>, NodeMain {

    @Override
    public void onNewMessage(Twist twist) {
        System.out.println("Listener2:" + twist.getLinear().getX());
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/test_listener2");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<Twist> subscriber =
                connectedNode.newSubscriber("cmd_vel", Twist._TYPE);
        subscriber.addMessageListener(this);
    }

    @Override
    public void onShutdown(Node node) {

    }

    @Override
    public void onShutdownComplete(Node node) {

    }

    @Override
    public void onError(Node node, Throwable throwable) {

    }
}