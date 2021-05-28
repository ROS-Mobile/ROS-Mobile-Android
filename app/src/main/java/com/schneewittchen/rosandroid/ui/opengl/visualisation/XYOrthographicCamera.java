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

import com.google.common.base.Preconditions;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.TransformProvider;

import org.ros.math.RosMath;
import org.ros.namespace.GraphName;
import org.ros.rosjava_geometry.FrameTransform;
import org.ros.rosjava_geometry.FrameTransformTree;
import org.ros.rosjava_geometry.Transform;
import org.ros.rosjava_geometry.Vector3;

import javax.microedition.khronos.opengles.GL10;


/**
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class XYOrthographicCamera {

    public static final String TAG = XYOrthographicCamera.class.getSimpleName();

    /**
     * Pixels per meter in the world. If zoom is set to the number of pixels per
     * meter (the display density) then 1 cm in the world will be displayed as 1
     * cm on the display.
     */
    private static final double PIXELS_PER_METER = 100.0;

    /**
     * Transforms from our ROS frame (the data frame) to the screen frame (our
     * target frame) by rotating the coordinate system so that x is forward and y
     * is left. See <a href="http://www.ros.org/reps/rep-0103.html">REP 103</a>.
     */
    private static final Transform ROS_TO_SCREEN_TRANSFORM = Transform.zRotation(Math.PI / 2).scale(
            PIXELS_PER_METER);

    /**
     * Most the user can zoom in.
     */
    private static final float MINIMUM_ZOOM_FACTOR = 0.1f;

    /**
     * Most the user can zoom out.
     */
    private static final float MAXIMUM_ZOOM_FACTOR = 5.f;

    private final FrameTransformTree frameTransformTree;
    private final Object mutex;

    private Viewport viewport;

    /**
     * Transforms from camera frame (our data frame) to the ROS frame (our target
     * frame). See {@link #ROS_TO_SCREEN_TRANSFORM}.
     */
    private Transform cameraToRosTransform;

    /**
     * The frame in which to render everything. The default value is /map which
     * indicates that everything is rendered in map. If this is changed to, for
     * instance, base_link, the view follows the robot and the robot itself is in
     * the origin.
     */
    private GraphName frame;


    public XYOrthographicCamera() {
        this.frameTransformTree = TransformProvider.getInstance().getTree();
        this.mutex = new Object();

        this.resetTransform();
    }


    private void resetTransform() {
        cameraToRosTransform = Transform.identity();
    }

    public void apply(GL10 gl) {
        synchronized (mutex) {
            OpenGlTransform.apply(gl, ROS_TO_SCREEN_TRANSFORM);
            OpenGlTransform.apply(gl, cameraToRosTransform);
        }
    }

    public boolean applyFrameTransform(GL10 gl, GraphName frame) {
        if (this.frame == null) return false;

        FrameTransform frameTransform = frameTransformTree.transform(frame, this.frame);
        if (frameTransform == null) return false;

        OpenGlTransform.apply(gl, frameTransform.getTransform());
        return true;
    }

    /**
     * Translates the camera.
     *
     * @param deltaX distance to move in x in pixels
     * @param deltaY distance to move in y in pixels
     */
    public void translate(double deltaX, double deltaY) {
        synchronized (mutex) {
            cameraToRosTransform =
                    ROS_TO_SCREEN_TRANSFORM.invert().multiply(Transform.translation(deltaX, deltaY, 0))
                            .multiply(getCameraToScreenTransform());
        }
    }

    private Transform getCameraToScreenTransform() {
        return ROS_TO_SCREEN_TRANSFORM.multiply(cameraToRosTransform);
    }

    public Transform getScreenTransform(GraphName targetFrame) {
        FrameTransform frameTransform = frameTransformTree.transform(frame, targetFrame);
        return frameTransform.getTransform().multiply(getCameraToScreenTransform().invert());
    }

    /**
     * Rotates the camera round the specified coordinates.
     *
     * @param focusX     the x coordinate to focus on
     * @param focusY     the y coordinate to focus on
     * @param deltaAngle the camera will be rotated by {@code deltaAngle} radians
     */
    public void rotate(double focusX, double focusY, double deltaAngle) {
        synchronized (mutex) {
            Transform focus = Transform.translation(toCameraFrame((int) focusX, (int) focusY));
            cameraToRosTransform =
                    cameraToRosTransform.multiply(focus).multiply(Transform.zRotation(deltaAngle))
                            .multiply(focus.invert());
        }
    }

    /**
     * Zooms the camera around the specified focus coordinates.
     *
     * @param focusX the x coordinate to focus on
     * @param focusY the y coordinate to focus on
     * @param factor the zoom will be scaled by this factor
     */
    public void zoom(double focusX, double focusY, double factor) {
        synchronized (mutex) {
            Transform focus = Transform.translation(toCameraFrame((int) focusX, (int) focusY));
            double scale = cameraToRosTransform.getScale();
            double zoom = RosMath.clamp(scale * factor, MINIMUM_ZOOM_FACTOR, MAXIMUM_ZOOM_FACTOR) / scale;
            cameraToRosTransform =
                    cameraToRosTransform.multiply(focus).scale(zoom).multiply(focus.invert());
        }
    }

    /**
     * @return the current zoom level in pixels per meter
     */
    public double getZoom() {
        return cameraToRosTransform.getScale() * PIXELS_PER_METER;
    }

    /**
     * @return the provided pixel coordinates (where the origin is the top left
     * corner of the view) in the camera {@link #frame}
     */
    public Vector3 toCameraFrame(int pixelX, int pixelY) {
        final double centeredX = pixelX - viewport.getWidth() / 2.0d;
        final double centeredY = viewport.getHeight() / 2.0d - pixelY;
        return getCameraToScreenTransform().invert().apply(new Vector3(centeredX, centeredY, 0));
    }

    /**
     * @param pixelX the x coordinate on the screen (origin top left) in pixels
     * @param pixelY the y coordinate on the screen (origin top left) in pixels
     * @param frame  the frame to transform the coordinates into (e.g. "map")
     * @return the pixel coordinate in the specified frame
     */
    public Transform toFrame(final int pixelX, final int pixelY, final GraphName frame) {
        final Transform translation = Transform.translation(toCameraFrame(pixelX, pixelY));
        final FrameTransform cameraToFrame =
                frameTransformTree.transform(this.frame, frame);

        if (cameraToFrame == null) return null;

        return cameraToFrame.getTransform().multiply(translation);
    }

    public Transform toFrame(float x, float y) {
        return toFrame((int)x, (int)y, this.frame);
    }

    public GraphName getFrame() {
        return frame;
    }

    /**
     * Changes the camera frame to the specified frame.
     * <p/>
     * If possible, the camera will avoid jumping on the next frame.
     *
     * @param frame the new camera frame
     */
    public void setFrame(GraphName frame) {
        Preconditions.checkNotNull(frame);
        synchronized (mutex) {
            if (this.frame != null && this.frame != frame) {
                FrameTransform frameTransform = frameTransformTree.transform(frame, this.frame);
                if (frameTransform != null) {
                    // Best effort to prevent the camera from jumping. If we don't have
                    // the transform yet, we can't help matters.
                    cameraToRosTransform = cameraToRosTransform.multiply(frameTransform.getTransform());
                }
            }
            this.frame = frame;
        }
    }

    /**
     * @see #setFrame(GraphName)
     */
    public void setFrame(String frame) {
        setFrame(GraphName.of(frame));
    }

    /**
     * Changes the camera frame to the specified frame and aligns the camera with
     * the new frame.
     *
     * @param frame the new camera frame
     */
    public void jumpToFrame(GraphName frame) {
        synchronized (mutex) {
            this.frame = frame;
            final double scale = cameraToRosTransform.getScale();
            resetTransform();
            cameraToRosTransform = cameraToRosTransform.scale(scale / cameraToRosTransform.getScale());
        }
    }

    /**
     * @see #jumpToFrame(GraphName)
     */
    public void jumpToFrame(String frame) {
        jumpToFrame(GraphName.of(frame));
    }

    public void setViewport(Viewport viewport) {
        Preconditions.checkNotNull(viewport);
        this.viewport = viewport;
    }

    public Viewport getViewport() {
        Preconditions.checkNotNull(viewport);
        return viewport;
    }

    public Transform getCameraToRosTransform() {
        // Transforms are immutable. No need for a defensive copy.
        return cameraToRosTransform;
    }
}
