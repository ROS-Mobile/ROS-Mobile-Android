package com.schneewittchen.rosandroid.ros;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 15.09.20
 * @updated on 15.09.20
 * @modified by
 */
class Node {

    Topic topic;
    List<NodeListener> listeners;


    public void addListener(NodeListener listener) {

    }

    public interface NodeListener  {
        void onData();
    }
}
