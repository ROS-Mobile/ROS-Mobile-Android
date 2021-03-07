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

import android.view.MotionEvent;

import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMainExecutor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Base class for visualization layers.
 * 
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public abstract class DefaultLayer implements Layer {

  @Override
  public void init(NodeMainExecutor nodeMainExecutor) {
  }
  
  @Override
  public void draw(VisualizationView view, GL10 gl) {
  }

  @Override
  public boolean onTouchEvent(VisualizationView view, MotionEvent event) {
    return false;
  }

  @Override
  public void onStart(VisualizationView view, ConnectedNode connectedNode) {
  }

  @Override
  public void onShutdown(VisualizationView view, Node node) {
  }
  
  @Override
  public void onSurfaceChanged(VisualizationView view, GL10 gl, int width, int height) {
  }
  
  @Override
  public void onSurfaceCreated(VisualizationView view, GL10 gl, EGLConfig config) {
  }
}
