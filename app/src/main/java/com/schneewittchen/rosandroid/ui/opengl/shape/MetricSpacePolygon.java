/*
 * Copyright (C) 2014 Google Inc.
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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.Vertices;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;


/**
 * A polygon with metric space vertices.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MetricSpacePolygon extends BaseShape {

    final FloatBuffer vertexBuffer;
    final List<FloatBuffer> triangles;

    public MetricSpacePolygon(final float[] vertices, final ROSColor color) {
        super();
        vertexBuffer = Vertices.toFloatBuffer(vertices);
        setColor(color);

        final List<Triangulate.Point> points = Lists.newArrayList();
        final Triangulate.Point[] contour = new Triangulate.Point[vertices.length / 3];
        for (int i = 0; i < contour.length; ++i) {
            contour[i] = new Triangulate.Point(vertices[i * 3], vertices[i * 3 + 1]);
        }
        Preconditions.checkState(Triangulate.process(contour, points));

        triangles = Lists.newArrayList();
        for (int i = 0; i < points.size() / 3; ++i) {
            final FloatBuffer triangle = Vertices.allocateBuffer(3 * 3);
            for (int j = i * 3; j < i * 3 + 3; ++j) {
                triangle.put(points.get(j).x());
                triangle.put(points.get(j).y());
                triangle.put(0.f);
            }
            triangle.flip();
            triangles.add(triangle);
        }
    }

    @Override
    public void drawShape(VisualizationView view, GL10 gl) {
        final ROSColor translucent = getColor();
        translucent.setAlpha(0.3f);
        for (final FloatBuffer triangle : triangles) {
            Vertices.drawTriangleFan(gl, triangle, translucent);
        }
        final ROSColor opaque = getColor();
        opaque.setAlpha(1.f);
        Vertices.drawLineLoop(gl, vertexBuffer, opaque, 3.f);
        Vertices.drawPoints(gl, vertexBuffer, opaque, 10.f);
    }
}
