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

import com.schneewittchen.rosandroid.widgets.gltest.shape.PixelSpacePoseShape;
import com.schneewittchen.rosandroid.widgets.gltest.shape.Shape;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class RobotLayer extends DefaultLayer implements TfLayer {

    private final GraphName frame;

    private Shape shape;


    public RobotLayer(GraphName frame) {
        this.frame = frame;
    }

    public RobotLayer(String frame) {
        this(GraphName.of(frame));
    }


    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (shape != null) {
            shape.draw(view, gl);
        }
    }

    @Override
    public void onStart(VisualizationView view, ConnectedNode connectedNode) {
        shape = new PixelSpacePoseShape();
    }

    @Override
    public GraphName getFrame() {
        return frame;
    }
}