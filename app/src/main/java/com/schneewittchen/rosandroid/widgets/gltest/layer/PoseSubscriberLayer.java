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

import com.schneewittchen.rosandroid.widgets.gltest.shape.GoalShape;
import com.schneewittchen.rosandroid.widgets.gltest.shape.Shape;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.rosjava_geometry.FrameTransform;
import org.ros.rosjava_geometry.Transform;

import javax.microedition.khronos.opengles.GL10;

import geometry_msgs.PoseStamped;

/**
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class PoseSubscriberLayer extends SubscriberLayer<PoseStamped> implements TfLayer {

    private final GraphName targetFrame;

    private Shape shape;
    private boolean ready;

    public PoseSubscriberLayer(String topic) {
        this(GraphName.of(topic));
    }

    public PoseSubscriberLayer(GraphName topic) {
        super(topic, PoseStamped._TYPE);
        targetFrame = GraphName.of("map");
        ready = false;
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (ready) {
            shape.draw(view, gl);
        }
    }

    @Override
    public void onStart(final VisualizationView view, ConnectedNode connectedNode) {
        super.onStart(view, connectedNode);
        shape = new GoalShape();
        getSubscriber().addMessageListener(new MessageListener<PoseStamped>() {
            @Override
            public void onNewMessage(PoseStamped pose) {
                GraphName source = GraphName.of(pose.getHeader().getFrameId());
                FrameTransform frameTransform = view.getFrameTransformTree().transform(source, targetFrame);
                if (frameTransform != null) {
                    Transform poseTransform = Transform.fromPoseMessage(pose.getPose());
                    shape.setTransform(frameTransform.getTransform().multiply(poseTransform));
                    ready = true;
                }
            }
        });
    }

    @Override
    public GraphName getFrame() {
        return targetFrame;
    }
}