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
import android.util.Log;
import android.view.MotionEvent;

import com.google.common.collect.Lists;
import com.schneewittchen.rosandroid.widgets.gltest.layer.CameraControl;
import com.schneewittchen.rosandroid.widgets.gltest.layer.Layer;
import com.schneewittchen.rosandroid.widgets.gltest.layer.SubscriberLayer;

import org.ros.internal.message.Message;
import org.ros.rosjava_geometry.FrameTransformTree;

import java.util.Collections;
import java.util.List;

import geometry_msgs.TransformStamped;
import tf2_msgs.TFMessage;


/**
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 * @version 1.0.0
 * @updated on 08.07.2021
 * @modified by Nico Studt
 */
public class VisualizationView extends GLSurfaceView {

    public static String TAG = VisualizationView.class.getSimpleName();
    private static final boolean DEBUG = true;

    private final FrameTransformTree frameTransformTree = new FrameTransformTree();
    private final XYOrthographicCamera camera = new XYOrthographicCamera(frameTransformTree);
    private CameraControl cameraControl;
    private List<Layer> layers;
    private XYOrthographicRenderer renderer;


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

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        renderer = new XYOrthographicRenderer(this);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        cameraControl = new CameraControl(this);
        cameraControl.init(true, true, true);
        camera.jumpToFrame("map");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(cameraControl.onTouchEvent(event)) {
            this.requestRender();
            return true;
        }

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


    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public void onNewMessage(Message message) {
        boolean dirtyView = false;

        if (message instanceof TFMessage) {
            TFMessage tf = (TFMessage) message;

            for (TransformStamped transform: tf.getTransforms()) {
                frameTransformTree.update(transform);
            }

            dirtyView = false;
        }

        for(Layer layer: getLayers()) {
            if (layer instanceof SubscriberLayer) {
                if (((SubscriberLayer<?>)layer).reactOnMessage(this, message)) {
                    dirtyView = true;
                }
            }
        }

        if (dirtyView) {
            this.requestRender();
        }
    }
}