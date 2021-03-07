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

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.schneewittchen.rosandroid.widgets.gltest.visualisation.OpenGlDrawable;
import com.schneewittchen.rosandroid.widgets.gltest.visualisation.VisualizationView;

import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMainExecutor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Interface for a drawable layer on a VisualizationView.
 * 
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public interface Layer extends OpenGlDrawable {


  void init(NodeMainExecutor nodeMainExecutor);
  
  /**
   * Event handler for touch events.
   * 
   * @param view
   *          the view generating the event
   * @param event
   *          the touch event
   * @return true if the event has been handled
   */
  boolean onTouchEvent(VisualizationView view, MotionEvent event);

  /**
   * Called when the layer is added to the {@link VisualizationView}.
   */
  void onStart(VisualizationView view, ConnectedNode connectedNode);

  /**
   * Called when the view is removed from the {@link VisualizationView}.
   */
  void onShutdown(VisualizationView view, Node node);

  /**
   * @param view
   *          the {@link VisualizationView} associated with the
   *          {@link GLSurfaceView.Renderer}
   * @see GLSurfaceView.Renderer#onSurfaceCreated(GL10, EGLConfig)
   */
  void onSurfaceCreated(VisualizationView view, GL10 gl, EGLConfig config);

  /**
   * @param view
   *          the {@link VisualizationView} associated with the
   *          {@link GLSurfaceView.Renderer}
   * @see GLSurfaceView.Renderer#onSurfaceChanged(GL10, int, int)
   */
  void onSurfaceChanged(VisualizationView view, GL10 gl, int width, int height);
}
