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

package com.schneewittchen.rosandroid.widgets.gltest.visualisation;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.schneewittchen.rosandroid.widgets.gltest.layer.Layer;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Subscriber;
import org.ros.rosjava_geometry.FrameTransformTree;

import java.util.Collections;
import java.util.List;

import tf2_msgs.TFMessage;


/**
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class VisualizationView extends GLSurfaceView implements NodeMain {

    private static final boolean DEBUG = false;

    private final Object mutex = new Object();
    private final FrameTransformTree frameTransformTree = new FrameTransformTree();
    private final XYOrthographicCamera camera = new XYOrthographicCamera(frameTransformTree);

    private List<Layer> layers;
    private XYOrthographicRenderer renderer;
    private ConnectedNode connectedNode;


    public VisualizationView(Context context) {
        super(context);
    }

    public VisualizationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void onCreate(List<Layer> layers) {
        this.layers = layers;
        setDebugFlags(DEBUG_CHECK_GL_ERROR);

        if (DEBUG) {
            // Turn on OpenGL logging.
            setDebugFlags(getDebugFlags() | DEBUG_LOG_GL_CALLS);
        }

        setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        renderer = new XYOrthographicRenderer(this);
        setRenderer(renderer);
    }

    public void init(NodeMainExecutor nodeMainExecutor) {
        Preconditions.checkNotNull(layers);

        for (Layer layer : layers) {
            layer.init(nodeMainExecutor);
        }
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android_15/visualization_view");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (Layer layer : Lists.reverse(layers)) {
            if (layer.onTouchEvent(this, event)) {
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public XYOrthographicRenderer getRenderer() {
        return renderer;
    }

    public XYOrthographicCamera getCamera() {
        return camera;
    }

    public FrameTransformTree getFrameTransformTree() {
        return frameTransformTree;
    }

    public List<Layer> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        this.connectedNode = connectedNode;
        startTransformListener();
        startLayers();
    }

    private void startTransformListener() {
        final Subscriber<TFMessage> tfSubscriber =
                connectedNode.newSubscriber("tf", tf2_msgs.TFMessage._TYPE);
        tfSubscriber.addMessageListener(message -> {
            synchronized (mutex) {
                for (geometry_msgs.TransformStamped transform : message.getTransforms()) {
                    frameTransformTree.update(transform);
                }
            }
        });
        final Subscriber<tf2_msgs.TFMessage> tfStaticSubscriber =
                connectedNode.newSubscriber("tf_static", tf2_msgs.TFMessage._TYPE);
        tfStaticSubscriber.addMessageListener(message -> {
            synchronized (mutex) {
                for (geometry_msgs.TransformStamped transform : message.getTransforms()) {
                    frameTransformTree.update(transform);
                }
            }
        });
    }

    private void startLayers() {
        for (Layer layer : layers) {
            layer.onStart(this, connectedNode);
        }
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    @Override
    public void onShutdown(Node node) {
        for (Layer layer : layers) {
            layer.onShutdown(this, node);
        }
        this.connectedNode = null;
    }

    @Override
    public void onShutdownComplete(Node node) {
    }

    @Override
    public void onError(Node node, Throwable throwable) {
    }
}