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

import java.util.List;

/**
 * Triangulates a contour for rendering as a triangle fan.
 *
 * @author damonkohler@google.com (Damon Kohler)
 * @see <a href="http://www.flipcode.com/archives/Efficient_Polygon_Triangulation
 * .shtml">Efficient Polygon Triangulation</a>
 */
public class Triangulate {

    private static final float EPSILON = 1e-9f;

    /**
     * Defines a point in 2D space.
     */
    public static class Point {
        private final float x;
        private final float y;

        public Point(final float x, final float y) {
            this.x = x;
            this.y = y;
        }

        public float x() {
            return x;
        }

        public float y() {
            return y;
        }
    }

    /**
     * Triangulate a contour/polygon.
     *
     * @param contour the vertices of the polygon
     * @param result  the result series of triangles
     * @return true on success
     */
    public static boolean process(final Point[] contour, List<Point> result) {
        // Allocate and initialize list of Vertices in polygon.
        int n = contour.length;
        if (n < 3) {
            return false;
        }

        int[] V = new int[n];

        // We want a counter-clockwise polygon in V.
        if (0.0f < area(contour)) {
            for (int v = 0; v < n; v++) {
                V[v] = v;
            }
        } else {
            for (int v = 0; v < n; v++) {
                V[v] = (n - 1) - v;
            }
        }

        int nv = n;

        // Remove nv-2 Vertices, creating 1 triangle every time.
        int count = 2 * nv;  // error detection

        for (int m = 0, v = nv - 1; nv > 2; ) {
            // If we loop, it is probably a non-simple polygon.
            if (0 >= (count--)) {
                // Triangulate: ERROR - probable bad polygon!
                return false;
            }

            // Three consecutive vertices in current polygon, <u,v,w>
            int u = v;
            if (nv <= u) {
                u = 0;  // previous
            }
            v = u + 1;
            if (nv <= v) {
                v = 0;  // new v
            }
            int w = v + 1;
            if (nv <= w) {
                w = 0;  // next
            }

            if (snip(contour, u, v, w, nv, V)) {
                // True names of the vertices.
                final int a = V[u];
                final int b = V[v];
                final int c = V[w];

                // Output Triangle
                result.add(contour[a]);
                result.add(contour[b]);
                result.add(contour[c]);

                m++;

                // Remove v from remaining polygon.
                for (int s = v, t = v + 1; t < nv; s++, t++) {
                    V[s] = V[t];
                }
                nv--;

                // Reset error detection counter.
                count = 2 * nv;
            }
        }

        return true;
    }

    /**
     * Compute area of a contour/polygon.
     *
     * @param contour the contour to measure the area of
     * @return the area defined by the contour
     */
    public static float area(final Point[] contour) {
        final int n = contour.length;
        float A = 0.0f;
        for (int p = n - 1, q = 0; q < n; p = q++) {
            A += contour[p].x() * contour[q].y() - contour[q].x() * contour[p].y();
        }
        return A * 0.5f;
    }

    /**
     * Decide if point (Px, Py) is inside triangle defined by
     * ((Ax,Ay), (Bx,By), (Cx,Cy)).
     *
     * @return true if the test point lies inside the triangle
     */
    public static boolean isInsideTriangle(final float Ax, final float Ay,
                                           final float Bx, final float By,
                                           final float Cx, final float Cy,
                                           final float Px, final float Py) {
        final float ax = Cx - Bx;
        final float ay = Cy - By;
        final float bx = Ax - Cx;
        final float by = Ay - Cy;
        final float cx = Bx - Ax;
        final float cy = By - Ay;
        final float apx = Px - Ax;
        final float apy = Py - Ay;
        final float bpx = Px - Bx;
        final float bpy = Py - By;
        final float cpx = Px - Cx;
        final float cpy = Py - Cy;
        final float aCROSSbp = ax * bpy - ay * bpx;
        final float cCROSSap = cx * apy - cy * apx;
        final float bCROSScp = bx * cpy - by * cpx;
        return ((aCROSSbp >= 0.0f) && (bCROSScp >= 0.0f) && (cCROSSap >= 0.0f));
    }

    private static boolean snip(Point[] contour, int u, int v, int w, int n, int[] V) {
        final float Ax = contour[V[u]].x();
        final float Ay = contour[V[u]].y();
        final float Bx = contour[V[v]].x();
        final float By = contour[V[v]].y();
        final float Cx = contour[V[w]].x();
        final float Cy = contour[V[w]].y();

        if (EPSILON > (((Bx - Ax) * (Cy - Ay)) - ((By - Ay) * (Cx - Ax)))) {
            return false;
        }

        for (int p = 0; p < n; p++) {
            if ((p == u) || (p == v) || (p == w)) {
                continue;
            }
            final float Px = contour[V[p]].x();
            final float Py = contour[V[p]].y();
            if (isInsideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py)) {
                return false;
            }
        }
        return true;
    }
}
