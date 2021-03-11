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

package com.schneewittchen.rosandroid.ui.opengl.shape;

import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents a pose.
 * <p>
 * This shape is defined in pixel space and will not be affected by the zoom
 * level of the camera.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class PixelSpacePoseShape extends MetricSpacePoseShape {

    private static final float PIXELS_PER_METER = 250.f;

    @Override
    protected void scale(VisualizationView view, GL10 gl) {
        // Adjust for metric scale definition of MetricSpacePoseShape vertices.
        gl.glScalef(PIXELS_PER_METER, 250.f, 1.f);

        // Counter adjust for the camera zoom.
        gl.glScalef(1 / (float) view.getCamera().getZoom(),
                1 / (float) view.getCamera().getZoom(),
                1.0f);
    }
}
