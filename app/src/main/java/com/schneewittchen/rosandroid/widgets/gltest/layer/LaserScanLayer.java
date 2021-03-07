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

package com.schneewittchen.rosandroid.widgets.gltest.layer;

import com.schneewittchen.rosandroid.widgets.gltest.visualisation.ROSColor;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.Vertices;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import java.nio.FloatBuffer;


import javax.microedition.khronos.opengles.GL10;

import sensor_msgs.LaserScan;

/**
 * A {@link SubscriberLayer} that visualizes sensor_msgs/LaserScan messages.
 *
 * @author munjaldesai@google.com (Munjal Desai)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class LaserScanLayer extends SubscriberLayer<LaserScan> implements TfLayer {

    private static final ROSColor FREE_SPACE_COLOR = ROSColor.fromHexAndAlpha("377dfa", 0.1f);
    private static final ROSColor OCCUPIED_SPACE_COLOR = ROSColor.fromHexAndAlpha("377dfa", 0.3f);
    private static final float LASER_SCAN_POINT_SIZE = 10.f;
    private static final int LASER_SCAN_STRIDE = 15;

    private final Object mutex;

    private GraphName frame;
    private FloatBuffer vertexFrontBuffer;
    private FloatBuffer vertexBackBuffer;

    public LaserScanLayer(String topicName) {
        this(GraphName.of(topicName));
    }

    public LaserScanLayer(GraphName topicName) {
        super(topicName, LaserScan._TYPE);
        mutex = new Object();
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (vertexFrontBuffer != null) {
            synchronized (mutex) {
                Vertices.drawTriangleFan(gl, vertexFrontBuffer, FREE_SPACE_COLOR);
                // Drop the first point which is required for the triangle fan but is
                // not a range reading.
                FloatBuffer pointVertices = vertexFrontBuffer.duplicate();
                pointVertices.position(3);
                Vertices.drawPoints(gl, pointVertices, OCCUPIED_SPACE_COLOR, LASER_SCAN_POINT_SIZE);
            }
        }
    }

    @Override
    public void onStart(VisualizationView view, ConnectedNode connectedNode) {
        super.onStart(view, connectedNode);
        Subscriber<LaserScan> subscriber = getSubscriber();
        subscriber.addMessageListener(new MessageListener<LaserScan>() {
            @Override
            public void onNewMessage(LaserScan laserScan) {
                frame = GraphName.of(laserScan.getHeader().getFrameId());
                updateVertexBuffer(laserScan, LASER_SCAN_STRIDE);
            }
        });
    }

    private void updateVertexBuffer(LaserScan laserScan, int stride) {
        int vertexCount = 0;
        float[] ranges = laserScan.getRanges();
        int size = ((ranges.length / stride) + 2) * 3;
        if (vertexBackBuffer == null || vertexBackBuffer.capacity() < size) {
            vertexBackBuffer = Vertices.allocateBuffer(size);
        }
        vertexBackBuffer.clear();
        // We start with the origin of the triangle fan.
        vertexBackBuffer.put(0);
        vertexBackBuffer.put(0);
        vertexBackBuffer.put(0);
        vertexCount++;
        float minimumRange = laserScan.getRangeMin();
        float maximumRange = laserScan.getRangeMax();
        float angle = laserScan.getAngleMin();
        float angleIncrement = laserScan.getAngleIncrement();
        // Calculate the coordinates of the laser range values.
        for (int i = 0; i < ranges.length; i += stride) {
            float range = ranges[i];
            // Ignore ranges that are outside the defined range. We are not overly
            // concerned about the accuracy of the visualization and this is makes it
            // look a lot nicer.
            if (minimumRange < range && range < maximumRange) {
                // x, y, z
                vertexBackBuffer.put((float) (range * Math.cos(angle)));
                vertexBackBuffer.put((float) (range * Math.sin(angle)));
                vertexBackBuffer.put(0);
                vertexCount++;
            }
            angle += angleIncrement * stride;
        }
        vertexBackBuffer.position(0);
        vertexBackBuffer.limit(vertexCount * 3);
        synchronized (mutex) {
            FloatBuffer tmp = vertexFrontBuffer;
            vertexFrontBuffer = vertexBackBuffer;
            vertexBackBuffer = tmp;
        }
    }

    @Override
    public GraphName getFrame() {
        return frame;
    }
}
