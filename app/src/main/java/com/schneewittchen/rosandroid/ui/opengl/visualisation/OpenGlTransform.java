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

package com.schneewittchen.rosandroid.ui.opengl.visualisation;

import org.ros.rosjava_geometry.Transform;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * An adapter for applying {@link Transform}s in an OpenGL context.
 *
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class OpenGlTransform {


    private static final ThreadLocal<FloatBuffer> buffer = new ThreadLocal<FloatBuffer>() {

        @Override
        protected FloatBuffer initialValue() {
            return FloatBuffer.allocate(16);
        }

        @Override
        public FloatBuffer get() {
            FloatBuffer buffer = super.get();
            buffer.clear();
            return buffer;
        }

    };

    private OpenGlTransform() {
        // Utility class.
    }

    /**
     * Applies a {@link Transform} to an OpenGL context.
     *
     * @param gl        the context
     * @param transform the {@link Transform} to apply
     */
    public static void apply(GL10 gl, Transform transform) {
        FloatBuffer matrix = buffer.get();

        for (double value : transform.toMatrix()) {
            matrix.put((float) value);
        }

        matrix.position(0);
        gl.glMultMatrixf(matrix);
    }

}
