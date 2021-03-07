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
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import geometry_msgs.PoseStamped;
import nav_msgs.Path;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Renders a nav_msgs/Path as a solid line.
 *
 * @author moesenle@google.com (Lorenz Moesenlechner)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class PathLayer extends SubscriberLayer<Path> implements TfLayer {

    private static final ROSColor COLOR = ROSColor.fromHexAndAlpha("03dfc9", 0.3f);
    private static final float LINE_WIDTH = 4.0f;

    private FloatBuffer vertexBuffer;
    private int numPoints;
    private boolean ready;
    private GraphName frame;

    public PathLayer(String topic) {
        this(GraphName.of(topic));
    }

    public PathLayer(GraphName topic) {
        super(topic, "nav_msgs/Path");
        ready = false;
        numPoints = 0;
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (ready) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            COLOR.apply(gl);
            gl.glLineWidth(LINE_WIDTH);
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, numPoints);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
    }

    @Override
    public void onStart(VisualizationView view, ConnectedNode connectedNode) {
        super.onStart(view, connectedNode);
        getSubscriber().addMessageListener(new MessageListener<Path>() {
            @Override
            public void onNewMessage(Path path) {
                updateVertexBuffer(path);
                ready = true;
            }
        });
    }

    private void updateVertexBuffer(Path path) {
        ByteBuffer goalVertexByteBuffer =
                ByteBuffer.allocateDirect(path.getPoses().size() * 3 * Float.SIZE);
        goalVertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = goalVertexByteBuffer.asFloatBuffer();
        int i = 0;
        if (path.getPoses().size() > 0) {
            frame = GraphName.of(path.getPoses().get(0).getHeader().getFrameId());
            for (PoseStamped pose : path.getPoses()) {
                vertexBuffer.put((float) pose.getPose().getPosition().getX());
                vertexBuffer.put((float) pose.getPose().getPosition().getY());
                vertexBuffer.put((float) pose.getPose().getPosition().getZ());
                i++;
            }
        }
        vertexBuffer.position(0);
        numPoints = i;
    }

    @Override
    public GraphName getFrame() {
        return frame;
    }
}
