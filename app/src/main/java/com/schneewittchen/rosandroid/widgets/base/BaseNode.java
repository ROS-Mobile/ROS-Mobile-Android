package com.schneewittchen.rosandroid.widgets.base;

import com.schneewittchen.rosandroid.model.entities.WidgetEntity;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 13.03.20
 * @updated on 13.03.20
 * @modified by
 */
public abstract class BaseNode<T extends BaseEntity> implements NodeMain, DataListener{

    protected T widget;
    protected DataListener listener;

    public BaseNode(T widget) {
        this.widget = widget;
    }


    public void setListener(DataListener listener) {
        this.listener = listener;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(widget.name);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

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
