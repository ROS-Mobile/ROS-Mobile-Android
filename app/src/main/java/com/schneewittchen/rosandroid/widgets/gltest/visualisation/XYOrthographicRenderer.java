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

import android.opengl.GLSurfaceView;

import com.schneewittchen.rosandroid.widgets.gltest.layer.Layer;
import com.schneewittchen.rosandroid.widgets.gltest.layer.TfLayer;

import org.ros.namespace.GraphName;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Renders all layers of a navigation view.
 *
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class XYOrthographicRenderer implements GLSurfaceView.Renderer {


    private static final ROSColor BACKGROUND_COLOR = new ROSColor(0.87f, 0.87f, 0.87f, 1.f);

    private final VisualizationView view;

    public XYOrthographicRenderer(VisualizationView view) {
        this.view = view;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Viewport viewport = new Viewport(width, height);
        viewport.apply(gl);
        view.getCamera().setViewport(viewport);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glClearColor(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(),
                BACKGROUND_COLOR.getBlue(), BACKGROUND_COLOR.getAlpha());
        for (Layer layer : view.getLayers()) {
            layer.onSurfaceChanged(view, gl, width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        view.getCamera().apply(gl);
        drawLayers(gl);
    }

    private void drawLayers(GL10 gl) {
        for (Layer layer : view.getLayers()) {
            gl.glPushMatrix();

            if (layer instanceof TfLayer) {
                GraphName layerFrame = ((TfLayer) layer).getFrame();
                if (layerFrame != null && view.getCamera().applyFrameTransform(gl, layerFrame)) {
                    layer.draw(view, gl);
                }

            } else {
                layer.draw(view, gl);
            }

            gl.glPopMatrix();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        for (Layer layer : view.getLayers()) {
            layer.onSurfaceCreated(view, gl, config);
        }
    }
}