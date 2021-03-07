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

import com.google.common.base.Preconditions;

import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class SubscriberLayer<T> extends DefaultLayer {

    private final GraphName topicName;
    private final String messageType;

    private Subscriber<T> subscriber;

    public SubscriberLayer(GraphName topicName, String messageType) {
        this.topicName = topicName;
        this.messageType = messageType;
    }

    @Override
    public void onStart(VisualizationView view, ConnectedNode connectedNode) {
        super.onStart(view, connectedNode);
        subscriber = connectedNode.newSubscriber(topicName, messageType);
    }

    @Override
    public void onShutdown(VisualizationView view, Node node) {
        subscriber.shutdown();
        super.onShutdown(view, node);
    }

    public Subscriber<T> getSubscriber() {
        Preconditions.checkNotNull(subscriber);
        return subscriber;
    }
}
